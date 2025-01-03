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

        btn1=findViewById(R.id.button);
        btn1.setOnClickListener(this);
        btn2=findViewById(R.id.button2);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btn1){Intent intent=new Intent(this, Register_num_one.class);
            startActivity(intent);}
        if(v==btn2){Intent intent1=new Intent(this, Register_num_two.class);
            startActivity(intent1);}


    }
}