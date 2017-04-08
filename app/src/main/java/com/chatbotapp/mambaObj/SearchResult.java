package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by aossa on 08.04.2017.
 */

public class SearchResult extends AResponse {


    private User[] users;

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
