package com.chatbotapp.springtest;

import android.os.AsyncTask;
import android.util.Log;

import com.chatbotapp.mambaObj.Login;
import com.chatbotapp.mambaObj.Logon;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by U550264 on 12.08.2017.
 */

public class LoginTask extends AsyncTask<Void, Void, Logon> {
    String email = "nathalie.degtjanikov@gmail.com";
    String password = "Schokobanane123";

//    private static final String DEFAULT_HOST = "api.mobile-api.ru";
//            try {n
//        result = query(response, Logon.class, CREATE_REQ, "", null, data);

    @Override
    protected Logon doInBackground(Void... params) {
        ObjectMapper om = new ObjectMapper();
        try {
            final String url = "https://api.mobile-api.ru/v5.2.38.0/login/builder/?langId=de&dateType=timestamp";

            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter mc = new MappingJackson2HttpMessageConverter();
            mc.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
            restTemplate.getMessageConverters().add(mc);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Host", "api.mobile-api.ru");
            headers.set("User-Agent", "okhttp/2.2.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            String content = om.writeValueAsString(new Login(email, password));
            headers.set("Content-Length", "" + content.length());

            HttpEntity<String> entity = new HttpEntity<String>(content, headers);
            ResponseEntity<Logon> l = restTemplate.postForEntity(url, entity, Logon.class);
            if (l != null) {
                return l.getBody();
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }
}


//
//    public Thread logon(final String email, final String password, final IResponse<Logon> response) throws Exception {
//        Thread result;
//
//        try {
//            String data = "";
//            data += "{";
//            data += "   \"login\":{";
//            data += "      \"login\":\"" + email + "\",";
//            data += "      \"password\":\"" + password + "\"";
//            data += "  }";
//            data += "}";
//
//            result = query(response, Logon.class, CREATE_REQ, "v5.2.38.0/login/builder/", null, data);
//        } catch (Exception e) {
//            log.l(ILog.LogLevel.error, "Failed to generate the result of a logon-request.", e);
//            throw e;
//        }
//
//        return result;
////    }
//private <T extends AResponse> Thread query(final IResponse<T> response,
//                                           final Class<T> resultClass,
//                                           final String reqMethod,
//                                           final String url,
//                                           final String urlParams,
//                                           final String data) throws Exception {
//    final Thread queryResult = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            T result;
//            HttpsURLConnection connection = null;
//            OutputStreamWriter request = null;
//            StringBuilder sb = new StringBuilder();
//
//            try {
//                String fullUrl = "https://" + DEFAULT_HOST + "/" + url + (urlParams == null ? "?" : urlParams + "&") + DEFAULT_PARAMETER;
//                log.l(ILog.LogLevel.debug, "Send query to '" + fullUrl + "'...", null);
//
//                connection = (HttpsURLConnection) new URL(fullUrl).openConnection();
//                connection.setRequestMethod(reqMethod);
//
//                if (cookie.size() > 0) {
//                    StringBuilder cookieStr = new StringBuilder();
//
//                    for (Map.Entry<String, String> cookieVal : cookie.entrySet()) {
//                        cookieStr.append(cookieVal.getKey()).append("=").append(cookieVal.getValue()).append("; ");
//                    }
//
//                    connection.setRequestProperty("Cookie", cookieStr.toString());
//                }
//
//                connection.setRequestProperty("Host", DEFAULT_HOST);
//                connection.setRequestProperty("User-Agent", "okhttp/2.2.0");
//                connection.setRequestProperty("Content-Type", "application/json");
//
//                if (data != null && !data.isEmpty()) {
//                    connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
//
//                    connection.setDoOutput(true);
//                    request = new OutputStreamWriter(connection.getOutputStream());
//                    request.write(data);
//                    request.flush();
//                    request.close();
//                }
//
//                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
//                BufferedReader reader = new BufferedReader(isr);
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                makeCookie(connection.getHeaderFields().get("Set-Cookie"));
//
//                log.l(ILog.LogLevel.debug, "Got result:", null);
//                log.l(ILog.LogLevel.debug, sb.toString(), null);
//            } catch (Exception e) {
//                log.l(ILog.LogLevel.error, "Failed to send a query.", e);
//            } finally {
//                try {
//                    if (request != null)
//                        request.close();
//                } catch (Exception e) {
//                    log.l(ILog.LogLevel.warn, "Failed to close the request.", e);
//                }
//
//                try {
//                    if (connection != null)
//                        connection.disconnect();
//                } catch (Exception e) {
//                    log.l(ILog.LogLevel.warn, "Failed to close the connection.", e);
//                }
//            }
//
//            try {
//                log.l(ILog.LogLevel.debug, "Generate result class from json-result...", null);
//
//                result = AResponse.GSON.fromJson(sb.toString(), resultClass);
//
//                log.l(ILog.LogLevel.debug, "Result successfully generated.", null);
//            } catch (Exception e) {
//                log.l(ILog.LogLevel.error, "Failed to generate result class from json-result.", null);
//                throw e;
//            }
//
//            response.doResponse(result);
//        }
//    });
//
//    queryResult.run();
//
//    return queryResult;
//}