package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guided.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_page extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

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

   //     navView = findViewById(R.id.bottomNavigationView);
   //     navView.setItemIconTintList(null);

        replaceFragment(new HomeFragment());


//        navView.setOnItemSelectedListener(item -> {
////            switch (item.getItemId()){
////                case R.id.home:
////                    ;
////            }
//            return true;
//        });
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
        return false;
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        //getMenuInflater().inflate(R.menu.toolbar_menu,menu);
//        return true;
//    }
//       public boolean onOptionsItemSelected (MenuItem item){
//           super.onOptionsItemSelected(item);
//           int id= item.getItemId();
//        if(id == R.id.action_profile){
//            Toast.makeText(this, "action_profile", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else if(id ==  R.id.action_add_operation){
//            Toast.makeText(this, "action_add_operation", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else if(id ==  R.id.action_add_trip){
//            Toast.makeText(this, "action_add_trip", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else if(id ==  R.id.action_library_operations){
//            Toast.makeText(this, "action_library_operations", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else if(id ==  R.id.action_library_trips){
//            Toast.makeText(this, "action_library_trips", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        else if(id ==  R.id.home){
//            Toast.makeText(this, "action_home", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//       }
//



}