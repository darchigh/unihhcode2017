package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by aossa on 08.04.2017.
 */

public class OnlineStatus extends AResponse {

    private boolean online;

    private String status;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
