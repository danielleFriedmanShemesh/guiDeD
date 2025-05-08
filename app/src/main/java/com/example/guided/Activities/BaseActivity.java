package com.example.guided.Activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guided.InternetReceiver;

/**
        * BaseActivity היא מחלקת בסיס עבור Activities באפליקציה.
 * היא מטפלת בהאזנה לשינויים בחיבור לאינטרנט בעזרת BroadcastReceiver.
        * מחלקות אחרות יכולות להוריש ממנה כדי לקבל את ההתנהגות הזו כברירת מחדל.
        */
public class BaseActivity extends AppCompatActivity{

    /** מאזין לשינויים במצב החיבור לאינטרנט */
    private InternetReceiver broadcastReceiver;

    /**
     * מופעל כאשר ה-Activity נוצר. מאתחל את ה-BroadcastReceiver ומרשם אותו.
     *
     * @param savedInstanceState מצב שמור של ה-Activity, אם קיים
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // יצירת אובייקט שמאזין לשינויים בחיבור לרשת
        broadcastReceiver = new InternetReceiver();

        // רישום ה-Receiver
        Internetstatus();
    }

    /**
     * רושם את ה-BroadcastReceiver כדי להאזין לשינויים במצב הרשת.
     */
    public void Internetstatus(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * מופעל כאשר ה-Activity עובר למצב Pause.
     * מבטל את הרישום של ה-BroadcastReceiver כדי למנוע הדלפות זיכרון.
     */
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
