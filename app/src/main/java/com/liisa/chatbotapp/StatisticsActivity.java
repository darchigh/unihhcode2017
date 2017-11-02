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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liisa_000 on 14/05/2017.
 */

public class StatisticsActivity extends ApiActivity {

    private static final String email = "nathalie.degtjanikov@gmail.com";
    private static final String password = "Schokobanane123";

    private final static int DEFAULT_BAR_COLOR_1 = Color.BLUE;
    private final static int DEFAULT_BAR_COLOR_2 = Color.RED;
    private final static int DEFAULT_BAR_COLOR_3 = Color.GREEN;
    private final static int DEFAULT_BAR_COLOR_4 = Color.YELLOW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        addBtnMessagePeriod();
        addBtnAge();
        addBtnMessageCount();
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

    private void addBtnMessagePeriod() {
        ((Button) findViewById(R.id.btnMessagePeriod)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final BarchartWrapper result = new BarchartWrapper();

                try {
                    final Map<String, Integer> daytimes = new HashMap<>();
                    daytimes.put("Morgen", 0);
                    daytimes.put("Vormittag", 0);
                    daytimes.put("Mittag", 0);
                    daytimes.put("Nachmittag", 0);
                    daytimes.put("Abend", 0);
                    daytimes.put("Nacht", 0);

                    final Calendar cal = Calendar.getInstance();
                    final MambaWebApi api = apiService.getApi();

                    api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                        @Override
                        public void doResponse(Logon logon) {
                            try {
                                Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                                if (logon.isSuccessful()) {
                                    api.getContacts(new MambaWebApi.IResponse<Contacts>() {
                                        @Override
                                        public void doResponse(Contacts contacts) {
                                            // Await-Methode führt zu einem Stop des gesamten Programmes....
                                            final CountDownLatch c = new CountDownLatch(contacts.getContacts().length);

                                            try {
                                                for (final Contact contact : contacts.getContacts()) {
                                                    api.getChat(contact.getAnketa().getUserId(), new MambaWebApi.IResponse<ChatMessages>() {
                                                        @Override
                                                        public void doResponse(ChatMessages chatMessage) {
                                                            for (ChatMessage message : chatMessage.getMessages()) {
                                                                cal.setTimeInMillis(message.getCreated());
                                                                int hour = cal.get(Calendar.HOUR_OF_DAY);

                                                                // Morgen: 5 bis 10 Uhr
                                                                // Vormittag: 10 bis 12 Uhr
                                                                // Mittag: 12 bis 14 Uhr
                                                                // Nachmittag: 14 bis 18 Uhr
                                                                // Abend: 18 bis 23 Uhr
                                                                // Nacht: 23 bis 5 Uhr

                                                                if (hour >= 5 && hour < 10)
                                                                    daytimes.put("Morgen", daytimes.get("Morgen") + 1);
                                                                else if (hour >= 10 && hour < 12)
                                                                    daytimes.put("Vormittag", daytimes.get("Vormittag") + 1);
                                                                else if (hour >= 12 && hour < 14)
                                                                    daytimes.put("Mittag", daytimes.get("Mittag") + 1);
                                                                else if (hour >= 14 && hour < 18)
                                                                    daytimes.put("Nachmittag", daytimes.get("Nachmittag") + 1);
                                                                else if (hour >= 18 && hour < 23)
                                                                    daytimes.put("Abend", daytimes.get("Abend") + 1);
                                                                else
                                                                    daytimes.put("Nacht", daytimes.get("Nacht") + 1);
                                                            }

                                                            c.countDown();

                                                            if (c.getCount() == 0) {
                                                                result.addBarset("# Nachrichten", DEFAULT_BAR_COLOR_1);

                                                                for (Map.Entry<String, Integer> dayTime : daytimes.entrySet()) {
                                                                    result.addXAxisCaption(dayTime.getKey());
                                                                    result.addData(dayTime.getKey(), "# Nachrichten", dayTime.getValue());
                                                                }
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
        });
    }

    private void addBtnAge() {
        ((Button) findViewById(R.id.btnAge)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final BarchartWrapper result = new BarchartWrapper();

                try {
                    final Map<String, Integer> ageGroups = new HashMap<>();
                    ageGroups.put("18 - 25", 0);
                    ageGroups.put("26 - 35", 0);
                    ageGroups.put("36 - 45", 0);
                    ageGroups.put("46 - 55", 0);
                    ageGroups.put("55+", 0);
                    ageGroups.put("Unbekannt", 0);

                    final Calendar cal = Calendar.getInstance();
                    final MambaWebApi api = apiService.getApi();

                    api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                        @Override
                        public void doResponse(Logon logon) {
                            try {
                                Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                                if (logon.isSuccessful()) {
                                    api.getContacts(new MambaWebApi.IResponse<Contacts>() {
                                        @Override
                                        public void doResponse(Contacts contacts) {
                                            for (final Contact contact : contacts.getContacts()) {
                                                int age = contact.getAnketa().getAge();

                                                if (age >= 18 && age <= 25)
                                                    ageGroups.put("18 - 25", ageGroups.get("18 - 25") + 1);
                                                else if (age >= 26 && age <= 35)
                                                    ageGroups.put("26 - 35", ageGroups.get("26 - 35") + 1);
                                                else if (age >= 36 && age <= 45)
                                                    ageGroups.put("36 - 45", ageGroups.get("36 - 45") + 1);
                                                else if (age >= 46 && age <= 55)
                                                    ageGroups.put("46 - 55", ageGroups.get("46 - 55") + 1);
                                                else if (age > 55)
                                                    ageGroups.put("55+", ageGroups.get("55+") + 1);
                                                else
                                                    ageGroups.put("Unbekannt", ageGroups.get("Unbekannt") + 1);
                                            }

                                            result.addBarset("# Personen", DEFAULT_BAR_COLOR_1);

                                            for (Map.Entry<String, Integer> ageGroup : ageGroups.entrySet()) {
                                                result.addXAxisCaption(ageGroup.getKey());
                                                result.addData(ageGroup.getKey(), "# Personen", ageGroup.getValue());
                                            }

                                            changeActivity(result);
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
        });
    }


    private void addBtnMessageCount() {
        ((Button) findViewById(R.id.btnMessageCount)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final BarchartWrapper result = new BarchartWrapper();

                try {
                    final String barInName = "Nachrichten Eingehend";
                    final String barOutName = "Nachrichten Ausgehend";

                    result.addBarset(barInName, DEFAULT_BAR_COLOR_1);
                    result.addBarset(barOutName, DEFAULT_BAR_COLOR_2);

                    final MambaWebApi api = apiService.getApi();

                    api.logon(email, password, new MambaWebApi.IResponse<Logon>() {
                        @Override
                        public void doResponse(Logon logon) {
                            try {
                                Log.i("MambaWebApi", "Logon successful: " + logon.isSuccessful());

                                if (logon.isSuccessful()) {
                                    api.getContacts(new MambaWebApi.IResponse<Contacts>() {
                                        @Override
                                        public void doResponse(Contacts contacts) {
                                            // Await-Methode führt zu einem Stop des gesamten Programmes....
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
                                                            result.addData(contact.getAnketa().getName(), barOutName, outcome);
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
        });
    }


}