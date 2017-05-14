package com.liisa.chatbotapp;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.ChatAcknowledge;
import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;
import com.chatbotapp.mambaObj.SearchResult;
import com.chatbotapp.mambaObj.User;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private final static MambaWebApi api = new MambaWebApi(new MambaWebApi.ILog() {
        @Override
        public void l(LogLevel level, String message, Exception e) {
            System.out.println(level.toString() + ": " + message + (e == null ? "" : "; Exception: " + e.toString()));
        }
    });

    static {
        String email = "nathalie.degtjanikov@gmail.com";
        String password = "Schokobanane123";

        try {
            api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon result) {
                    System.out.println("Authentication:");
                    System.out.println("   Authenticated: " + result.isSuccessful());
                    System.out.println("   User: " + result.getProfile().toString());
                }
            }).join();
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
        }).join();
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
        }).join();
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
        }).join();
    }
}