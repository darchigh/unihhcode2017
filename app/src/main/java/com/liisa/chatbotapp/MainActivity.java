package com.liisa.chatbotapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.SearchResult;
import com.chatbotapp.mambaObj.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String LOG_TAG = "bla";
    private Context mContext;
    private String workspace_id;
    private Recycler mAdapter;
    private String conversation_username;
    private String conversation_password;
    private EditText editMessage;
    private ArrayList messageArrayList;
    private ImageButton btnSend;
    private Map<String, Object> context = new HashMap<>();
    private BMSClient bmsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        conversation_username = mContext.getString(R.string.conversation_username);
        conversation_password = mContext.getString(R.string.conversation_password);
        workspace_id = mContext.getString(R.string.workspace_id);
        bmsClient = BMSClient.getInstance();


        editMessage = (EditText) findViewById(R.id.messageInput);
        btnSend = (ImageButton) findViewById(R.id.sendMessageButton);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();
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
        Message imessage = new Message();
        imessage.setMessage(inputMessage);
        imessage.setId("1");
        messageArrayList.add(imessage);
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
                    service.setUsernameAndPassword(conversation_username, conversation_password);
                    MessageRequest newMessage = new MessageRequest.Builder().inputText(inputMessage).context(context).build();
                    MessageResponse response = service.message(workspace_id, newMessage).execute();
                    Log.d(LOG_TAG, "Item created successfully");

                    if (response.getContext() != null) {
                        context.clear();
                        context = response.getContext();

                    }
                    Message outMessage = new Message();
                    if (response != null) {
                        if (response.getOutput() != null && response.getOutput().containsKey("text")) {

                            ArrayList responseList = (ArrayList) response.getOutput().get("text");
                            if (null != responseList && responseList.size() > 0) {
                                outMessage.setMessage((String) responseList.get(0));
                                outMessage.setId("2");
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
}