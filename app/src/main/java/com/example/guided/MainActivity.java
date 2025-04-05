package com.example.guided;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BroadcastReceiver broadcastReceiver;

    Button register;
    Button logIn;
    Button btn3;
    Button btn4;
    Button btn5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        View mainView = findViewById(R.id.main);
        setContentView(R.layout.activity_main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        else {
            Log.e("MainActivity", "View with ID 'main' not found!");
        }



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();



        broadcastReceiver = new InternetReceiver();
        Internetstatus();



        if (user != null) {
            // המשתמש מחובר, המשיכי לאפליקציה
            String userId = user.getUid();
            String email = user.getEmail();
            Log.e("MainActivity", "id ="+ userId+" email= "+ email);
            Intent intent = new Intent(this, Home_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }


        register=findViewById(R.id.signUp);
        register.setOnClickListener(this);
        logIn=findViewById(R.id.logIn);
        logIn.setOnClickListener(this);
        btn3=findViewById(R.id.add_operation);
        btn3.setOnClickListener(this);
        btn5=findViewById(R.id.add_trip);
        btn5.setOnClickListener(this);
        btn4=findViewById(R.id.home);
        btn4.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        Intent intent;
        if(v==register) {
            intent=new Intent(this, Register_num_one.class);
            startActivity(intent);


        }
        else if(v==logIn) {
            intent=new Intent(this, Log_in.class);
            startActivity(intent);
        }
        else if (v==btn4){
            intent=new Intent(this, Home_page.class);
            startActivity(intent);
        }
        else if (v==btn3) {
            intent=new Intent(this, Add_operation.class);
            startActivity(intent);
        }
        else if (v==btn5) {
            intent=new Intent(this, Add_trip.class);
            startActivity(intent);
        }
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

