package com.example.guided;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * {@code CheckInternet} היא מחלקת עזר לבדיקת מצב חיבור האינטרנט של המכשיר.
 */
public class CheckInternet {

    /**
     * בודקת אם יש חיבור לרשת (Wi-Fi או נתונים סלולריים).
     *
     * @param context הקשר ממנו מתבצעת הבדיקה (Activity).
     * @return {@code "connected"} אם יש חיבור פעיל לרשת, {@code "disconnected"} אחרת.
     */
    public static String getNetworkInfo(Context context){

        String status = null;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.
                        getSystemService(
                                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager.
                        getActiveNetworkInfo();

        if (networkInfo!=null){
            status = "connected";
            return status;
        }
        else {
            status = "disconnected";
            return status;
        }
    }
}
