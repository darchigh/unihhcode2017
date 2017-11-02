package com.liisa.chatbotapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.User;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by daeva on 02.11.2017.
 */

public class WatsonActivity extends ApiActivity {
    private ImageButton btnSend;
    private Button watsonButton;
    private RecyclerView recyclerView;
    private Recycler mAdapter;
    private EditText editMessage;
    private ArrayList<ChatMessage> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        editMessage = (EditText) findViewById(R.id.messageInput);
        editMessage.setHint("Chat with Watson!");
        btnSend = (ImageButton) findViewById(R.id.sendMessageButton);
        watsonButton = (Button) findViewById(R.id.watsonAnswerButton);
        watsonButton.setEnabled(false);
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
                final String inputMessage = editMessage.getText().toString().trim();
                ChatMessage yourMsg = new ChatMessage();
                yourMsg.setCreated(Long.valueOf(System.currentTimeMillis()).intValue());
                yourMsg.setMessage(inputMessage);
                yourMsg.setIncoming(false);
                messageArrayList.add(yourMsg);
                editMessage.setText("");
                mAdapter.notifyDataSetChanged();
                sendMessageToWatson(inputMessage);
            }
        });
    }


    @Override
    protected void onApiAvailable() {

    }

    @Override
    protected void onApiUnavailable() {

    }

    private void sendMessageToWatson(String inputMessage) {
        apiService.sendMessageToWatson(inputMessage, 0, new ServiceCallback<MessageResponse>() {
            @Override
            public void onResponse(final MessageResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null && response.getOutput() != null && response.getOutput().containsKey("text")) {
                            ArrayList<String> responseList = (ArrayList) response.getOutput().get("text");
                            String responseText = responseList.get(0);
                            ChatMessage msg = new ChatMessage();
                            msg.setIncoming(true);
                            msg.setMessage(responseText);
                            msg.setCreated(Long.valueOf(System.currentTimeMillis()).intValue());
                            messageArrayList.add(msg);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("liisa.chatbotapp", "run: watson error: " + e.getMessage());
                        Toast.makeText(WatsonActivity.this, "Error sending msg to watson", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
