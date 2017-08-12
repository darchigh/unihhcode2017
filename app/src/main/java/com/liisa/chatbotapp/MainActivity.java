package com.liisa.chatbotapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by liisa_000 on 09/04/2017.
 */

public class MainActivity extends AppCompatActivity{

    Button comeToChatView;
    ArrayList<Message> myListItems  = new ArrayList<>();
    ImageButton statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        comeToChatView = (Button) findViewById(R.id.button);
        statistics = (ImageButton) findViewById(R.id.statistics);
        myListItems.add(new Message("a","a"));
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.activity_listview,myListItems);
        final ListView listOfMessages = (ListView)findViewById(R.id.list_view_messages);
        listOfMessages.setAdapter(adapter);

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        comeToChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ChatActivity.class);
                //TODO: ADD SELected USER PARameters
                //  myIntent.putExtra("key", value);
                MainActivity.this.startActivity(myIntent);
            }
        });

        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                String main = listOfMessages.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),main.toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("message", main);
                startActivity(intent);
            }
        });
    }
}





