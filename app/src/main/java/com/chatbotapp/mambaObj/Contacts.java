package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by Alex on 14.05.2017.
 */

public class Contacts extends AResponse {

    private Contact[] contacts;

    public Contact[] getContacts() {
        return contacts;
    }

    public void setContacts(Contact[] contacts) {
        this.contacts = contacts;
    }
}
