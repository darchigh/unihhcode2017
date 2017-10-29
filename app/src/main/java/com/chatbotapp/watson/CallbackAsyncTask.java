package com.chatbotapp.watson;

import android.os.AsyncTask;

/**
 * Created by U550264 on 29.10.2017.
 */

abstract class CallbackAsyncTask<A,B,C> extends AsyncTask<A,B,C> {
    private OnTaskCompleted listener;
    public CallbackAsyncTask(OnTaskCompleted listener) {
        super();
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(C c) {
        super.onPostExecute(c);
        listener.onTaskCompleted(c);
    }
}
