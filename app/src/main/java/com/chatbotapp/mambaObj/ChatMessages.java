package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

import java.util.ArrayList;

/**
 * Created by aossa on 09.04.2017.
 */

public class ChatMessages extends AResponse {


    private ChatMessage[] messages;

    private Recipient recipient;


    public ChatMessage[] getMessages() {
        return messages;
    }

    public void setMessages(ChatMessage[] messages) {
        this.messages = messages;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }
}