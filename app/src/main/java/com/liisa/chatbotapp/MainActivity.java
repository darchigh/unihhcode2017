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
import android.widget.Toast;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.SearchResult;
import com.chatbotapp.mambaObj.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private String workspace_id;
    private String conversation_username;
    private String conversation_password;
    private EditText inputMessage;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        conversation_username = mContext.getString(R.string.conversation_username);
        conversation_password = mContext.getString(R.string.conversation_password);
        workspace_id = mContext.getString(R.string.workspace_id);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (ImageButton) findViewById(R.id.sendButton);
    }

    private void sendMessage() {
        final String inputmessage = this.inputMessage.getText().toString().trim();
        Message inputMessage = new Message();

    }
}

