package com.liisa.chatbotapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private String workspace_id;
    private String conversation_username;
    private String conversation_password;
    private EditText editMessage;
    private ArrayList messageArrayList;
    private ImageButton btnSend;
    private Map<String, Object> context = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        conversation_username = mContext.getString(R.string.conversation_username);
        conversation_password = mContext.getString(R.string.conversation_password);
        workspace_id = mContext.getString(R.string.workspace_id);

        editMessage = (EditText) findViewById(R.id.message);
        btnSend = (ImageButton) findViewById(R.id.sendButton);
        messageArrayList = new ArrayList<>();
    }

    private void sendMessage() {
        final String inputMessage = this.editMessage.getText().toString().trim();
        Message imessage = new Message();
        imessage.setMessage(inputMessage);
        imessage.setId("1");
        messageArrayList.add(imessage);

        ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
        service.setUsernameAndPassword(conversation_username, conversation_password);
        MessageRequest newMessage = new MessageRequest.Builder().inputText(inputMessage).context(context).build();
        MessageResponse response = service.message(workspace_id, newMessage).execute();

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

        }
    }
}