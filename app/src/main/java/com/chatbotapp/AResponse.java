package com.chatbotapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aossa on 08.04.2017.
 */

public abstract class AResponse implements Serializable {


    public static Gson GSON = new GsonBuilder().create();

    public String toJSON() {
        return GSON.toJson(this);
    }


    protected String mask(String in) {
        return in
                .replace("&auml;", "ä")
                .replace("&Auml;", "Ä")
                .replace("&ouml;", "ö")
                .replace("&Ouml;", "Ö")
                .replace("&uuml;", "ü")
                .replace("&Uuml;", "Ü")
                .replace("&szlig;", "ß");
    }
}
