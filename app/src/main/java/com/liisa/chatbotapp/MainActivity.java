package com.liisa.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by liisa_000 on 09/04/2017.
 */

public class MainActivity extends AppCompatActivity{

    Button comeToChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        comeToChatView = (Button) findViewById(R.id.button);

        comeToChatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ChatActivity.class);
                //TODO: ADD SELected USER PARameters
              //  myIntent.putExtra("key", value);
                MainActivity.this.startActivity(myIntent);            }
        });

    }
}


