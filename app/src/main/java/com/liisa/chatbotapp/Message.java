package com.liisa.chatbotapp;

/**
 * Created by liisa_000 on 08/04/2017.
 */
import java.io.Serializable;

public class Message implements Serializable {
    String id, message;
    public Message() {
    }

    public Message(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
