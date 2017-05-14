package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by Alex on 14.05.2017.
 */

public class Logon extends AResponse {

    private boolean isAuth;

    private User profile;


    public boolean isSuccessful() {
        return isAuth;
    }

    public void setSuccessful(boolean successful) {
        this.isAuth = successful;
    }


    public User getProfile() {
        return profile;
    }

    public void setProfile(User profile) {
        this.profile = profile;
    }
}
