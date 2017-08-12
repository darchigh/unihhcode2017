package com.chatbotapp;


import android.os.AsyncTask;
import android.util.Log;

import com.chatbotapp.mambaObj.ChatAcknowledge;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Contacts;
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
 * @author Alex
 */
public class MambaWebApi {

    public static final int DEFAULT_LIMIT = 20;

    private static final String DEFAULT_HOST = "api.mobile-api.ru";

    private static final String DEFAULT_PARAMETER = "langId=de&dateType=timestamp";

    private static final String CREATE_REQ = "POST";
    private static final String READ_REQ = "GET";
    private static final String UPDATE_REQ = "PUT";
    private static final String DELETE_REQ = "DELETE";

    private final Map<String, String> cookie = new HashMap<String, String>();

    private final Semaphore sema = new Semaphore(1);

    private final String tag;

    public MambaWebApi() {
        this(MambaWebApi.class.getSimpleName());
    }


    public MambaWebApi(String tag) {
        this.tag = tag;
    }


    public AsyncTask logon(final String email, final String password, final IResponse<Logon> response) throws Exception {
        AsyncTask result;

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
            Log.e(tag, "Failed to generate the result of a logon-request.", e);
            throw e;
        }

        return result;
    }


    public AsyncTask sendMessage(String receiverId, String message, final IResponse<ChatAcknowledge> response) throws Exception {
        AsyncTask result = null;

        try {
            String data = "";
            data += "{";
            data += "   \"message\":\"" + message.replace('\"', '\'') + "\",";
            data += "   \"dateType\":\"timestamp\",";
            data += "   \"lang_id\":\"de\",";
            data += "   \"lat\":0.0,";
            data += "   \"lng\":0.0";
            data += "}";

            result = query(response, ChatAcknowledge.class, CREATE_REQ,
                    "v5.2.38.0/users/" + receiverId + "/post/", null, data);
        } catch (Exception e) {
            Log.e(tag, "Failed to generate the result of a sendMessage-request.", e);
            throw e;
        }

        return result;
    }

    public AsyncTask getContacts(final IResponse<Contacts> response) throws Exception {
        return getContacts(DEFAULT_LIMIT, 0, response);
    }

    public AsyncTask getContacts(int limit, int offset, final IResponse<Contacts> response) throws Exception {
        AsyncTask result;

        try {
            result = query(response, Contacts.class, READ_REQ,
                    "v5.2.38.0/contacts/all/",
                    "?status=all&lastMessage=true&limit=" + limit + "&offset=" + offset,
                    null);
        } catch (Exception e) {
            Log.e(tag, "Failed to generate the result of a getContacts-request.", e);
            throw e;
        }

        return result;
    }


    public AsyncTask getChat(String userId, final IResponse<ChatMessages> response) throws Exception {
        return getChat(userId, DEFAULT_LIMIT, 0, response); // Default values from the request.
    }


    public AsyncTask getChat(String userId, int limit, int offset, final IResponse<ChatMessages> response) throws Exception {
        AsyncTask result;

        try {
            result = query(response, ChatMessages.class, READ_REQ,
                    "v5.2.38.0/users/" + userId + "/chat/",
                    "?limit=" + limit + "&offset=" + offset,
                    null);
        } catch (Exception e) {
            Log.e(tag, "Failed to generate the result of a getChat-request.", e);
            throw e;
        }

        return result;
    }


    public AsyncTask search(String filter, String search, double lat, double lon, final IResponse<SearchResult> response) throws Exception {
        AsyncTask result;

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
            Log.e(tag, "Failed to generate the result of a search-request.", e);
            throw e;
        }

        return result;
    }


    private <T extends AResponse> AsyncTask query(final IResponse<T> response,
                                                  final Class<T> resultClass,
                                                  final String reqMethod,
                                                  final String url,
                                                  final String urlParams,
                                                  final String data) throws Exception {

        AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>() {


            @Override
            protected void onPostExecute(T t) {
                response.doResponse(t);
            }

            @Override
            protected T doInBackground(Void... params) {
                T result;
                HttpsURLConnection connection = null;
                OutputStreamWriter request = null;
                StringBuilder sb = new StringBuilder();

                try {
                    String fullUrl = "https://" + DEFAULT_HOST + "/" + url + (urlParams == null ? "?" : urlParams + "&") + DEFAULT_PARAMETER;
                    Log.d(tag, "Send query to '" + fullUrl + "'...");

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

                    Log.d(tag, "Got result:");
                    Log.d(tag, sb.toString());
                } catch (Exception e) {
                    Log.e(tag, "Failed to send a query.", e);
                } finally {
                    try {
                        if (request != null)
                            request.close();
                    } catch (Exception e) {
                        Log.w(tag, "Failed to close the request.", e);
                    }

                    try {
                        if (connection != null)
                            connection.disconnect();
                    } catch (Exception e) {
                        Log.w(tag, "Failed to close the connection.", e);
                    }
                }

                try {
                    Log.d(tag, "Generate result class from json-result...");

                    result = AResponse.GSON.fromJson(sb.toString(), resultClass);

                    Log.d(tag, "Result successfully generated.");
                } catch (Exception e) {
                    Log.e(tag, "Failed to generate result class from json-result.");
                    throw e;
                }

                return result;
            }
        };

        task.execute();

        return task;
    }

    private void makeCookie(List<String> cookieFields) throws Exception {
        sema.acquire();

        try {
            Log.d(tag, "Generate cookie from field list...");

            if (cookieFields == null)
                Log.d(tag, "Es wurden keine Cookies gesetzt!");
            else {
                // s_post=7zjJD8f3pfh34NIgyYmRZgtDVfx3F0ZA; path=/; domain=api.mobile-api.ru
                // mmbsid=V6MieupiCGF5XdUJqIMObhqCI7ndymFX_20170514145128_api.mobile-api.ru; path=/; domain=api.mobile-api.ru
                // return_token=iBeiQX34hK1qlw9ofYI8UK4IdZtuZhDa; expires=Mon, 14-May-2018 11:51:28 GMT; Max-Age=31536000; path=/; domain=.api.mobile-api.ru; HttpOnly

                for (String cookieField : cookieFields) {
                    int index = cookieField.indexOf(';');
                    String value = cookieField.substring(0, index >= 0 ? index : cookieField.length() - 1);
                    String[] keyValue = value.split("=");
                    cookie.put(keyValue[0], keyValue[1]);
                }

                Log.d(tag, "Successfully generated cookie from field list.");
            }
        } finally {
            sema.release();
        }
    }

    public interface IResponse<T extends AResponse> {

        void doResponse(T result);

    }
}