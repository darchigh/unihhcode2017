package com.chatbotapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Background Service Thread that holds WebApi so it does not loose cookies when changing Activites.
 */
public class ApiService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private MambaWebApi api;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ApiService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ApiService.this;
        }
    }

    public ApiService() {
        api = new MambaWebApi();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public MambaWebApi getApi() {
        return api;
    }
}
