package com.example.guided;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Add_operation extends AppCompatActivity implements View.OnClickListener {
    EditText topic;
    TextView ageAdjustments;
    String[] listAgeAdjustments;
    boolean[] checkedAgeAdjustments;
    ArrayList<Integer> userAgeAdjustments = new ArrayList<>();


    ImageButton exitBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_operation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        topic = findViewById(R.id.topic);

        exitBTN = findViewById(R.id.exit);
        exitBTN.setOnClickListener(this);


        ageAdjustments = findViewById(R.id.age);
        listAgeAdjustments = getResources().getStringArray(R.array.age_adjustment);
        checkedAgeAdjustments = new boolean[listAgeAdjustments.length];

        ageAdjustments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_operation.this);
                builder.setTitle("בחר את גיל החניכים: ");
                builder.setMultiChoiceItems(listAgeAdjustments, checkedAgeAdjustments, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(! userAgeAdjustments.contains(which)){
                                userAgeAdjustments.add(which);
                            }
                        }
                        else if (userAgeAdjustments.contains(which)){
                            userAgeAdjustments.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String age = "";
                        for(int i = 0; i < userAgeAdjustments.size(); i++){
                            age = age + listAgeAdjustments[userAgeAdjustments.get(i)];
                            if (i != userAgeAdjustments.size() - 1){
                                age = age + ", ";
                            }
                        }
                        ageAdjustments.setText(age);
                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}