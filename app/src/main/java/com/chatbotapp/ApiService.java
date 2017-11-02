package com.chatbotapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.liisa.chatbotapp.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Background Service Thread that holds WebApi so it does not loose cookies when changing Activites.
 */
public class ApiService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private MambaWebApi api;
    private static final String CONTEXT_FILE_NAME = "chatbotapp.watsoncontexts";
    private String conversation_username;
    private String conversation_password;
    private String workspace_id;
    private BMSClient bmsClient;
    private ConversationService conversationService;
    private Map<Integer, Map<String, Object>> userConversationMap;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ApiService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ApiService.this;
        }
    }

    public ApiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public MambaWebApi getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadContextState();
        conversation_username = getString(R.string.conversation_username);
        conversation_password = getString(R.string.conversation_password);
        workspace_id = getString(R.string.workspace_id);
        api = new MambaWebApi();
        conversationService = new ConversationService(ConversationService.VERSION_DATE_2017_02_03);
        conversationService.setUsernameAndPassword(conversation_username, conversation_password);
        conversationService.setEndPoint("https://gateway-fra.watsonplatform.net/conversation/api");
        bmsClient = BMSClient.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveContextState();
    }

    /**
     * Send a message to watson workspace.
     *
     * @param inputMessage input text watson should react to
     * @param userId needed for mapping conversation state to user
     * @param callback callback handle for activity
     */
    public void sendMessageToWatson(String inputMessage, final int userId, final ServiceCallback<MessageResponse> callback) {
        MessageRequest.Builder b = new MessageRequest.Builder().inputText(inputMessage);
        if (userConversationMap.containsKey(userId)) {
            b.context(userConversationMap.get(userId));
        }
        MessageRequest newMessage = b.build();

        conversationService.message(workspace_id, newMessage).enqueue(new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(MessageResponse response) {
                userConversationMap.put(userId, response.getContext());
                callback.onResponse(response);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    /**
     * Save conversation context state to disk to continue conversation later
     */
    private void saveContextState() {
        if (userConversationMap.size() == 0) {
            return;
        }
        String file = "";
        FileOutputStream outputStream;
        try {
            file = new ObjectMapper().writeValueAsString(userConversationMap);
            outputStream = openFileOutput(CONTEXT_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(file.getBytes());
            outputStream.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Context Information from disk, used to continue conversation state with watson
     */
    private void loadContextState() {
        FileInputStream inputStream;
        try {
            inputStream = openFileInput(CONTEXT_FILE_NAME);
            userConversationMap = new ObjectMapper().readValue(inputStream, new TypeReference<Map<Integer, Map<String, Object>>>() {
            });
        } catch (FileNotFoundException e) {
            Log.d("com.liisa.chatbotapp", "no conversation state found");
            userConversationMap = new HashMap<>();
        } catch (IOException e) {
            userConversationMap = new HashMap<>();
            e.printStackTrace();
        }
    }
}
