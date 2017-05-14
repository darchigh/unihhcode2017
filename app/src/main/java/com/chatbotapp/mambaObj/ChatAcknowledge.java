package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by Alex on 14.05.2017.
 */

public class ChatAcknowledge extends AResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
