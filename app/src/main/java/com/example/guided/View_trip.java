package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class View_trip extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    TextView writer;
    TextView age;
    TextView length;
    TextView organization;
    TextView goals;
    TextView equipments;
    TextView area;
    TextView place;
    Button pictureBTN;

    ImageButton exit;

    ListView parts;
    PartsListViewAdapter partsListViewAdapter;
    FireBaseTripHelper fireBaseTripHelper;

    ArrayList<Part> partArrayList;

    Trip trip;

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

        Intent intent = getIntent();
        String tripKey = intent.getStringExtra("tripKey");

        fireBaseTripHelper = new FireBaseTripHelper();
        fireBaseTripHelper.fetchOneTrip(new FireBaseTripHelper.DataStatusT() {
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
                partsListViewAdapter = new PartsListViewAdapter(View_trip.this, 0, 0, partArrayList);
                parts.setAdapter(partsListViewAdapter);
            }

        },tripKey);

    }

    @Override
    public void onClick(View v) {
        if (v == exit){
            finish();
        }
    }
}