package com.liisa.chatbotapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Logon;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by liisa_000 on 09/04/2017.
 */

public class MainActivity extends AppCompatActivity {

    Button comeToChatView;
    ArrayList<Contact> myListItems = new ArrayList<>();
    ImageButton statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        comeToChatView = (Button) findViewById(R.id.button);
        statistics = (ImageButton) findViewById(R.id.statistics);
        myListItems.add(new Contact());
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview, myListItems);
        final ListView listOfMessages = (ListView) findViewById(R.id.list_view_messages);
        listOfMessages.setAdapter(adapter);


        MambaWebApi api = new MambaWebApi();

        try {
            String email = "nathalie.degtjanikov@gmail.com";
            String password = "Schokobanane123";

            api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon result) {


                }
            });
        } catch (Exception e) {

        }


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
                Toast.makeText(getApplicationContext(), main.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("message", main);
                startActivity(intent);
            }
        });
    }
}





