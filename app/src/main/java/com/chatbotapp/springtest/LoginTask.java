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