package com.chatbotapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity that automatically binds ApiService onStart and unbinds onStop. Implement onApiAvailable() and onApiUnavailable to actually use the Service.
 */
public abstract class ApiActivity extends AppCompatActivity {
    protected ApiService apiService;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ApiService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    protected abstract void onApiAvailable();

    protected abstract void onApiUnavailable();

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            apiService = ((ApiService.LocalBinder) service).getService();
            mBound = true;
            onApiAvailable();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            onApiUnavailable();
        }
    };

}
