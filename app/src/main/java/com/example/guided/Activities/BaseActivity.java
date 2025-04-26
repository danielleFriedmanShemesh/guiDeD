package com.example.guided.Activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{

    InternetReceiver broadcastReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new InternetReceiver();
        Internetstatus();
    }


    public void Internetstatus(){
        registerReceiver(
                broadcastReceiver,
                new IntentFilter(
                        ConnectivityManager.
                                CONNECTIVITY_ACTION));    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            // אם ה-Receiver לא נרשם, אנחנו לא צריכים לפתח שגיאה
            Log.e("ReceiverError", "Receiver was not registered");
        }
    }
}
