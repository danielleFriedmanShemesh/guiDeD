package com.example.guided.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.Metoda;
import com.example.guided.Classes.Operation;
import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.ListAdapters.MetodotListViewAdapter;
import com.example.guided.R;

import java.util.ArrayList;

public class View_operation extends BaseActivity implements View.OnClickListener {
    private TextView title; //שם הפעולה
    private TextView writer; //שם המשתמש של כותב הפעולה
    private TextView age; // התאמת גיל החניכים
    private TextView length; //שדה המציג את האורך הכולל של הפעולה (בדקות)
    private TextView organization; // תנועת הנוער בה נמצא כותב הפעולה
    private TextView goals;// מטרות הפעולה
    private TextView equipments; // ציוד נדרש

    ImageButton exit; // כפתור סגירת המסך
    ListView metodot; //ListView להצגת מתודות הפעולה
    MetodotListViewAdapter metodotListViewAdapter; // מתאם להצגת המתודות ב-ListView
    FireBaseOperationHelper fireBaseOperationHelper; //עוזר לשליפת נתוני טיול מ-Firebase

    ArrayList<Metoda> metodaArrayList; //רשימת מתודות הפעולה
    Operation operation; //האובייקט שמכיל את פרטי הפעולה

    /**
     * פעולה זו מופעלת בעת פתיחת המסך.
     * היא מאתחלת את כל הרכיבים, שולפת את מפתח הפעולה מה-Intent,
     * ומציגה את הנתונים מהFirebase במסך.
     *
     * @param savedInstanceState שמירת מצב קודם אם קיים
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_operation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        title = findViewById(R.id.titleOfOp);
        writer = findViewById(R.id.writer_Username);
        age = findViewById(R.id.ageOp);
        length = findViewById(R.id.lengthOp);
        organization = findViewById(R.id.organizationOp);
        goals = findViewById(R.id.goalsOp);
        equipments = findViewById(R.id.equipmentsOp);

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);

        metodot = findViewById(R.id.list_view);

        Intent intent = getIntent();
        String operationKey = intent.getStringExtra("operationKey");

        fireBaseOperationHelper = new FireBaseOperationHelper();
        fireBaseOperationHelper.fetchOneOperation(
                new FireBaseOperationHelper.DataStatusM() {
            /**
             * פעולה זו מופעלת כאשר נתוני הפעולה התקבלו מהFirebase.
             * היא מציגה את הנתונים במסך, ומאתחלת את רשימת המתודות.
             *
             * @param o אובייקט הפעולה שהתקבלה
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataLoaded(Operation o) {
                operation = o;

                title.setText(operation.getNameOfOperation());
                writer.setText(operation.getUserName());
                age.setText(operation.getAge());
                length.setText(operation.getLengthOfOperation()+"");
                organization.setText(operation.getOrganization());
                goals.setText(operation.getGoals());
                equipments.setText(operation.getEquipment());

                metodaArrayList = operation.getMetodotArr();
                metodotListViewAdapter = new MetodotListViewAdapter(
                        View_operation.this,
                        0,
                        0,
                        metodaArrayList);
                metodot.setAdapter(metodotListViewAdapter);
            }
        },operationKey);
    }

    /**
     * טיפול בלחיצות על כפתורים: סגירה .
     *
     * @param v הרכיב שנלחץ
     */
    @Override
    public void onClick(View v) {
        if (v == exit)
            finish();
    }
}