package com.example.guided;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_operation extends BaseActivity implements View.OnClickListener {
    TextView title;
    TextView writer;
    TextView age;
    TextView length;
    TextView organization;
    TextView goals;
    TextView equipments;

    ImageButton exit;
    ListView metodot;
    MetodotListViewAdapter metodotListViewAdapter;
    FireBaseOperationHelper fireBaseOperationHelper;

    ArrayList<Metoda> metodaArrayList;
    Operation operation;


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
        fireBaseOperationHelper.fetchOneOperation(new FireBaseOperationHelper.DataStatusM() {
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
                metodotListViewAdapter = new MetodotListViewAdapter(View_operation.this, 0, 0, metodaArrayList);
                metodot.setAdapter(metodotListViewAdapter);
            }
        },operationKey);


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("operations").child(id);
//
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                operation = snapshot.getValue(Operation.class);
//
//                if (operation!= null){
//                    title.setText(operation.getNameOfOperation());
//                    writer.setText(operation.getUserName());
//                    age.setText(operation.getAge());
//                    length.setText(operation.getLengthOfOperation());
//                    organization.setText(operation.getOrganization());
//                    goals.setText(operation.getGoals());
//                    equipments.setText(operation.getEquipment());
//
//                    metodaArrayList = operation.getMetodotArr();
//                    metodotListViewAdapter = new MetodotListViewAdapter(View_operation.this, 0, 0, metodaArrayList);
//                    metodot.setAdapter(metodotListViewAdapter);
//
//

                    //הערכים לא נכנסים מהדאטא בייס למקום כי זה דאטאבייס חיצוני מריך לסדר את זה
//                }
//                metodaArrayList = new ArrayList<>();
//                for (DataSnapshot methodSnapshot : snapshot.child("metodotArr").getChildren()) {
//                    Metoda metoda = methodSnapshot.getValue(Metoda.class);
//                    if (metoda != null) {
//                        metodaArrayList.add(metoda);
//                    }
//                }
//                if (metodaArrayList != null) {
//                    metodotListViewAdapter = new MetodotListViewAdapter(View_operation.this, 0, 0, metodaArrayList);
//                    metodot.setAdapter(metodotListViewAdapter);
//                }
            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });





//    }

    @Override
    public void onClick(View v) {
        if (v == exit){
            finish();
        }

    }
}