package com.liisa.chatbotapp;

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
    //String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
    //       "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        comeToChatView = (Button) findViewById(R.id.button);
        myListItems.add(new Message("a","a"));
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.activity_listview,myListItems);
        final ListView listOfMessages = (ListView)findViewById(R.id.list_view_messages);
        listOfMessages.setAdapter(adapter);



        comeToChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ChatActivity.class);
                //TODO: ADD SELected USER PARameters
              //  myIntent.putExtra("key", value);
                MainActivity.this.startActivity(myIntent);            }
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
//
//        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?>adapter,View v, int position){
//                ItemClicked item = adapter.getItemAtPosition(position);
//
//                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
//                //based on item add info to intent
//                startActivity(intent);
//            }
//        });
//
//        public ItemClicked getItem(int position){
//            return myListItems.get(position);
//        }





