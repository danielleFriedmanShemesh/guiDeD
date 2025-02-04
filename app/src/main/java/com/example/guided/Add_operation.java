package com.example.guided;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

    TextView length;

    Button addMetodaBTN;
    EditText metodaLength;
    EditText title;
    EditText description;
    EditText equipment;
    LinearLayout metodaLayout;
    ArrayList<Metoda> metodotArr = new ArrayList<>();
    Metoda tempMetoda;
    int i=0;



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

        length = findViewById(R.id.length);

        metodaLayout = findViewById(R.id.llMetoda);

        addMetodaBTN= findViewById(R.id.addMetoda);
        addMetodaBTN.setOnClickListener(this);

        title = findViewById(R.id.title);
        metodaLength = findViewById(R.id.lengthInMinutes);
        description = findViewById(R.id.description);
        equipment = findViewById(R.id.equipment);


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
        if(v == addMetodaBTN){
            addMetoda();
        }

    }
    private void addMetoda(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        int dynamicLayoutId = ViewCompat.generateViewId();
        linearLayout.setId(dynamicLayoutId);
        linearLayout.setLayoutParams( new LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));

        // Create 4 EditTexts dynamically
        EditText metodaTitle = new EditText(this);
        metodaTitle.setId(View.generateViewId());
        metodaTitle.setHint("כותרת");
        metodaTitle.setTextSize(25);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
                150, LinearLayout.LayoutParams.WRAP_CONTENT );
        params4.setMarginStart(30);
        params4.setMargins(0,15,0,0);
        metodaTitle.setLayoutParams(params4);



        EditText metodaLengthInMinutes = new EditText(this);
        metodaLengthInMinutes.setId(View.generateViewId());
        metodaLengthInMinutes.setHint("זמן(בדקות)");
        metodaLengthInMinutes.setTextSize(20);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        params3.setMarginStart(30);
        params3.setMargins(0,10,0,0);
        metodaLengthInMinutes.setLayoutParams(params3);


        EditText metodaDescription = new EditText(this);
        metodaDescription.setId(View.generateViewId());
        metodaDescription.setHint("תיאור");
        metodaDescription.setTextSize(25);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                700, 400 );
        params.setMarginStart(60);
        params.setMargins(0,30,0,0);
        metodaDescription.setLayoutParams(params);

        EditText metodaEquipment = new EditText(this);
        metodaEquipment.setId(View.generateViewId());
        metodaEquipment.setHint("הוספת עזרים");
        metodaEquipment.setTextSize(25);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                300, LinearLayout.LayoutParams.WRAP_CONTENT );
        params2.setMarginStart(30);
        params2.setMargins(0,15,0,0);
        metodaEquipment.setLayoutParams(params2);


        // Create a Button dynamically
        Button addNewMetodaBTN = new Button(this);
        addNewMetodaBTN.setId(ViewCompat.generateViewId());
        addNewMetodaBTN.setText("+ הוספת מתודה");
        addNewMetodaBTN.setBackgroundColor(-1);
        addNewMetodaBTN.setTextColor(BLACK);
        addNewMetodaBTN.setTextSize(25);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        params5.setMargins(0,15,0,0);
        addNewMetodaBTN.setLayoutParams(params5);


        addNewMetodaBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v == addNewMetodaBTN){
                    addMetoda();
                }
            }
        });

        // Add views to the dynamically created ConstraintLayout
        linearLayout.addView(metodaTitle);
        linearLayout.addView(metodaLengthInMinutes);
        linearLayout.addView(metodaDescription);
        linearLayout.addView(metodaEquipment);
        linearLayout.addView(addNewMetodaBTN);

//        tempMetoda = new Metoda(
//                metodaTitle.getText().toString(),
//                Integer.parseInt(
//                        metodaLengthInMinutes.getText().toString()),
//                metodaDescription.getText().toString(),
//                metodaEquipment.getText().toString());
//        metodotArr.add(tempMetoda);

        metodaLayout.addView(linearLayout);
    }


}