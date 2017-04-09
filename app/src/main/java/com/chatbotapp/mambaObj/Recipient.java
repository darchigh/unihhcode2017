package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by aossa on 09.04.2017.
 */

public class Recipient extends AResponse {

    private int unread;

    private boolean stickersSupport;

    private int lastMessageTimestamp;

    private User anketa;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public boolean isStickersSupport() {
        return stickersSupport;
    }

    public void setStickersSupport(boolean stickersSupport) {
        this.stickersSupport = stickersSupport;
    }

    public int getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(int lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public User getAnketa() {
        return anketa;
    }

    public void setAnketa(User anketa) {
        this.anketa = anketa;
    }
}
