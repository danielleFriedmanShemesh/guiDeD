package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_page extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    ImageView menuBTN;
    BottomNavigationView navView;



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

        menuBTN = findViewById(R.id.option_btn);
        menuBTN.setOnClickListener(this);

        navView = findViewById(R.id.bottomNavigationView);
        navView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.home:
//                    ;
//            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        int id= item.getItemId();

        /* if ((id == R.id.action_home)){
            Intent intent=new Intent(this, Home_page.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_library_operations){
            Intent intent=new Intent(this, Operations_library.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_library_trips){
            Intent intent=new Intent(this, Trips_library.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_add_operation){
            Intent intent=new Intent(this, Add_operation.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_add_trip){
            Intent intent=new Intent(this, Add_trip.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.action_profile){
            Intent intent=new Intent(this, Profile.class);
            startActivity(intent);
            finish();
        }*/

        return true;
    }

    @Override
    public void onClick(View v) {

    }
}