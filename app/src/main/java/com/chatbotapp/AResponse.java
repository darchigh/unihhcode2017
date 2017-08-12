package com.chatbotapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by aossa on 08.04.2017.
 */

public abstract class AResponse {


    public static Gson GSON = new GsonBuilder().create();

    public String toJSON() {
        return GSON.toJson(this);
    }
}
