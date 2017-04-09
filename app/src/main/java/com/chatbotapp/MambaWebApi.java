package com.chatbotapp;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.SearchResult;
import com.chatbotapp.mambaObj.User;
import com.google.gson.Gson;
import com.liisa.chatbotapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by aossa on 08.04.2017.
 */

public class MambaWebApi {

    private static final Gson GSON = new Gson();

    private static final String DEFAULT_HOST = "api.mobile-api.ru";

    private static final String DEFAULT_PARAMETER = "langId=de&dateType=timestamp";

    private static final String CREATE_REQ = "POST";
    private static final String READ_REQ = "GET";
    private static final String UPDATE_REQ = "PUT";
    private static final String DELETE_REQ = "DELETE";

    private Activity act;
    private String cookie;

    public MambaWebApi(Activity act) {
        this.act = act;


/**
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


    public void getChat(String userId, IResponse<ChatMessages> response)  throws Exception {
        getChat(userId, 20, 0,response); // Default values from the request.
    }


    public void getChat(String userId, int limit, int offset,IResponse<ChatMessages> response) throws Exception {
        try {
            StringBuilder url = new StringBuilder();
            url.append("v5.2.38.0/");
            url.append("users/").append(userId).append("/chat/");
            url.append("?limit=").append(limit);
            url.append("&offset=").append(offset);
            url.append("&").append(DEFAULT_PARAMETER);

            query(response, ChatMessages.class, READ_REQ, url.toString(), "");
        } catch (Exception e) {
            Log.e(Resources.getSystem().getString(R.string.log_tag),
                    "Failed to generate the result of a getChat-request.", e);
            throw e;
        }
    }


    public void search(String filter, String search, double lat, double lon, IResponse<SearchResult> response) throws Exception {
        try {
            StringBuilder data = new StringBuilder();
            data.append("{");
            data.append("   'filter': '").append(filter).append("',");
            data.append("   'search': '").append(search).append("',");
            data.append("   'lat': ").append(lat).append(",");
            data.append("   'lng': ").append(lon).append(",");
            data.append("   'limit': 40,");
            data.append("   'lang_id': 'de',");
            data.append("   'dateType': 'timestamp'");
            data.append("}");

            StringBuilder url = new StringBuilder();
            url.append("v5.2.38.0/search/").append("?").append(DEFAULT_PARAMETER);

            query(response, SearchResult.class, READ_REQ, url.toString(), data.toString());
        } catch (Exception e) {
            Log.e(Resources.getSystem().getString(R.string.log_tag),
                    "Failed to generate the result of a search-request.", e);
            throw e;
        }
    }


    private <T extends AResponse> void query(final IResponse<T> response,
                                             final Class<T> resultClass,
                                             final String reqMethod,
                                             final String urlParams,
                                             final String data) throws Exception {

        if (cookie == null || cookie.isEmpty())
            throw new Exception("Die Optionen wurden noch nicht gesetzt.");

        new Thread(new Runnable() {
            @Override
            public void run() {
                T result = null;
                HttpsURLConnection connection = null;
                OutputStreamWriter request = null;
                StringBuilder sb = new StringBuilder();

                try {
                    String fullUrl = "https://" + DEFAULT_HOST + "/" + (urlParams == null ? "" : urlParams);
                    Log.d(act.getString(R.string.log_tag), "Send query to '" + fullUrl + "'...");

                    connection = (HttpsURLConnection) new URL(fullUrl).openConnection();
                    connection.setRequestProperty("Cookie", cookie);
                    connection.setRequestProperty("Host", DEFAULT_HOST);
                    connection.setRequestProperty("User-Agent", "okhttp/2.2.0");
                    connection.setRequestProperty("Content-Type", "application/json");

                    if (data != null && !data.isEmpty() ) {
                        connection.setRequestProperty("Content-Length",  String.valueOf(data.length()));
                    }

                    connection.setRequestMethod(reqMethod);


                    if (data != null && !data.isEmpty()) {
                        request = new OutputStreamWriter(connection.getOutputStream());
                        request.write(data);
                        request.flush();
                        request.close();
                    }

                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    Log.d(act.getString(R.string.log_tag), "Got result:");
                    Log.d(act.getString(R.string.log_tag), sb.toString());
                } catch (Exception e) {
                    Log.e(act.getString(R.string.log_tag), "Failed to send a query.", e);
                } finally {
                    try {
                        if (request != null)
                            request.close();
                    } catch (Exception e) {
                        Log.w(act.getString(R.string.log_tag), "Failed to close the request.", e);
                    }

                    try {
                        if (connection != null)
                            connection.disconnect();
                    } catch (Exception e) {
                        Log.w(act.getString(R.string.log_tag), "Failed to close the connection.", e);
                    }
                }

                try {
                    Log.d(act.getString(R.string.log_tag),
                            "Generate result class from json-result...");

                    result = GSON.fromJson(sb.toString(), resultClass);

                    Log.d(act.getString(R.string.log_tag),
                            "Result successfully generated.");
                } catch (Exception e) {
                    Log.e(act.getString(R.string.log_tag),
                            "Failed to generate result class from json-result.", e);
                    throw e;
                }

                response.doResponse((T) result);
            }
        }).start();
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    public interface IResponse<T extends AResponse> {

        public abstract void doResponse(T result);

    }
}