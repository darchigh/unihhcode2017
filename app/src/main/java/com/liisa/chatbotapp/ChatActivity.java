package com.liisa.chatbotapp;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chatbotapp.ApiActivity;
import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.ChatAcknowledge;
import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Logon;
import com.chatbotapp.mambaObj.User;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatActivity extends ApiActivity {
    private RecyclerView recyclerView;
    private Recycler mAdapter;
    private EditText editMessage;
    private ArrayList<ChatMessage> messageArrayList;
    private ImageButton btnSend;
    private Button watsonButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        if (getIntent().hasExtra("user"))
            user = (User) getIntent().getSerializableExtra("user");

        editMessage = (EditText) findViewById(R.id.messageInput);
        btnSend = (ImageButton) findViewById(R.id.sendMessageButton);
        watsonButton = (Button) findViewById(R.id.watsonAnswerButton);
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

        watsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button should only be enabled when last msg is not ours -> set after init contacts
                ChatMessage lastMsg = messageArrayList.get(0);
                letWatsonAnswer(lastMsg.getMessage(), user.getUserId());
                //then we send the last msg from the contact to watson, with user id. apiservice can continue context if exists
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputMessage = editMessage.getText().toString().trim();
                sendMessageToMamba(user.getUserId(), inputMessage);
                editMessage.setText("");
            }
        });

    }

    private void sendMessageToMamba(int userId, String input) {
        try {
            apiService.getApi().sendMessage(userId, input, new MambaWebApi.IResponse<ChatAcknowledge>() {
                @Override
                public void doResponse(ChatAcknowledge result) {
                    refreshMessages();
                    //just refreshMessages messages. it should be there if it posted OK
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error sending msg to mamba", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * See watson doc https://www.ibm.com/watson/developercloud/conversation/api/v1/#send_message
     */
    private void letWatsonAnswer(String input, final int userId) {
        watsonButton.setEnabled(false);
        apiService.sendMessageToWatson(input, userId, new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(MessageResponse response) {
                if (response != null && response.getOutput() != null && response.getOutput().containsKey("text")) {
                    ArrayList<String> responseList = (ArrayList) response.getOutput().get("text");
                    String responseText = responseList.get(0);
                    sendMessageToMamba(userId, responseText);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Error sending msg to watson", Toast.LENGTH_SHORT).show();
            }
        });
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
                        refreshMessages();
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

    private void refreshMessages() {
        try {
            apiService.getApi().getChat(user.getId(), new MambaWebApi.IResponse<ChatMessages>() {
                @Override
                public void doResponse(ChatMessages result) {
                    messageArrayList.clear();
                    for (ChatMessage msg : result.getMessages()) {
                        messageArrayList.add(msg);
                    }
                    ChatMessage lastMessage = result.getMessages()[0];
                    //watson can only answer chat messages. Chat will always have at least 1 msg. Last msg is index 0
                    watsonButton.setEnabled(lastMessage.isIncoming());
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getItemCount() > 1) {
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}