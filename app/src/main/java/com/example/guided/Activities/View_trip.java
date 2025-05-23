package com.example.guided.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.Part;
import com.example.guided.Classes.Trip;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.ListAdapters.PartsListViewAdapter;
import com.example.guided.R;

import java.util.ArrayList;

public class View_trip extends BaseActivity implements View.OnClickListener {

    private TextView title; //שם הטיול
    private TextView writer; //שם המשתמש של כותב הטיול
    private TextView age; // התאמת גיל החניכים
    private TextView length; //שדה המציג את האורך הכולל של הטיול (בק"מ)
    private TextView organization; // תנועת הנוער בה נמצא כותב הטיול
    private TextView goals;// מטרות הטיול
    private TextView equipments; // ציוד נדרש
    private TextView area; // אזור הטיול
    private TextView place; // מקום ספציפי של הטיול
    private Button pictureBTN; // כפתור להצגת תמונת הטיול

    private ImageButton exit; // כפתור סגירת המסך

    private ListView parts; //ListView להצגת מקטעי הטיול
    private PartsListViewAdapter partsListViewAdapter; // מתאם להצגת המקטעים ב-ListView

    private ArrayList<Part> partArrayList; //רשימת מקטעי הטיול

    private Trip trip; //האובייקט שמכיל את פרטי הטיול

    /**
     * פעולה זו מופעלת בעת פתיחת המסך.
     * היא מאתחלת את כל הרכיבים, שולפת את מפתח הטיול מה-Intent,
     * ומציגה את הנתונים מהFirebase במסך.
     *
     * @param savedInstanceState שמירת מצב קודם אם קיים
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.titleOfTR);
        writer = findViewById(R.id.writer_Username);
        age = findViewById(R.id.ageTR);
        length = findViewById(R.id.lengthTR);
        organization = findViewById(R.id.organizationTR);
        goals = findViewById(R.id.goalsTR);
        equipments = findViewById(R.id.equipmentsTR);
        area = findViewById(R.id.areaOfTR);
        place = findViewById(R.id.placeOfTR);
        pictureBTN = findViewById(R.id.pictureBTN);
        pictureBTN.setOnClickListener(this);

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);

        parts = findViewById(R.id.list_view);

        // שליפת tripKey והצגת הטיול
        Intent intent = getIntent();
        String tripKey = intent.getStringExtra("tripKey");

        //עוזר לשליפת נתוני טיול מ-Firebase
        FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
        fireBaseTripHelper.fetchOneTrip(
                new FireBaseTripHelper.DataStatusT() {
            /**
             * פעולה זו מופעלת כאשר נתוני הטיול התקבלו מהFirebase.
             * היא מציגה את הנתונים במסך, ומאתחלת את רשימת המקטעים.
             *
             * @param t אובייקט הטיול שהתקבל
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataLoaded(Trip t) {
                trip = t;

                title.setText(trip.getNameOfTrip());
                writer.setText(trip.getUserName());
                age.setText(trip.getAge());
                area.setText(trip.getArea());
                place.setText(trip.getPlace());
                length.setText(trip.getLengthInKm()+"");
                organization.setText(trip.getOrganization());
                goals.setText(trip.getGoals());
                equipments.setText(trip.getEquipments());

                partArrayList = trip.getPartsArr();
                partsListViewAdapter = new PartsListViewAdapter(
                        View_trip.this,
                        0,
                        0,
                        partArrayList);
                parts.setAdapter(partsListViewAdapter);
            }

        },tripKey);

    }

    /**
     * טיפול בלחיצות על כפתורים: סגירה או הצגת תמונה.
     *
     * @param v הרכיב שנלחץ
     */
    @Override
    public void onClick(View v) {
        if (v == exit){
            finish();
        }
        else if (v == pictureBTN) {
            String image = trip.getPicture();

            Dialog dialog = new Dialog( View_trip.this);
            dialog.setContentView(R.layout.trip_pic_dialog_layout);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            ImageView tripPic = dialog.findViewById(R.id.pic);
            TextView noPic = dialog.findViewById(R.id.noPic);
            Button close = dialog.findViewById(R.id.closeBtn);
            close.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if(image != null || !image.equals(BitmapHelper.bitmapToString(BitmapFactory.
                    decodeResource(
                            getResources(),
                            R.drawable.add_image)))){
                tripPic.setImageBitmap(
                        BitmapHelper.stringToBitmap(image));
                noPic.setVisibility(View.GONE);
            }
            else
                tripPic.setVisibility(View.GONE);
        }
    }
}