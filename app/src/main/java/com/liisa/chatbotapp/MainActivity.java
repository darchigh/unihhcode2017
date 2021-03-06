package com.liisa.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.ApiActivity;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by liisa_000 on 09/04/2017.
 */

public class MainActivity extends ApiActivity {
    ArrayList<Contact> myListItems = new ArrayList<>();
    ImageButton statistics;
    ImageButton watson;
    Button refreshButton;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //  comeToChatView = (Button) findViewById(R.id.button);
        statistics = (ImageButton) findViewById(R.id.statistics);
        watson = (ImageButton) findViewById(R.id.watson);
        refreshButton = (Button) findViewById(R.id.refreshButton);
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

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContacts();
            }
        });

        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                Contact contact = (Contact) listOfMessages.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), contact.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("user", contact.getAnketa());
                startActivity(intent);
            }
        });
        watson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, WatsonActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onApiAvailable() {
        getContacts();
    }

    @Override
    protected void onApiUnavailable() {

    }


    private void getContacts() {
        try {
            adapter.clear();

            String email = "nathalie.degtjanikov@gmail.com";
            String password = "Schokobanane123";

            apiService.getApi().logon(email, password, new MambaWebApi.IResponse<Logon>() {
                @Override
                public void doResponse(Logon logon) {
                    try {
                        Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                        if (logon.isSuccessful()) {
                            apiService.getApi().getContacts(new MambaWebApi.IResponse<Contacts>() {
                                @Override
                                public void doResponse(Contacts contacts) {
                                    // Absteigende Sortierung --> D.h. neue Einträge werden als erstes angezeigt.
                                    Arrays.sort(contacts.getContacts(), new Comparator<Contact>() {
                                        @Override
                                        public int compare(Contact o1, Contact o2) {
                                            return o2.getLastMessageTimestamp() - o1.getLastMessageTimestamp();
                                        }
                                    });

                                    for (Contact contact : contacts.getContacts()) {
                                        Log.i("MambaWebApi", "Add contact: " + contact.toJSON());
                                        adapter.add(contact);
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }
}
