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

/**
 * מחלקה המאפשרת יצירה ועריכה של פעולה חינוכית הכוללת מטודות.
 * כוללת שדות קלט, RecyclerView להצגת המטודות, ממשק לשמירה למסד נתונים (Firebase),
 * וממשק גרפי לניהול המטודות (הוספה, מחיקה, גרירה).
 */

public class Add_operation extends BaseActivity implements View.OnClickListener {
    // שדות ממשק משתמש, משתנים פנימיים, מתאמים ונתונים...

    // שדות כלליים למסך הפעולה
    private EditText topic;// שדה להזנת שם פעולה
    private TextView length;//שדה המציג את האורך הכולל של הפעולה (בדקות)
    private int lengthCount = 0; // משתנה הסופר את אורך הפעולה הכולל
    private Switch privateORpublic; // Switch לבחירת פעולה פרטית או ציבורית
    private EditText goals;// שדה להזנת מטרות הפעולה
    private EditText equipments;// שדה להזנת עזרים לפעולה
    private TextView ageAdjustments;//שדה לבחירת גיל החניכים

    /** שדות לדיאלוג הוספת מטודה להזנת פרטי המתודה */
    private Button addMetodaBTN; //כפתור להוספת מתודה חדשה
    private EditText metodaLength;//אורך המטודה
    private EditText title;//כותרת המתודה
    private EditText description;//תוכן המתודה
    private EditText equipment;//עזרים למתודה
    private Dialog addNewMetodaDialog; //דיאלוג להזנת מתודה חדשה
    private Button saveMetoda; // כפתור לשמירת מתודה מהדיאלוג

    // מערך מטודות ורשימה
    private ArrayList<Metoda> metodotArr; //רשימת המתודות בפעולה
    private int id = 0; // מזהה ייחודי לכל מתודה
    private RecyclerView recyclerView; //תצוגת RecyclerView להצגת המתודות
    private RecyclerAdapterOperation recyclerAdapter; //אדפטר לרשימת המתודות

    // כפתורים כלליים
    private ImageButton exitBTN; //כפתור יציאה
    private Button saveOperationBTN; //כפתור שמירת פעולה

    // מידע על פעולה
    private Operation operation; //האובייקט של הפעולה עצמה
    private String operationKey = ""; //מפתח הפעולה (אם מדובר בעריכה)
    private OperationsAndTripsHelper operationsAndTripsHelper; //אובייקט עזר לדיאלוגים ופעולות הקשורות לטיולים ולפעולות.

    /**
     * מופעל בעת יצירת הפעולה.
     * מאתחל את כל רכיבי הממשק, מקבל פעולה קיימת במידת הצורך ומציב מאזינים.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // אתחול רכיבים, מאזינים, טיפול בעריכה של פעולה קיימת

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_operation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול רכיבים מה-XML

        topic = findViewById(R.id.topic);
        length = findViewById(R.id.length);
        goals = findViewById(R.id.goals);
        equipments = findViewById(R.id.equipments);
        privateORpublic = findViewById(R.id.publicORpivate);
        ageAdjustments = findViewById(R.id.age);

        exitBTN = findViewById(R.id.exit);
        saveOperationBTN = findViewById(R.id.save);
        addMetodaBTN= findViewById(R.id.addMetoda);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        metodotArr = new ArrayList<>();

        operationsAndTripsHelper = new OperationsAndTripsHelper(Add_operation.this);

        setAdapter();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // שינוי אייקון לפי סטטוס פרטי/ציבורי
        privateORpublic.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked) {
                if(!privateORpublic.isChecked()){
                    privateORpublic.setThumbDrawable(
                            ContextCompat.getDrawable(
                                    Add_operation.this,
                                    drawable.baseline_person_24));
                }
                else {
                    privateORpublic.setThumbDrawable(
                            ContextCompat.getDrawable(
                                    Add_operation.this,
                                    drawable.baseline_groups_24));
                }
            }
        } );

        // מאזינים
        exitBTN.setOnClickListener(this);
        saveOperationBTN.setOnClickListener(this);
        addMetodaBTN.setOnClickListener(this);
        ageAdjustments.setOnClickListener(this);

        // קבלת פעולה לעריכה (אם נשלחה כ-intent extra)
        Intent intent = getIntent();
        if (intent != null) {
            operationKey = intent.getStringExtra("operationKey");
            if (operationKey != null) {
                FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
                fireBaseOperationHelper.fetchOneOperation(
                        new FireBaseOperationHelper.DataStatusM() {
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
                            privateORpublic.setThumbDrawable(
                                    ContextCompat.getDrawable(
                                            Add_operation.this,
                                            drawable.baseline_groups_24));
                        } else if (operation.getPrivateORpublic().equals("isPrivate")) {
                            privateORpublic.setThumbDrawable(
                                    ContextCompat.getDrawable(
                                            Add_operation.this,
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

    /**
     * מאזין לכל הלחצנים במסך.
     * מבצע פעולה שונה בהתאם ללחצן שנלחץ: הוספת מטודה, שמירת מתודה, שמירת פעולה, דיאלוג יציאה עם או בלי שמירה, ופתיחת דיאלוג התאמת גיל.
     *
     * @param v רכיב ה-View שנלחץ
     */
    @Override
    public void onClick(View v) {
        // טיפול בלחיצות

        if (v == addMetodaBTN)
            addMetoda();
        else if (v == saveMetoda)
            saveMetoda();
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
        // פתיחת דיאלוג והצגת שדות הזנת מטודה

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

    /**
     * שומר את המטודה החדשה שהוזנה בדיאלוג, ומעדכן את רשימת המטודות.
     * מחשב מחדש את האורך הכולל של הפעולה.
     */
    @SuppressLint("SetTextI18n")
    public void saveMetoda(){
        // בדיקת קלט, יצירת אובייקט מטודה, הוספה לרשימה, עדכון RecyclerView

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

        Metoda newMetoda = new Metoda(
                titleStr,
                metodaLengthInt,
                descriptionStr,
                equipmentStr,
                id);
        metodotArr.add(newMetoda);
        id++;

        recyclerAdapter.notifyDataSetChanged();
        lengthCount = lengthCount + metodaLengthInt;
        length.setText(lengthCount + " דקות ");

        addNewMetodaDialog.dismiss();
    }

    /**
     * שומר את הפעולה כולה למסד הנתונים Firebase.
     * מאחזר פרטי משתמש כדי לצרף אותם לפעולה לפני השמירה.
     */
    public void saveOperation() {
        // בניית אובייקט Operation, שמירה ל-Firebase

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
        firebaseUserHelper.fetchUserData(
                new FirebaseUserHelper.UserDataCallback() {
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

                operationsAndTripsHelper.saveOperation(
                        operation,
                        operationKey);
            }
            @Override
            public void onError(String errorMessage) {}
        });
    }

    /**
     * ממשק גרירה והחלקה של פריטים ב-RecyclerView.
     * מאפשר הזזת מטודות וסידור מחדש, או מחיקה עם אפשרות ביטול.
     */
    Metoda deletedMetoda = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.
            SimpleCallback(
                    ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            // הזזת פריט
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(metodotArr, fromPosition, toPosition);
            recyclerAdapter.notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onSwiped(
                @NonNull RecyclerView.ViewHolder viewHolder,
                int direction) {
            // החלקה לצדדים ומחיקת פריט עם אפשרות undo

            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                deletedMetoda = metodotArr.get(position);
                metodotArr.remove(position);
                recyclerAdapter.notifyItemRemoved(position);

                lengthCount = lengthCount - deletedMetoda.getLength();
                length.setText(lengthCount + " דקות ");

                Snackbar.make(
                        recyclerView,
                        deletedMetoda.toString(),
                        Snackbar.LENGTH_LONG).setAction(
                                "undo",
                        new View.OnClickListener() {
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
                                float dX,
                                float dY,
                                int actionState,
                                boolean isCurrentlyActive) {
            // ציור רקע ואייקון מחיקה

            new RecyclerViewSwipeDecorator.Builder(c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                    Add_operation.this,
                                    R.color.red))
                    .addSwipeLeftActionIcon(drawable.trash)
                    .create()
                    .decorate();
            super.onChildDraw(c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive);
        }
    };

    /**
     * מאתחלת את ה־RecyclerView עם מתאם מסוג {@link RecyclerAdapterOperation}
     * עבור רשימת המטודות (metodotArr).
     * בנוסף, מגדירה מאזין לשינויים ברשימת המטודות כדי לעדכן את הסכום הכולל של הדקות
     * ולהציגו ב־TextView.
     */
    private void setAdapter(){
        recyclerAdapter = new RecyclerAdapterOperation(
                metodotArr,
                Add_operation.this);

        // מאזין לשינויי רשימת מטודות
        recyclerAdapter.setOnMetodaListChangedListener(
                new RecyclerAdapterOperation.OnMetodaListChangedListener() {
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