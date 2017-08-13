package com.liisa.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.ApiActivity;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;

import java.util.ArrayList;

/**
 * Created by liisa_000 on 09/04/2017.
 */

public class MainActivity extends ApiActivity {
    ArrayList<Contact> myListItems = new ArrayList<>();
    ImageButton statistics;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
      //  comeToChatView = (Button) findViewById(R.id.button);
        statistics = (ImageButton) findViewById(R.id.statistics);
        adapter = new ArrayAdapter(this, R.layout.listview, myListItems);
        final ListView listOfMessages = (ListView) findViewById(R.id.list_view_messages);
        listOfMessages.setAdapter(adapter);

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StatisticsActivity.class);
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

    @Override
    protected void onServiceConnect() {
        try {
            String email = "nathalie.degtjanikov@gmail.com";
            String password = "Schokobanane123";

            apiService.getApi().logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon logon) {
                    Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                    if (logon.isSuccessful()) {
                        try {
                            apiService.getApi().getContacts(new MambaWebApi.IResponse<Contacts>() {
                                @Override
                                public void doResponse(Contacts contacts) {
                                    adapter.clear();

                                    for(Contact contact: contacts.getContacts()) {
                                        Log.i("MambaWebApi", "Add contact: " + contact.toJSON());
                                        adapter.add(contact);
                                    }


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
    protected void onServiceDisconnect() {

    }
}
