package com.example.guided;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class InternetReceiver extends BroadcastReceiver {
    Button retryBTN;

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if (status.equals("connected")){
        }
        else if (status.equals("disconnected")){

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (context instanceof Activity) {
                        Activity activity = (Activity) context;
                        if (!activity.isFinishing() && !activity.isDestroyed()) {
                            showNoInternetDialog(context,intent);
                        }
                    }
                });



        }
    }
    private void showNoInternetDialog(Context context, Intent intent) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.check_internet_connection_dialog);
            dialog.setCancelable(false);
            dialog.show();

            retryBTN = dialog.findViewById(R.id.retry);
            retryBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);

                }
            });

        });
    }

}
