package com.example.guided;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    ArrayList<Metoda> metodotArr;
    int id = 0;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    Dialog addNewMetodaDialog;
    Button saveMetoda;



    ImageButton exitBTN;

    @SuppressLint("NotifyDataSetChanged")
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


        addMetodaBTN= findViewById(R.id.addMetoda);
        addMetodaBTN.setOnClickListener(this);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        metodotArr = new ArrayList<Metoda>();

        recyclerAdapter = new RecyclerAdapter(metodotArr, Add_operation.this);
        recyclerView.setAdapter(recyclerAdapter);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);




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
        else if (v ==saveMetoda) {
            saveMetoda();
        }
    }
    //מקבל את הID של המתודה שאחרייה רוצים לשים את המתודה החדשה
    private void addMetoda(){

        addNewMetodaDialog = new Dialog(Add_operation.this);
        addNewMetodaDialog.setContentView(R.layout.metoda_layout);
        addNewMetodaDialog.setCancelable(true);

        title = addNewMetodaDialog.findViewById(R.id.title);
        metodaLength = addNewMetodaDialog.findViewById(R.id.lengthInMinutes);
        description = addNewMetodaDialog.findViewById(R.id.description);
        equipment = addNewMetodaDialog.findViewById(R.id.equipment);

        saveMetoda = addNewMetodaDialog.findViewById(R.id.saveMetoda);
        saveMetoda.setOnClickListener(this);

        addNewMetodaDialog.show();

    }
    public void saveMetoda(){
        int metodaLengthInt = Integer.parseInt(metodaLength.getText().toString());
        String titleStr = title.getText().toString();
        String descriptionStr = description.getText().toString();
        String equipmentStr = equipment.getText().toString();

        Metoda newMetoda = new Metoda(titleStr, metodaLengthInt, descriptionStr, equipmentStr, id);

        metodotArr.add(newMetoda);
        id++;
        recyclerAdapter.notifyDataSetChanged();

        addNewMetodaDialog.dismiss();
    }

//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END | ItemTouchHelper.START) {
//        @Override
//        //להזיז את הפריט
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//
//            int fromPosition = viewHolder.getAdapterPosition();
//            int toPosition = target.getAdapterPosition();
//
//
//
//            Collections.swap(metodotArr, fromPosition, toPosition);
//            recyclerAdapter.notifyItemMoved(fromPosition,toPosition);
//
//
//            return true;
//        }
//
//        @Override
//        //להחליק לצדדים
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//        }
//    };


}