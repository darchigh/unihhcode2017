package com.liisa.chatbotapp;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chatbotapp.ApiActivity;
import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;
import com.chatbotapp.mambaObj.User;
import com.chatbotapp.mambaObj.ChatMessage;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends ApiActivity {

    private RecyclerView recyclerView;
    private Context mContext;
    private String workspace_id;
    private Recycler mAdapter;
    private String conversation_username;
    private String conversation_password;
    private EditText editMessage;
    private ArrayList<ChatMessage> messageArrayList;
    private ImageButton btnSend;
    private Map<String, Object> context = new HashMap<>();
    private BMSClient bmsClient;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        if (getIntent().hasExtra("user"))
            user = (User) getIntent().getSerializableExtra("user");

        mContext = getApplicationContext();
        conversation_username = mContext.getString(R.string.conversation_username);
        conversation_password = mContext.getString(R.string.conversation_password);
        workspace_id = mContext.getString(R.string.workspace_id);
        bmsClient = BMSClient.getInstance();


        editMessage = (EditText) findViewById(R.id.messageInput);
        btnSend = (ImageButton) findViewById(R.id.sendMessageButton);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<ChatMessage>() {

            @Override
            public boolean add(ChatMessage o) {
                boolean result = super.add(o);

                // Automatisches sortieren der Liste beim Einfügen von neuen Objekten.
                Collections.sort(this, new Comparator<ChatMessage>() {
                    @Override
                    public int compare(ChatMessage o1, ChatMessage o2) {
                        return o1.getCreated() - o2.getCreated();
                    }
                });

                return result;
            }
        };
        mAdapter = new Recycler(messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                editMessage.setText("");
            }
        });

    }

    private void sendMessage() {
        final String inputMessage = this.editMessage.getText().toString().trim();
        ChatMessage imessage = new ChatMessage();
        imessage.setMessage(inputMessage);
        imessage.setId(1);
        messageArrayList.add(imessage);
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
                    service.setUsernameAndPassword(conversation_username, conversation_password);
                    MessageRequest newMessage = new MessageRequest.Builder().inputText(inputMessage).context(context).build();
                    MessageResponse response = service.message(workspace_id, newMessage).execute();
                    //Thread.sleep(10000);
                    if (response.getContext() != null) {
                        context.clear();
                        context = response.getContext();
                    }

                    ChatMessage outMessage = new ChatMessage();
                    if (response != null) {
                        if (response.getOutput() != null && response.getOutput().containsKey("text")) {

                            ArrayList responseList = (ArrayList) response.getOutput().get("text");
                            if (null != responseList && responseList.size() > 0) {
                                outMessage.setMessage((String) responseList.get(0));
                                outMessage.setId(2);
                            }
                            messageArrayList.add(outMessage);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);

                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    @Override
    protected void onApiAvailable() {
        try {
            String email = "nathalie.degtjanikov@gmail.com";
            String password = "Schokobanane123";

            apiService.getApi().logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon logon) {
                    Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                    if (logon.isSuccessful()) {
                        try {
                            apiService.getApi().getChat(user.getId(), new MambaWebApi.IResponse<ChatMessages>() {
                                @Override
                                public void doResponse(ChatMessages result) {
                                    for (ChatMessage msg : result.getMessages()) {
                                        messageArrayList.add(msg);
                                    }

                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onApiUnavailable() {

    }

}