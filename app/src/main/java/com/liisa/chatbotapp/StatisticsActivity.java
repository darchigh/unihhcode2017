package com.liisa.chatbotapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.chatbotapp.ApiActivity;
import com.chatbotapp.BarchartWrapper;
import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.ChatMessage;
import com.chatbotapp.mambaObj.ChatMessages;
import com.chatbotapp.mambaObj.Contact;
import com.chatbotapp.mambaObj.Contacts;
import com.chatbotapp.mambaObj.Logon;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liisa_000 on 14/05/2017.
 */

public class StatisticsActivity extends ApiActivity {

    private final static int DEFAULT_BAR_COLOR_1 = Color.BLUE;
    private final static int DEFAULT_BAR_COLOR_2 = Color.RED;
    private final static int DEFAULT_BAR_COLOR_3 = Color.GREEN;
    private final static int DEFAULT_BAR_COLOR_4 = Color.YELLOW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        // Der onClickListener sollte allen Buttons zugewiesen werden, wobei dann die
        // makeBarchart-Methode erweitert werden sollte.
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final BarchartWrapper result = new BarchartWrapper();

                try {
                    final String barInName = "Nachrichten Eingehend";
                    final String barOutName = "Nachrichten Ausgehend";

                    result.addBarset(barInName, DEFAULT_BAR_COLOR_1);
                    result.addBarset(barOutName, DEFAULT_BAR_COLOR_2);

                    final MambaWebApi api = apiService.getApi();
                    final String email = "nathalie.degtjanikov@gmail.com";
                    final String password = "Schokobanane123";

                    api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                        @Override
                        public void doResponse(Logon logon) {
                            try {
                                Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                                if (logon.isSuccessful()) {
                                    api.getContacts(new MambaWebApi.IResponse<Contacts>() {
                                        @Override
                                        public void doResponse(Contacts contacts) {
                                            final CountDownLatch c = new CountDownLatch(contacts.getContacts().length);

                                            try {
                                                for (final Contact contact : contacts.getContacts()) {
                                                    api.getChat(contact.getAnketa().getUserId(), new MambaWebApi.IResponse<ChatMessages>() {
                                                        @Override
                                                        public void doResponse(ChatMessages chatMessage) {
                                                            result.addXAxisCaption(contact.getAnketa().getName());
                                                            int income = 0;
                                                            int outcome = 0;

                                                            for (ChatMessage message : chatMessage.getMessages()) {
                                                                if (message.isIncoming())
                                                                    income++;
                                                                else
                                                                    outcome++;
                                                            }

                                                            result.addData(contact.getAnketa().getName(), barInName, income);
                                                            result.addData(contact.getAnketa().getName(), barOutName, outcome );
                                                            c.countDown();

                                                            if (c.getCount() == 0) {
                                                                changeActivity(result);
                                                            }
                                                        }
                                                    });
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT);
                                            }

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        };


        ((Button) findViewById(R.id.btnMessageCount)).setOnClickListener(onClickListener);
    }


    private BarchartWrapper makeBarchart(int id) throws Exception {

        BarchartWrapper result = new BarchartWrapper();


        switch (id) {
            case R.id.btnMessageCount:

                break;
            case R.id.btnMessagePeriod:
                break;
            default:
        }

        return result;
    }


    private void changeActivity(BarchartWrapper barchart) {
        Intent myIntent = new Intent(StatisticsActivity.this, BarChartActivity.class);
        myIntent.putExtra("chart", barchart);

        StatisticsActivity.this.startActivity(myIntent);
    }

    @Override
    protected void onApiAvailable() {
        // api = apiService.getApi();
    }

    @Override
    protected void onApiUnavailable() {

    }
}