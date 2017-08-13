package com.chatbotapp.mambaObj;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by U550264 on 12.08.2017.
 */

public class Login {
    Map<String, String> login = new TreeMap<>();

    public Login(String login, String password) {
        this.login.put("login", login);
        this.login.put("password", password);
    }
}
