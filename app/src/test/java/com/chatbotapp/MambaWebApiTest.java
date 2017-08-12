package com.chatbotapp;

import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;
import com.chatbotapp.mambaObj.SearchResult;
import com.chatbotapp.mambaObj.User;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aossa on 12.08.2017.
 */
public class MambaWebApiTest {

    private static MambaWebApi api;

    @BeforeClass
    public static void init() throws Exception {
        String email = "nathalie.degtjanikov@gmail.com";
        String password = "Schokobanane123";

        try {
            api = new MambaWebApi();


            api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon result) {
                    System.out.println("Authentication:");
                    System.out.println("   Authenticated: " + result.isSuccessful());
                    System.out.println("   User: " + result.getProfile().toJSON());
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void search() throws Exception {
        api.search("", "", 50, 31, new MambaWebApi.IResponse<SearchResult>() {
            @Override
            public void doResponse(SearchResult result) {
                for (User user : result.getUsers()) {
                    System.out.println(user.getUserId() + " " + user.getName());
                }
            }
        }).get();
    }

    @Test
    public void getAllContacts() throws Exception {
        api.getContacts(new MambaWebApi.IResponse<Contacts>() {
            @Override
            public void doResponse(Contacts result) {
                for (Contact contact : result.getContacts()) {
                    System.out.println("User:");
                    System.out.println("   " + contact.getAnketa().getUserId() + " " + contact.getAnketa().getName());
                    System.out.println("   " + contact.getLastNessage().getMessage());
                }


            }
        }).get();
    }

    @Test
    public void sendMessage() throws Exception {
       /* api.sendMessage("1392384820", "Hi, wie gehts?", new MambaWebApi.IResponse<ChatAcknowledge>() {
            @Override
            public void doResponse(ChatAcknowledge result) {

            }
        }).join();
*/

    }


    @Test
    public void getChat() throws Exception {
        api.getChat("1392384820", new MambaWebApi.IResponse<ChatMessages>() {
            @Override
            public void doResponse(ChatMessages result) {
                System.out.println("Chat mit " + result.getRecipient().getAnketa().getName());

                for (ChatMessage message : result.getMessages()) {
                    System.out.println((message.isIncoming() ? "-->" : "<--") + " " + message.getMessage());
                }
            }
        }).get();
    }

}