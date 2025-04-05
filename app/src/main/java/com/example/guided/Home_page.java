package com.example.guided;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guided.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_page extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ImageView menuBTN;
    BottomNavigationView navView;
    BroadcastReceiver broadcastReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        replaceFragment(new HomeFragment());


        menuBTN = findViewById(R.id.option_btn);
        menuBTN.setOnClickListener(this);

        navView = findViewById(R.id.bottomNavigationView);
        navView.setItemIconTintList(null);

        navView.setOnNavigationItemSelectedListener(this);


        broadcastReceiver = new InternetReceiver();
        Internetstatus();


    }

    @Override
    public void onClick(View v) {
        if(v == menuBTN){
            showMenu(v);
        }

    }
    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener.
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_main);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        int id= item.getItemId();
        if(id == R.id.action_profile){
            Toast.makeText(this, "action_profile", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id ==  R.id.action_add_operation){
            Toast.makeText(this, "action_add_operation", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Add_operation.class);
            startActivity(intent);
            return true;
        }
        else if(id ==  R.id.action_add_trip){
            Toast.makeText(this, "action_add_trip", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Add_trip.class);
            startActivity(intent);
            return true;
        }
        else if(id ==  R.id.action_library_operations){
            Toast.makeText(this, "action_library_operations", Toast.LENGTH_SHORT).show();
            replaceFragment(new LibraryOperationsFragment());
            return true;
        }
        else if(id ==  R.id.action_library_trips){
            Toast.makeText(this, "action_library_trips", Toast.LENGTH_SHORT).show();
            replaceFragment(new LibraryTripsFragment());
            return true;
        }
        else if(id ==  R.id.action_home){
            Toast.makeText(this, "action_home", Toast.LENGTH_SHORT).show();
            replaceFragment(new HomeFragment());
            return true;
        }
        else if (id == R.id.log_out){
            FirebaseAuth.getInstance().signOut();

            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // FLAG_ACTIVITY_CLEAR_TOP- מנקה את הדרך חזרה ומונע מהמשתמש לחזור למסכים קודמים עם כפתור Back.
            // FLAG_ACTIVITY_NEW_TASK - פותח את האקטיביטי החדשה בתוך טסק חדש. ועוזר "לנתק" את המסך החדש מהקודמים.
            startActivity(intent1);
            finish();

        }
        return false;
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        int id= item.getItemId();
        if(id == R.id.profile){
            Toast.makeText(this, "action_profile", Toast.LENGTH_SHORT).show();
            replaceFragment(new ProfileFragment());
            return true;
        }
        else if(id ==  R.id.add){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.bottom_sheet_layout);
            TextView addOpTv = dialog.findViewById(R.id.op);
            TextView addTrTv = dialog.findViewById(R.id.tr);
            addTrTv.setText("הוספת טיול");
            addOpTv.setText("הוספת פעולה");

            LinearLayout addOP = dialog.findViewById(R.id.opLl);
            addOP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(Home_page.this, Add_operation.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            LinearLayout addTR = dialog.findViewById(R.id.trLl);
            addTR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(Home_page.this, Add_trip.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            return true;
        }

        else if(id ==  R.id.library){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.bottom_sheet_layout);
            TextView libraryOpTv = dialog.findViewById(R.id.op);
            TextView libraryTrTv = dialog.findViewById(R.id.tr);
            libraryTrTv.setText("מאגר טיולים");
            libraryOpTv.setText("מאגר פעולות");

            LinearLayout libraryOP = dialog.findViewById(R.id.opLl);
            libraryOP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(new LibraryOperationsFragment());
                    dialog.dismiss();
                }
            });
            LinearLayout libraryTR = dialog.findViewById(R.id.trLl);
            libraryTR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(new LibraryTripsFragment());
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            return true;
        }

        else if(id ==  R.id.home){
            Toast.makeText(this, "action_home", Toast.LENGTH_SHORT).show();
            replaceFragment(new HomeFragment());
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // אם יש משתמש מחובר, לא נחזור אחורה
            Toast.makeText(this, "אתה כבר מחובר!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();  // במידה ואין משתמש מחובר, נשאיר את ההתנהגות הרגילה
        }
    }

    public void Internetstatus(){
        registerReceiver(
                broadcastReceiver,
                new IntentFilter(
                        ConnectivityManager.
                                CONNECTIVITY_ACTION));    }




}