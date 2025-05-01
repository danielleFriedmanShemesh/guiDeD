package com.example.guided.Activities;

import static com.example.guided.R.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Classes.Metoda;
import com.example.guided.Classes.Operation;
import com.example.guided.Classes.User;
import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Helpers.OperationsAndTripsHelper;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerAdapterOperation;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Add_operation extends BaseActivity implements View.OnClickListener {

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

        operationsAndTripsHelper = new OperationsAndTripsHelper(Add_operation.this);

        setAdapter();
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
        ageAdjustments.setOnClickListener(this);

        // קבלת פעולה לעריכה (אם נשלחה כ-intent extra)
        Intent intent = getIntent();
        if (intent != null) {
            operationKey = intent.getStringExtra("operationKey");
            if (operationKey != null) {
                fireBaseOperationHelper = new FireBaseOperationHelper();
                fireBaseOperationHelper.fetchOneOperation(new FireBaseOperationHelper.DataStatusM() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataLoaded(Operation o) {
                        operation = o;
                        topic.setText(operation.getNameOfOperation());
                        ageAdjustments.setText(operation.getAge());
                        lengthCount = lengthCount + operation.getLengthOfOperation();
                        length.setText(lengthCount + " דקות ");
                        goals.setText(operation.getGoals());
                        equipments.setText(operation.getEquipment());
                        if (operation.getPrivateORpublic().equals("isPublic")) {
                            privateORpublic.setChecked(true);
                            privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_operation.this,
                                    drawable.baseline_groups_24));
                        } else if (operation.getPrivateORpublic().equals("isPrivate")) {
                            privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_operation.this,
                                    drawable.baseline_person_24));
                        }
                        metodotArr = operation.getMetodotArr();
                        id = metodotArr.size();
                        setAdapter();
                        //TODO: לשנות את הגיל למערך?
                    }
                }, operationKey);
            }
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
        else if (v == exitBTN) {
            operationsAndTripsHelper
                    .showExitDialog(
                            new OperationsAndTripsHelper.ExitDialogCallback() {
                                @Override
                                public void onResult(boolean exitAndSave) {
                                    if (exitAndSave)
                                        saveOperation();
                                    finish();
                                }
                            });
        }
        if( v == ageAdjustments){
            operationsAndTripsHelper
                    .showAgeAdjustmentsDialog(
                            new OperationsAndTripsHelper.AgeDialogCallback() {
                @Override
                public void onResult(String age) {
                    ageAdjustments.setText(age);
                }
            });
        }
    }

    /**
     * מציג דיאלוג המאפשר למשתמש להזין פרטי מתודה חדשה.
     * שומר את ההתייחסות לדיאלוג ולשדות הקלט שבו מתוך ממשק המשתמש (ה־XML של הדיאלוג)..
     */
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
    @SuppressLint("SetTextI18n")
    public void saveMetoda(){
        int metodaLengthInt = 0;
        if(!metodaLength.getText().toString().isEmpty()){
            metodaLengthInt = Integer.parseInt(metodaLength.getText().toString());
        }
        String titleStr = "";
        titleStr = title.getText().toString();
        String descriptionStr = "";
        if(!description.getText().toString().isEmpty()){
            descriptionStr = description.getText().toString();
        }
        String equipmentStr = "";
        if(!equipment.getText().toString().isEmpty()){
            equipmentStr = equipment.getText().toString();
        }

        if(equipmentStr.isEmpty()||
                descriptionStr.isEmpty()||
                metodaLengthInt == 0)
            Toast.makeText(this, "לא כל השדות מלאים!", Toast.LENGTH_LONG).show();

        Metoda newMetoda = new Metoda(titleStr, metodaLengthInt, descriptionStr, equipmentStr, id);
        metodotArr.add(newMetoda);
        id++;

        recyclerAdapter.notifyDataSetChanged();
        lengthCount = lengthCount + metodaLengthInt;
        length.setText(lengthCount + " דקות ");

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

        @SuppressLint("SetTextI18n")
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
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            metodotArr.add(position, deletedMetoda);
                            recyclerAdapter.notifyItemInserted(position);

                            lengthCount = lengthCount + deletedMetoda.getLength();;
                            length.setText(lengthCount + " דקות ");
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

                operationsAndTripsHelper.saveOperation(operation, operationKey);
            }
            @Override
            public void onError(String errorMessage) {}
        });
    }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // החלקה לצדדים ומחיקת פריט עם אפשרות undo

            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                deletedMetoda = metodotArr.get(position);
                metodotArr.remove(position);
                recyclerAdapter.notifyItemRemoved(position);

                lengthCount = lengthCount - deletedMetoda.getLength();
                length.setText(lengthCount + " דקות ");

                Snackbar.make(recyclerView, deletedMetoda.toString(), Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        metodotArr.add(position, deletedMetoda);
                        recyclerAdapter.notifyItemInserted(position);

                        lengthCount = lengthCount + deletedMetoda.getLength();
                        length.setText(lengthCount + " דקות ");
                    }
                }).show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c,
                                @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // ציור רקע ואייקון מחיקה

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Add_operation.this, R.color.red))
                    .addSwipeLeftActionIcon(drawable.trash)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void setAdapter(){
        recyclerAdapter = new RecyclerAdapterOperation(
                metodotArr,
                Add_operation.this);

        // מאזין לשינויי רשימת מטודות
        recyclerAdapter.setOnMetodaListChangedListener(new RecyclerAdapterOperation.OnMetodaListChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMetodaListChanged(ArrayList<Metoda> metodot) {
                lengthCount = 0;
                for (Metoda m : metodot)
                    lengthCount += m.getLength();
                length.setText(lengthCount + " דקות ");
            }
        });

        recyclerView.setAdapter(recyclerAdapter);
    }
}