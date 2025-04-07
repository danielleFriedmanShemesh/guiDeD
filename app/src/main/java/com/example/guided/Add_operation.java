package com.example.guided;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.example.guided.R.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Add_operation extends BaseActivity implements View.OnClickListener {

    // TODO: להוסיף אפשרות לשמור מתודה ופעולה בכללי שלא כל השדות מלאים מבלי שיקרוס
    EditText topic;//שם פעולה
    TextView length;//אורך הפעולה
    int lengthCount = 0;
    Switch privateORpublic;
    EditText goals;//מטרות הפעולה
    EditText equipments;// עזרים לפעולה
    TextView ageAdjustments;//גיל החניכים
    String[] listAgeAdjustments;
    boolean[] checkedAgeAdjustments;
    ArrayList<Integer> userAgeAdjustments = new ArrayList<>();

    Button addMetodaBTN;
    EditText metodaLength;//אורך המטודה
    EditText title;//כותרת המתודה
    EditText description;//תוכן המתודה
    EditText equipment;//עזרים למתודה

    ArrayList<Metoda> metodotArr;
    int id = 0;
    RecyclerView recyclerView;
    RecyclerAdapterOperation recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    Dialog addNewMetodaDialog;
    Button saveMetoda;
    ImageButton exitBTN;

    Button saveOperationBTN;

    Operation operation;
    String operationKey = "";

    FireBaseOperationHelper fireBaseOperationHelper;


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
        length = findViewById(R.id.length);
        goals = findViewById(R.id.goals);
        equipments = findViewById(R.id.equipments);
        privateORpublic = findViewById(R.id.publicORpivate);

        privateORpublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!privateORpublic.isChecked()){
                    privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_operation.this,
                            drawable.baseline_person_24));
                }
                else {
                    privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_operation.this,
                            drawable.baseline_groups_24));
                }
            }
        } );


        exitBTN = findViewById(R.id.exit);
        exitBTN.setOnClickListener(this);

        saveOperationBTN = findViewById(R.id.save);
        saveOperationBTN.setOnClickListener(this);


        addMetodaBTN= findViewById(R.id.addMetoda);
        addMetodaBTN.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        metodotArr = new ArrayList<Metoda>();

        recyclerAdapter = new RecyclerAdapterOperation(metodotArr, Add_operation.this);
        recyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


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

        Intent intent = getIntent();
        if (intent != null){
        operationKey = intent.getStringExtra("operationKey");

        fireBaseOperationHelper = new FireBaseOperationHelper();
        fireBaseOperationHelper.fetchOneOperation(new FireBaseOperationHelper.DataStatusM() {
            @Override
            public void onDataLoaded(Operation o) {
                operation = o;

                topic.setText(operation.getNameOfOperation());
                ageAdjustments.setText(operation.getAge());
                lengthCount = lengthCount + operation.getLengthOfOperation();
                length.setText(lengthCount + "דקות");
                goals.setText(operation.getGoals());
                equipments.setText(operation.getEquipment());

                metodotArr = operation.getMetodotArr();
                //TODO: לשנות את הגיל למערך?

                recyclerAdapter = new RecyclerAdapterOperation(metodotArr, Add_operation.this);
                recyclerView.setAdapter(recyclerAdapter);
                //privte or public+ age+ metodot



            }
        },operationKey);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == addMetodaBTN){
            addMetoda();
        }
        else if (v ==saveMetoda) {
            saveMetoda();
        }
        else if(v == saveOperationBTN){
            saveOperation();
            finish();
        }
        else if (v == exitBTN){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("סגירת חלון");
            builder.setMessage("תרצו לשמור את השינויים?");
            builder.setCancelable(true);
            builder.setPositiveButton("שמור", new Add_operation.AlartDialogLostenerSaveOperation());
            builder.setNegativeButton("אל תשמור", new Add_operation.AlartDialogLostenerSaveOperation());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
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

        lengthCount = lengthCount + metodaLengthInt;
        length.setText(lengthCount + "דקות");


        addNewMetodaDialog.dismiss();
    }

    Metoda deletedMetoda = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        //להזיז את הפריט
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(metodotArr, fromPosition, toPosition);
            recyclerAdapter.notifyItemMoved(fromPosition,toPosition);

            return true;
        }

        @Override
        //להחליק לצדדים
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedMetoda = metodotArr.get(position);
                    metodotArr.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    lengthCount = lengthCount - deletedMetoda.getLength();;
                    length.setText(lengthCount + " דקות ");

                    Snackbar.make(recyclerView, deletedMetoda.toString(),Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            metodotArr.add(position, deletedMetoda);
                            recyclerAdapter.notifyItemInserted(position);

                            lengthCount = lengthCount + deletedMetoda.getLength();;
                            length.setText(lengthCount + "דקות");
                        }
                    }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Add_operation.this, R.color.red))
                    .addSwipeLeftActionIcon(drawable.trash)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    public void saveOperation() {

        String nameSTR = topic.getText().toString();
        String ageSTR = ageAdjustments.getText().toString();
        String publicORprivateSRT;
        if (!privateORpublic.isChecked())
            publicORprivateSRT = "isPrivate";
        else
            publicORprivateSRT = "isPublic";
        int lengthINT = lengthCount;
        String goalsSTR = goals.getText().toString();
        String equipmentsSTR = equipments.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("operations");
        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
            @Override
            public void onUserDataLoaded(User user) {
                String organizationSTR = user.getOrganization();
                String userNameSTR = user.getUserName();

                operation = new Operation(
                        nameSTR,
                        ageSTR,
                        publicORprivateSRT,
                        lengthINT,
                        goalsSTR,
                        equipmentsSTR,
                        metodotArr,
                        organizationSTR,
                        userNameSTR);

                String key;
                if(operationKey == null)
                key = myRef.push().getKey();
                else key = operationKey;
                operation.setKey(key);
                myRef.child(key).setValue(operation);

            }

            @Override
            public void onError(String errorMessage) {

            }
        });



    }

    private class AlartDialogLostenerSaveOperation implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == BUTTON_POSITIVE){
                saveOperation();
                finish();
            }
            else if(which == BUTTON_NEGATIVE){
                finish();
            }
        }
    }
}