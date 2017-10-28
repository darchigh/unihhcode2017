package com.chatbotapp.mambaObj;

import android.app.ApplicationErrorReport;

import com.chatbotapp.AResponse;

import java.util.Date;

/**
 * Created by aossa on 09.04.2017.
 */

public class ChatMessage extends AResponse {

    private int id;
    private int created;
    private boolean incoming;
    private boolean unread;
    private String type;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return (incoming ? "--> " : "<-- ") + getMessage();

    }
}
