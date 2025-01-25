package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1;
    Button btn2;
    Button btn3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn1=findViewById(R.id.signUp);
        btn1.setOnClickListener(this);
        btn2=findViewById(R.id.signIn);
        btn2.setOnClickListener(this);
        btn3=findViewById(R.id.add_operation);
        btn3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v==btn1) {
            intent=new Intent(this, Register_num_one.class);
            startActivity(intent);
            finish();
        }
        else if(v==btn2) {
            intent=new Intent(this, Register_num_two.class);
            startActivity(intent);
            finish();
        }
//        else if (v==btn3){
//            intent=new Intent(this, Home_page.class);
//            startActivity(intent);
//            finish();
//        }
        else if (v==btn3) {
            intent=new Intent(this, Add_operation.class);
            startActivity(intent);
            finish();
        }

    }
}