package com.example.guided;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Add_trip extends AppCompatActivity implements View.OnClickListener {
    // TODO: להוסיף אפשרות לשמור מקטע וטיול בכללי שלא כל השדות מלאים מבלי שיקרוס


    EditText topic;//שם הטיול
    TextView length;//אורך הטיול
    int lengthCount = 0;
    Switch privateORpublic; //פרטי או ציבורי
    EditText goals;//מטרות הטיול
    EditText equipments;// עזרים לטיול
    EditText place; //מיקום מדוייק
    TextView area;//אזור בארץ
    String[] listArea;
    boolean[] checkedArea;
    ArrayList<Integer> userArea = new ArrayList<>();

    ImageView tripPicture;// תמונה של המסלול
    TextView ageAdjustments;//גיל החניכים
    String[] listAgeAdjustments;
    boolean[] checkedAgeAdjustments;
    ArrayList<Integer> userAgeAdjustments = new ArrayList<>();

    Button addPartBTN;
    TextView activityType;
    EditText lengthInMinute;
    EditText lengthInKM;
    EditText description;
    EditText equipment;
    ImageView picture;

    ArrayList<Part> partsArr;
    int id = 0;

    RecyclerView recyclerView;
    RecyclerAdapterTrip recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    Dialog addNewPartDialog;
    Button savePartBTN;
    ImageButton exitBTN;

    Trip trip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        topic = findViewById(R.id.topic);
        length = findViewById(R.id.length);
        goals = findViewById(R.id.goals);
        equipments = findViewById(R.id.equipments);
        place = findViewById(R.id.place);
        //לעשות שהמיקום הספציפי מתחבר לגוגל מאפ

        tripPicture = findViewById(R.id.picture);
        tripPicture.setOnClickListener(this);


        privateORpublic = findViewById(R.id.publicORpivate);

        privateORpublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!privateORpublic.isChecked()){
                    privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_trip.this,
                            R.drawable.baseline_person_24));
                }
                else {
                    privateORpublic.setThumbDrawable(ContextCompat.getDrawable(Add_trip.this,
                            R.drawable.baseline_groups_24));
                }
            }
        } );


        exitBTN = findViewById(R.id.exit);
        exitBTN.setOnClickListener(this);


        addPartBTN= findViewById(R.id.addPart);
        addPartBTN.setOnClickListener(this);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        partsArr = new ArrayList<Part>();

        recyclerAdapter = new RecyclerAdapterTrip(partsArr, Add_trip.this);
        recyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        area = findViewById(R.id.area);
        listArea = getResources().getStringArray(R.array.area_adjustment);
        checkedArea = new boolean[listArea.length];
        area.setOnClickListener(this);


        ageAdjustments = findViewById(R.id.age);
        listAgeAdjustments = getResources().getStringArray(R.array.age_adjustment);
        checkedAgeAdjustments = new boolean[listAgeAdjustments.length];
        ageAdjustments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == addPartBTN){
            addPart();
        }
        else if (v == savePartBTN) {
            savePart();
        }
        else if (v == exitBTN){
            saveTrip();
            finish();
        }

        else if (v == tripPicture){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("העלאת מסלול");
            builder.setMessage("תרצו להעלות תמונה מהגלריה או לצלם תמונה במצלמה?");
            builder.setCancelable(true);
            builder.setPositiveButton("מצלמה", new Add_trip.HandleAlartDialogLostener());
            builder.setNegativeButton("גלריה", new Add_trip.HandleAlartDialogLostener());
            AlertDialog dialog = builder.create();
            dialog.show();

            //לשים את התמונה של המקטע בחלק הנכון
        }
        else if(v == ageAdjustments){

                AlertDialog.Builder builder = new AlertDialog.Builder(Add_trip.this);
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
        else if(v == area){
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_trip.this);
            builder.setTitle("בחר את אזור הטיול: ");
            builder.setMultiChoiceItems(listArea, checkedArea, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        if(! userArea.contains(which)){
                            userArea.add(which);
                        }
                    }
                    else if (userArea.contains(which)){
                        userArea.remove(Integer.valueOf(which));
                    }
                }
            });
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String a = "";
                    for(int i = 0; i < userArea.size(); i++){
                        a = a + listArea[userArea.get(i)];
                        if (i != userArea.size() - 1){
                            a = a + ", ";
                        }
                    }
                    area.setText(a);
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
    }

    private void addPart(){

        addNewPartDialog = new Dialog(Add_trip.this);
        addNewPartDialog.setContentView(R.layout.part_layout);
        addNewPartDialog.setCancelable(true);

        activityType = addNewPartDialog.findViewById(R.id.activity);
        lengthInMinute = addNewPartDialog.findViewById(R.id.lengthInMinutes);
        lengthInKM = addNewPartDialog.findViewById(R.id.lengthInKM);
        picture = addNewPartDialog.findViewById(R.id.picture);
        description = addNewPartDialog.findViewById(R.id.description);
        equipment = addNewPartDialog.findViewById(R.id.equipment);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_trip.this);
                builder.setTitle("העלאת מסלול");
                builder.setMessage("תרצו להעלות תמונה מהגלריה או לצלם תמונה במצלמה?");
                builder.setCancelable(true);
                builder.setPositiveButton("מצלמה", new Add_trip.HandleAlartDialogLostener2());
                builder.setNegativeButton("גלריה", new Add_trip.HandleAlartDialogLostener2());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        String[] listActivityTypeAdjustments = getResources().getStringArray(R.array.activity_type_adjustment);;
        boolean[] checkedActivityTypeAdjustments = new boolean[listActivityTypeAdjustments.length];;
        ArrayList<Integer> userActivityTypeAdjustments = new ArrayList<>();
        activityType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_trip.this);
                builder.setTitle("בחר את סוג הפעילות ");
                builder.setMultiChoiceItems(listActivityTypeAdjustments, checkedActivityTypeAdjustments, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(! userActivityTypeAdjustments.contains(which)){
                                userActivityTypeAdjustments.add(which);
                                if (listActivityTypeAdjustments.equals("אחר")){
                                    //לתת אפשרות לכתוב משהו שלא מופיע כאחד מהאופציות
                                }
                            }

                        }
                        else if (userActivityTypeAdjustments.contains(which)){
                            userActivityTypeAdjustments.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type = "";
                        for(int i = 0; i < userActivityTypeAdjustments.size(); i++){
                            type = type + listActivityTypeAdjustments[userActivityTypeAdjustments.get(i)];
                            if (i != userActivityTypeAdjustments.size() - 1){
                                type = type + ", ";
                            }
                        }
                        activityType.setText(type);
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

        savePartBTN = addNewPartDialog.findViewById(R.id.savePart);
        savePartBTN.setOnClickListener(this);

        addNewPartDialog.show();

    }
    public void savePart(){

        int partLengthInMinuteInt = Integer.parseInt(lengthInMinute.getText().toString());
        int partLengthInKMInt = Integer.parseInt(lengthInKM.getText().toString());
        String activityTypeStr = activityType.getText().toString();
        String descriptionStr = description.getText().toString();
        String equipmentStr = equipment.getText().toString();
        String pictureSTR = BitmapHelper.bitmapToString(
                ((BitmapDrawable)picture.getDrawable())
                        .getBitmap());
        Part newPart = new Part(activityTypeStr, partLengthInMinuteInt, partLengthInKMInt, descriptionStr, equipmentStr, pictureSTR, id);

        partsArr.add(newPart);
        id++;
        recyclerAdapter.notifyDataSetChanged();

        lengthCount = lengthCount + partLengthInKMInt;
        length.setText(lengthCount + " קמ ");


        addNewPartDialog.dismiss();
    }

    Part deletedPart = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(partsArr, fromPosition, toPosition);
            recyclerAdapter.notifyItemMoved(fromPosition,toPosition);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedPart = partsArr.get(position);
                    partsArr.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    lengthCount = lengthCount - deletedPart.getLengthInKM();;
                    length.setText(lengthCount + " קמ ");

                    Snackbar.make(recyclerView, deletedPart.toString(),Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            partsArr.add(position, deletedPart);
                            recyclerAdapter.notifyItemInserted(position);

                            lengthCount = lengthCount + deletedPart.getLengthInKM();;
                            length.setText(lengthCount + " קמ ");
                        }
                    }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Add_trip.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public class HandleAlartDialogLostener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //camera
            if (which == -1) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            }
            //gallery
            else if (which == -2) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "picture"),
                        1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //get image from camera to trip
        if(requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                tripPicture.setImageBitmap(bitmap);
            }
        }
        //get image from gallery to part
        else if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                tripPicture.setImageBitmap(bitmap);
            }
        }

        if(requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap);
            }
        }
        //get image from gallery to part
        else if(requestCode == 4) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap);
            }
        }


    }

    public class HandleAlartDialogLostener2 implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //camera
            if (which == -1) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 3);

            }
            //gallery
            else if (which == -2) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "picture"),
                        3);
            }
        }
    }

    public void saveTrip(){

        String nameSTR = topic.getText().toString();
        String ageSTR = ageAdjustments.getText().toString();
        String publicORprivateSRT;
        if (!privateORpublic.isChecked())
            publicORprivateSRT = "isPrivate";
        else
            publicORprivateSRT = "isPublic";
        int lengthInKmINT = lengthCount;
        int lengthInMinutesINT =  Integer.parseInt(lengthInMinute.getText().toString());
        String goalsSTR = goals.getText().toString();
        String equipmentsSTR = equipments.getText().toString();
        String areaSTR = area.getText().toString();
        String placeSTR = place.getText().toString();
        String picSTR = BitmapHelper.bitmapToString(
                ((BitmapDrawable)tripPicture.getDrawable())
                        .getBitmap());
        
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("trips");

        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
            @Override
            public void onUserDataLoaded(User user) {
                String organizationSTR = user.getOrganization();
                String userNameSTR = user.getUserName();
                trip = new Trip(nameSTR,
                        ageSTR,
                        publicORprivateSRT,
                        lengthInKmINT,
                        lengthInMinutesINT,
                        goalsSTR,
                        equipmentsSTR,
                        areaSTR,
                        placeSTR,
                        partsArr,
                        userNameSTR,
                        organizationSTR, picSTR);
                String key = myRef.push().getKey();
                trip.setKey(key);
                myRef.child(key).setValue(trip);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


    }

}