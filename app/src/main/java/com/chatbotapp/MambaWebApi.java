package com.chatbotapp;


import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Logon;
import com.chatbotapp.mambaObj.SearchResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Alex
 */
public class MambaWebApi {

    private static final String DEFAULT_HOST = "api.mobile-api.ru";

    private static final String DEFAULT_PARAMETER = "langId=de&dateType=timestamp";

    private static final String CREATE_REQ = "POST";
    private static final String READ_REQ = "GET";
    private static final String UPDATE_REQ = "PUT";
    private static final String DELETE_REQ = "DELETE";

    private final Map<String, String> cookie = new HashMap<String, String>();

    private final Semaphore sema = new Semaphore(1);

    private final ILog log;

    public MambaWebApi(ILog log) {
        this.log = log;


/*
 double DEFAULT_LAT = 53.599815d;
 double DEFAULT_LON = 9.933121d;

 try {
 MambaWebApi api = new MambaWebApi(this);
 api.setCookie("dslkklkfdsfdslkfdslk");

 api.search("active", "nath", DEFAULT_LAT, DEFAULT_LON, new MambaWebApi.IResponse<SearchResult>() {
@Override public void doResponse(SearchResult result) {
for(User user : result.getUsers()) {
Log.d(getString(R.string.log_tag), user.getName() + " --> " +  user.toString());
}

}
});
 } catch (Exception e) {
 }
 */
    }

/*
    private Thread getSessionCookie() throws Exception {
        cookie = "";

        final Thread queryResult = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;

                try {
                    String fullUrl = "https://" + DEFAULT_HOST + "/v5.2.38.0/login/builder/?" + DEFAULT_PARAMETER;
                    log.l(ILog.LogLevel.debug, "Send query to '" + fullUrl + "'...", null);

                    connection = (HttpsURLConnection) new URL(fullUrl).openConnection();
                    connection.setRequestMethod(READ_REQ);
                    connection.setRequestProperty("Host", DEFAULT_HOST);
                    connection.setRequestProperty("User-Agent", "okhttp/2.2.0");

                    makeCookie(connection.getHeaderFields().get("Set-Cookie"));
                    log.l(ILog.LogLevel.debug, "Generated cookie: " + cookie, null);
                } catch (Exception e) {
                    log.l(ILog.LogLevel.error, "Failed to send a query.", e);
                } finally {
                    try {
                        if (connection != null)
                            connection.disconnect();
                    } catch (Exception e) {
                        log.l(ILog.LogLevel.warn, "Failed to close the connection.", e);
                    }
                }
            }
        });

        queryResult.start();

        return queryResult;
    }
    */


    public Thread logon(final String email, final String password, final IResponse<Logon> response) throws Exception {
        // getSessionCookie().join();
        Thread result;

        try {
            String data = "";
            data += "{";
            data += "   \"login\":{";
            data += "      \"login\":\"" + email + "\",";
            data += "      \"password\":\"" + password + "\"";
            data += "  }";
            data += "}";

            result = query(response, Logon.class, CREATE_REQ, "v5.2.38.0/login/builder/", null, data);
        } catch (Exception e) {
            log.l(ILog.LogLevel.error, "Failed to generate the result of a search-request.", e);
            throw e;
        }

        return result;
    }


    public Thread getChat(String userId, final IResponse<ChatMessages> response) throws Exception {
        return getChat(userId, 20, 0, response); // Default values from the request.
    }


    public Thread getChat(String userId, int limit, int offset, final IResponse<ChatMessages> response) throws Exception {
        Thread result;

        try {
            result = query(response, ChatMessages.class, READ_REQ,
                    "v5.2.38.0/users/" + userId + "/chat/",
                    "?limit=" + limit + "&offset=" + offset,
                    null);
        } catch (Exception e) {
            log.l(ILog.LogLevel.error, "Failed to generate the result of a getChat-request.", e);
            throw e;
        }

        return result;
    }


    public Thread search(String filter, String search, double lat, double lon, final IResponse<SearchResult> response) throws Exception {
        Thread result ;

        try {
            String data = "";
            data += "{";
            data += "   'filter': '" + filter + "',";
            data += "   'search': '" + search + "',";
            data += "   'lat': " + lat + ",";
            data += "   'lng': " + lon + ",";
            data += "   'limit': 40,";
            data += "   'lang_id': 'de',";
            data += "   'dateType': 'timestamp'";
            data += "}";

            result = query(response, SearchResult.class, READ_REQ, "v5.2.38.0/search/", null, data);
        } catch (Exception e) {
            log.l(ILog.LogLevel.error, "Failed to generate the result of a search-request.", e);
            throw e;
        }

        return result;
    }


    private <T extends AResponse> Thread query(final IResponse<T> response,
                                               final Class<T> resultClass,
                                               final String reqMethod,
                                               final String url,
                                               final String urlParams,
                                               final String data) throws Exception {
        final Thread queryResult = new Thread(new Runnable() {
            @Override
            public void run() {
                T result ;
                HttpsURLConnection connection = null;
                OutputStreamWriter request = null;
                StringBuilder sb = new StringBuilder();

                try {
                    String fullUrl = "https://" + DEFAULT_HOST + "/" + url + (urlParams == null ? "?" : urlParams + "&") + DEFAULT_PARAMETER;
                    log.l(ILog.LogLevel.debug, "Send query to '" + fullUrl + "'...", null);

                    connection = (HttpsURLConnection) new URL(fullUrl).openConnection();
                    connection.setRequestMethod(reqMethod);

                    if (cookie.size() > 0) {
                        StringBuilder cookieStr = new StringBuilder();

                        for (Map.Entry<String, String> cookieVal : cookie.entrySet()) {
                            cookieStr.append(cookieVal.getKey()).append("=").append(cookieVal.getValue()).append("; ");
                        }

                        connection.setRequestProperty("Cookie", cookieStr.toString());
                    }

                    connection.setRequestProperty("Host", DEFAULT_HOST);
                    connection.setRequestProperty("User-Agent", "okhttp/2.2.0");
                    connection.setRequestProperty("Content-Type", "application/json");

                    if (data != null && !data.isEmpty()) {
                        connection.setRequestProperty("Content-Length", String.valueOf(data.length()));

                        connection.setDoOutput(true);
                        request = new OutputStreamWriter(connection.getOutputStream());
                        request.write(data);
                        request.flush();
                        request.close();
                    }

                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    makeCookie(connection.getHeaderFields().get("Set-Cookie"));

                    log.l(ILog.LogLevel.debug, "Got result:", null);
                    log.l(ILog.LogLevel.debug, sb.toString(), null);
                } catch (Exception e) {
                    log.l(ILog.LogLevel.error, "Failed to send a query.", e);
                } finally {
                    try {
                        if (request != null)
                            request.close();
                    } catch (Exception e) {
                        log.l(ILog.LogLevel.warn, "Failed to close the request.", e);
                    }

                    try {
                        if (connection != null)
                            connection.disconnect();
                    } catch (Exception e) {
                        log.l(ILog.LogLevel.warn, "Failed to close the connection.", e);
                    }
                }

                try {
                    log.l(ILog.LogLevel.debug, "Generate result class from json-result...", null);

                    result = AResponse.GSON.fromJson(sb.toString(), resultClass);

                    log.l(ILog.LogLevel.debug, "Result successfully generated.", null);
                } catch (Exception e) {
                    log.l(ILog.LogLevel.error, "Failed to generate result class from json-result.", null);
                    throw e;
                }

                response.doResponse(result);
            }
        });

        queryResult.run();

        return queryResult;
    }

    private void makeCookie(List<String> cookieFields) throws Exception {
        sema.acquire();

        try {
            log.l(ILog.LogLevel.debug, "Generate cookie from field list...", null);

            if (cookieFields == null)
                throw new Exception("Es wurden keine Cookies gesetzt!");

            // s_post=7zjJD8f3pfh34NIgyYmRZgtDVfx3F0ZA; path=/; domain=api.mobile-api.ru
            // mmbsid=V6MieupiCGF5XdUJqIMObhqCI7ndymFX_20170514145128_api.mobile-api.ru; path=/; domain=api.mobile-api.ru
            // return_token=iBeiQX34hK1qlw9ofYI8UK4IdZtuZhDa; expires=Mon, 14-May-2018 11:51:28 GMT; Max-Age=31536000; path=/; domain=.api.mobile-api.ru; HttpOnly

            for (String cookieField : cookieFields) {
                int index = cookieField.indexOf(';');
                String value = cookieField.substring(0, index >= 0 ? index : cookieField.length() - 1);
                String[] keyValue = value.split("=");
                cookie.put(keyValue[0], keyValue[1]);
            }

            log.l(ILog.LogLevel.debug, "Successfully generated cookie from field list.", null);
        } finally {
            sema.release();
        }
    }

    public interface IResponse<T extends AResponse> {

        void doResponse(T result);

    }

    public interface ILog {

        void l(LogLevel level, String message, Exception e);

        public enum LogLevel {
            trace, debug, info, warn, error, fatal;
        }

    }
}