package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by Alex on 14.05.2017.
 */

public class Contact extends AResponse {

    private int contactId; // Contact ID is not UserId!
    private int folderId;

    private int unread;
    private int messages;

    private boolean stickersSupport;

    private User anketa;

    private int lastMessageTimestamp;

    private ChatMessage lastMessage;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public boolean isStickersSupport() {
        return stickersSupport;
    }

    public void setStickersSupport(boolean stickersSupport) {
        this.stickersSupport = stickersSupport;
    }

    public User getAnketa() {
        return anketa;
    }

    public void setAnketa(User anketa) {
        this.anketa = anketa;
    }

    public int getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(int lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public ChatMessage getLastNessage() {
        return lastMessage;
    }

    public void setLastNessage(ChatMessage lastNessage) {
        this.lastMessage = lastNessage;
    }

    @Override
    public String toString() {
        String name = "unknown";
        if (anketa != null) {
            name = anketa.getName();
        }
        String lastmsg= "no msg";
        if (getLastNessage() != null) {
            if (!getLastNessage().isIncoming()) {
                lastmsg = "Du: ";
            } else {
                lastmsg = "";
            }
            lastmsg += mask(getLastNessage().getMessage());
        }

        return name + " - " +lastmsg;
    }
}

