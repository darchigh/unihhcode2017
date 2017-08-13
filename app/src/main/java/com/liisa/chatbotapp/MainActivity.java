package com.liisa.chatbotapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.chatbotapp.api.ApiActivity;
import com.chatbotapp.api.ApiService;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;

import java.util.ArrayList;

/**
 * Created by liisa_000 on 09/04/2017.
 */

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity {

   // Button comeToChatView;
=======
public class MainActivity extends ApiActivity {
    Button comeToChatView;
>>>>>>> working changes from yesterday, may not work
    ArrayList<Contact> myListItems = new ArrayList<>();
    ImageButton statistics;
    String email = "nathalie.degtjanikov@gmail.com";
    String password = "Schokobanane123";
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

    @Override
    protected void onStart() {
        super.onStart();

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
<<<<<<< HEAD

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


//        comeToChatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(MainActivity.this, ChatActivity.class);
//                //TODO: ADD SELected USER PARameters
//                //  myIntent.putExtra("key", value);
//                MainActivity.this.startActivity(myIntent);
//            }
//        });

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
=======
>>>>>>> working changes from yesterday, may not work
    }
}





