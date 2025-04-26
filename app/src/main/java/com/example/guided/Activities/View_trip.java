package com.example.guided.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
                partsListViewAdapter = new PartsListViewAdapter(View_trip.this, 0, 0, partArrayList);
                parts.setAdapter(partsListViewAdapter);
            }

        },tripKey);

    }

    @Override
    public void onClick(View v) {
        if (v == exit){
            finish();
        } else if (v == pictureBTN) {
            String image = trip.getPicture();
            LayoutInflater inflater = LayoutInflater.from(this); // or getLayoutInflater() in an Activity
            View dialogView = inflater.inflate(R.layout.trip_pic_dialog_layout, null);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("מסלול הטיול");

            if(image != null){
                ImageView imageView = dialogView.findViewById(R.id.pic);
                imageView.setImageBitmap(BitmapHelper.stringToBitmap(image));
                builder.setView(dialogView);

            }
            else {
                builder.setMessage("לא הוסף מסלול");
            }
            builder.setPositiveButton("סגור", null);
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}