package com.example.guided;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

/**
 * {@code InternetReceiver} היא מחלקה שמאזינה לשינויים בחיבור לרשת.
 * במקרה של ניתוק מהרשת, היא מציגה דיאלוג למשתמש עם כפתור לנסות שוב (Retry).
 */
public class InternetReceiver extends BroadcastReceiver {
    private Button retryBTN; //כפתור "נסה שוב" שמופיע בדיאלוג כאשר אין חיבור אינטרנט.

    /**
     * מופעל כאשר מתקבלת הודעה על שינוי בחיבור לרשת.
     *
     * @param context הקשר של האפליקציה (Activity ).
     * @param intent  האינטנט שמכיל מידע על השינוי שקרה.
     */
    @Override
    public void onReceive(
            Context context,
            Intent intent) {
        String status = CheckInternet.
                getNetworkInfo(context);
        if (status.equals("disconnected")){
            // מציגים דיאלוג רק אם זה Activity תקף
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                    @Override
                    public void run() {
                        if (context instanceof Activity) {
                            Activity activity = (Activity) context;
                            if (!activity.isFinishing() &&
                                    !activity.isDestroyed()) {
                                InternetReceiver.this.
                                        showNoInternetDialog(
                                                context,
                                                intent);
                            }
                        }
                    }
                });
        }
    }

    /**
     * מציג דיאלוג שלא ניתן לבטל, עם כפתור "נסה שוב", כאשר אין חיבור לרשת.
     * לחיצה על הכפתור תפעיל מחדש את בדיקת החיבור.
     *
     * @param context הקשר שבו יוצג הדיאלוג.
     * @param intent  האינטנט המקורי שהועבר ל-onReceive.
     */
    private void showNoInternetDialog(
            Context context,
            Intent intent) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(
                        R.layout.check_internet_connection_dialog);
                dialog.setCancelable(false);
                dialog.show();

                retryBTN = dialog.findViewById(R.id.retry);
                retryBTN.setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onReceive(context, intent); // ניסיון נוסף לבדוק חיבור
                    }
                });
            }
        });
    }
}
