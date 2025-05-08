package com.example.guided.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Classes.Part;
import com.example.guided.Classes.Trip;
import com.example.guided.Classes.User;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Helpers.OperationsAndTripsHelper;
import com.example.guided.R;
import com.example.guided.RecyclerAdapters.RecyclerAdapterTrip;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * מחלקה זו אחראית על יצירת טיול חדש או עריכת טיול קיים.
 * מאפשרת הזנת פרטי טיול, הוספת מקטעים (Part), תמונה, גיל מתאים, אזור ועוד.
 * הנתונים נשמרים ב-Firebase.
 */
public class Add_trip extends BaseActivity implements View.OnClickListener {
    private EditText topic;//שם הטיול
    private TextView length;//שדה המציג את האורך הכולל של הטיול (בק"מ)
    private double lengthCount = 0; // משתנה הסופר את אורך מסלול הטיול הכולל
    private int timeCount = 0; // משתנה הסופר את אורך הטיול הכולל(בדקות)
    private Switch privateORpublic;  // Switch לבחירת טיול פרטי או ציבורי
    private EditText goals;// שדה להזנת מטרות הטיול
    private EditText equipments;// שדה להזנת עזרים לטיול
    private EditText place; //מיקום מדוייק
    private TextView area;//שדה לבחירת אזור בארץ
    private ImageView tripPicture;// תמונה של המסלול
    private TextView ageAdjustments;//שדה לבחירת גיל החניכים

    /** שדות לדיאלוג הוספת מקטע להזנת פרטי המקטע */
    private Button addPartBTN; //כפתור להוספת מקטע חדש
    private TextView activityType; // שדה לבחירת סוג הפעילות שנעשת במקטע
    private EditText lengthInMinute;//אורך המקטע בדקות
    private EditText lengthInKM; //אורך המקטע בקמ
    private EditText description;//תוכן המקטע
    private EditText equipment;//עזרים המקטע
    private ImageView picture; // תמונה של המקטע
    private ArrayList<Part> partsArr; //רשימת המקטעים בטיול
    private int id = 0; // מזהה ייחודי לכל מקטע

    private RecyclerView recyclerView;//תצוגת RecyclerView להצגת המקטעים
    private RecyclerAdapterTrip recyclerAdapter; //אדפטר לרשימת המקטעים
    private Dialog addNewPartDialog; //דיאלוג להזנת מקטע חדש

    private ActivityResultLauncher<Intent> cameraLauncher; //לאונצ'ר להפעלת מצלמה
    private ActivityResultLauncher<Intent> galleryLauncher; // לאונצ'ר לפתיחת גלריה
    private int selectedPartPosition = -1;  // נשתמש כדי לדעת עבור איזה פריט לבחור תמונה
    public ImageView currentDialogImageView; //שומר את ImageView הנוכחי לדיאלוג תמונה.

    private Button savePartBTN; // כפתור לשמירת מקטע מהדיאלוג
    private ImageButton exitBTN; //כפתור יציאה
    private Button saveTrip; //כפתור שמירת טיול

    private String tripKey = ""; //מפתח הטיול (אם מדובר בעריכה)
    private Trip trip;//האובייקט של טיול עצמו

    private OperationsAndTripsHelper operationsAndTripsHelper; //אובייקט עזר לדיאלוגים ופעולות הקשורות לטיולים ולפעולות.

    /**
     * מופעל בעת יצירת הטיול.
     * מאתחל את כל רכיבי הממשק, מציב מאזינים, ואם מתקבל מזהה של טיול (tripKey), הטיול נטען מפיירבייס ומוצג .
     *
     * @param savedInstanceState מצב שמור של האקטיביטי
     */
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
        privateORpublic = findViewById(R.id.publicORpivate);
        tripPicture = findViewById(R.id.picture);
        area = findViewById(R.id.area);
        ageAdjustments = findViewById(R.id.age);

        exitBTN = findViewById(R.id.exit);
        saveTrip = findViewById(R.id.save);
        addPartBTN= findViewById(R.id.addPart);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        partsArr = new ArrayList<Part>();

        operationsAndTripsHelper = new OperationsAndTripsHelper(Add_trip.this);

        setAdapter();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        privateORpublic.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked) {
                if(!privateORpublic.isChecked()){
                    privateORpublic.setThumbDrawable(
                            ContextCompat.
                                    getDrawable(
                                            Add_trip.this,
                                            R.drawable.baseline_person_24));
                }
                else {
                    privateORpublic.setThumbDrawable(
                            ContextCompat.getDrawable(
                                    Add_trip.this,
                                    R.drawable.baseline_groups_24));
                }
            }
        } );

        tripPicture.setOnClickListener(this);
        area.setOnClickListener(this);
        ageAdjustments.setOnClickListener(this);
        exitBTN.setOnClickListener(this);
        saveTrip.setOnClickListener(this);
        addPartBTN.setOnClickListener(this);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK &&
                                currentDialogImageView != null) {
                            Intent data = o.getData();
                            if (data != null) {
                                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                                currentDialogImageView.setImageBitmap(bitmap);
                            }
                        }
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK &&
                                currentDialogImageView != null) {
                            Intent data = o.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                try {
                                    Bitmap bitmap = MediaStore.
                                            Images.
                                            Media.
                                            getBitmap(
                                                    Add_trip.this.getContentResolver(),
                                                    imageUri);
                                    currentDialogImageView.setImageBitmap(bitmap);
                                }
                                catch (IOException e) {
                                    throw new RuntimeException(e); //מדפיס פרטי שגיאה
                                }
                            }
                        }
                    }
                }
        );

        Intent intent = getIntent();
        if (intent != null) {
            tripKey = intent.getStringExtra("tripKey");
            if (tripKey != null) {

                FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
                fireBaseTripHelper.fetchOneTrip(
                        new FireBaseTripHelper.DataStatusT() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataLoaded(Trip t) {
                        trip = t;

                        topic.setText(trip.getNameOfTrip());
                        ageAdjustments.setText(trip.getAge());
                        lengthCount = lengthCount + trip.getLengthInKm();
                        length.setText(lengthCount + " ק''מ ");
                        timeCount = timeCount + trip.getLengthInMinutes();
                        goals.setText(trip.getGoals());
                        equipments.setText(trip.getEquipments());
                        area.setText(trip.getArea());
                        place.setText(trip.getNameOfTrip());

                        tripPicture.setImageBitmap(
                                BitmapHelper.
                                        stringToBitmap(
                                                trip.getPicture()));
                        if (trip.getPublicORprivate().equals("isPublic")) {
                            privateORpublic.setChecked(true);
                            privateORpublic.setThumbDrawable(
                                    ContextCompat.getDrawable(
                                            Add_trip.this,
                                            R.drawable.baseline_groups_24));
                        }
                        else if (trip.getPublicORprivate().equals("isPrivate")) {
                            privateORpublic.setThumbDrawable(
                                    ContextCompat.getDrawable(
                                            Add_trip.this,
                                            R.drawable.baseline_person_24));
                        }
                        partsArr = trip.getPartsArr();
                        id = partsArr.size();
                        setAdapter();
                    }
                }, tripKey);
            }
        }
    }

    /**
     * מאזין לכל הלחצנים במסך.
     * מבצע פעולה שונה בהתאם ללחצן שנלחץ: הוספת מקטע, שמירת מקטע, שמירת טיול, דיאלוג יציאה עם או בלי שמירה, ופתיחת דיאלוג הוספת תמונה, בחירת התאמת גיל או אזור.
     *
     * @param v רכיב ה-View שנלחץ
     */
    @Override
    public void onClick(View v) {
        if(v == addPartBTN){
            addPart();
        }
        else if (v == savePartBTN) {
            savePart();
        }
        else if(v == saveTrip){
            saveTrip();
            finish();
        }
        else if (v == exitBTN){
             operationsAndTripsHelper
                    .showExitDialog(
                            new OperationsAndTripsHelper.ExitDialogCallback() {
                                @Override
                                public void onResult(boolean exitAndSave) {
                                    if (exitAndSave)
                                        saveTrip();
                                    finish();
                                }
                            });
        }

        else if (v == tripPicture){
            currentDialogImageView = tripPicture;
            operationsAndTripsHelper.showTripPic(
                    cameraLauncher,
                    galleryLauncher,
                    new OperationsAndTripsHelper.PicDialogCallback() {
                @Override
                public void onResult(ImageView pic) {
                    tripPicture = pic;
                }
            });
        }
        else if(v == ageAdjustments){
            operationsAndTripsHelper
                    .showAgeAdjustmentsDialog(
                            new OperationsAndTripsHelper.AgeDialogCallback() {
                @Override
                public void onResult(String age) {
                    ageAdjustments.setText(age);
                }
            });
        }
        else if(v == area){
           operationsAndTripsHelper
                   .showAreaDialog(
                           new OperationsAndTripsHelper.AreaDialogCallback() {
               @Override
               public void onResult(String a) {
                   area.setText(a);
               }
           });
        }
    }

    /**
     * מציג דיאלוג המאפשר למשתמש להזין פרטי מקטע חדש.
     *כולל בחירת סוג פעילות, אורך, תיאור, ציוד ותמונה
     */
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
        currentDialogImageView = picture;

        picture.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDialogImageView = picture;
                operationsAndTripsHelper.showTripPic(
                        cameraLauncher,
                        galleryLauncher,
                        new OperationsAndTripsHelper.PicDialogCallback() {
                    @Override
                    public void onResult(ImageView pic) {
                        picture = pic;
                    }
                });
            }
        });

        activityType.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                operationsAndTripsHelper
                        .showActivityDialog(
                                new OperationsAndTripsHelper.ActivityTypeDialogCallback() {
                                    @Override
                                    public void onResult(String type) {
                                        activityType.setText(type);
                                    }
                                });
            }
        });
        savePartBTN = addNewPartDialog.findViewById(R.id.savePart);
        savePartBTN.setOnClickListener(this);

        addNewPartDialog.show();
    }

    /**
     * שומר את המקטע החדש שהוזן בדיאלוג, ומעדכן את רשימת המקטעים.
     * מחשב מחדש את האורך(בק"מ) והזמן(בדקות) הכולל של הפעולה.
     *       אם שדה כלשהו חסר – מציג הודעת שגיאה.
     */
    @SuppressLint("SetTextI18n")
    public void savePart(){

        int partLengthInMinuteInt = 0;
        if(!lengthInMinute.getText().toString().isEmpty()){
            partLengthInMinuteInt = Integer.parseInt(lengthInMinute.getText().toString());
        }
        int partLengthInKMInt = 0;
        if(!lengthInKM.getText().toString().isEmpty()){
            partLengthInKMInt = Integer.parseInt(lengthInKM.getText().toString());
        }
        String activityTypeStr = "";
        if(!activityType.getText().toString().isEmpty()){
            activityTypeStr = activityType.getText().toString();

        }
        String descriptionStr = "";
        if(!description.getText().toString().isEmpty()){
            descriptionStr = description.getText().toString();
        }
        String equipmentStr = "";
        if(!equipment.getText().toString().isEmpty()){
            equipmentStr = equipment.getText().toString();
        }

        String pictureSTR;
        pictureSTR = BitmapHelper.
                bitmapToString(
                        BitmapFactory.
                                decodeResource(
                                        getResources(),
                                        R.drawable.add_image));
        if(!BitmapHelper.bitmapToString(
                ((BitmapDrawable)picture.getDrawable())
                        .getBitmap()).isEmpty()){
            pictureSTR = BitmapHelper.bitmapToString(
                    ((BitmapDrawable)picture.getDrawable())
                            .getBitmap());
        }

        if(pictureSTR.isEmpty()||
                equipmentStr.isEmpty()||
                descriptionStr.isEmpty()||
                activityTypeStr.isEmpty()||
                partLengthInKMInt == 0||
                partLengthInMinuteInt == 0)
            Toast.makeText(this, "לא כל השדות מלאים!", Toast.LENGTH_LONG).show();

        Part newPart = new Part(activityTypeStr, partLengthInMinuteInt, partLengthInKMInt, descriptionStr, equipmentStr, pictureSTR, id);
        partsArr.add(newPart);
        id++;
        recyclerAdapter.notifyDataSetChanged();

        lengthCount = lengthCount + partLengthInKMInt;
        length.setText(lengthCount + " ק''מ ");

        timeCount = timeCount + partLengthInMinuteInt;
        addNewPartDialog.dismiss();
    }

    /**
     * שומר את הטיול כולו למסד הנתונים Firebase.
     * מאחזר פרטי משתמש כדי לצרף אותם לטיול לפני השמירה.
     */
    public void saveTrip(){

        String nameSTR = topic.getText().toString();
        String ageSTR = ageAdjustments.getText().toString();
        String publicORprivateSRT;
        if (!privateORpublic.isChecked())
            publicORprivateSRT = "isPrivate";
        else
            publicORprivateSRT = "isPublic";
        double lengthInKmINT = lengthCount;
        int lengthInMinutesINT =  timeCount;
        String goalsSTR = goals.getText().toString();
        String equipmentsSTR = equipments.getText().toString();
        String areaSTR = area.getText().toString();
        String placeSTR = place.getText().toString();
        String picSTR = BitmapHelper.bitmapToString(
                ((BitmapDrawable)tripPicture.getDrawable())
                        .getBitmap());

        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.fetchUserData(
                new FirebaseUserHelper.UserDataCallback() {
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
                                organizationSTR,
                                picSTR);
                        operationsAndTripsHelper.saveTrip(
                                trip,
                                tripKey);
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    /**
     * ממשק גרירה והחלקה של פריטים ב-RecyclerView.
     * מאפשר הזזת מקטעים וסידור מחדש, או מחיקה עם אפשרות ביטול.
     *   onMove - מאפשר גרירת חלקים כדי לשנות את הסדר שלהם.
     *       onSwiped - מאפשר מחיקת חלק מהטיול על ידי החלקה שמאלה, עם אפשרות לשחזור באמצעות Snackbar.
     *       onChildDraw - מצייר רקע אדום ואייקון פח בעת גרירת פריט שמאלה.
     */
    Part deletedPart = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper
            .SimpleCallback(
                    ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(
                @NonNull RecyclerView recyclerView,
                @NonNull RecyclerView.ViewHolder viewHolder,
                @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(partsArr, fromPosition, toPosition);
            recyclerAdapter.notifyItemMoved(fromPosition,toPosition);

            return true;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onSwiped(
                @NonNull RecyclerView.ViewHolder viewHolder,
                int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                deletedPart = partsArr.get(position);
                partsArr.remove(position);
                recyclerAdapter.notifyItemRemoved(position);

                lengthCount = lengthCount - deletedPart.getLengthInKM();
                ;
                length.setText(lengthCount + " ק''מ ");

                Snackbar.make(
                        recyclerView,
                        deletedPart.toString(),
                        Snackbar.LENGTH_LONG).setAction(
                        "undo",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                partsArr.add(position, deletedPart);
                                recyclerAdapter.notifyItemInserted(position);

                                lengthCount = lengthCount + deletedPart.getLengthInKM();
                                ;
                                length.setText(lengthCount + " ק''מ ");
                            }
                        }).show();
            }
        }

        @Override
        public void onChildDraw(
                @NonNull Canvas c,
                @NonNull RecyclerView recyclerView,
                @NonNull RecyclerView.ViewHolder viewHolder,
                float dX,
                float dY,
                int actionState,
                boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                    Add_trip.this,
                                    R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.trash)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    /**
     * אתחול ה־RecyclerView עם מתאם מתאים (RecyclerAdapterTrip) והגדרת שני מאזינים:
     * 1. onPartListChanged – מחשב מחדש את סך כל הקילומטרים והזמן כשיש שינוי ברשימת החלקים.
     * 2. onImagePickerRequested – מאזין לבקשות בחירת תמונה לחלק (מצלמה, גלריה, או מחיקה).
     */
    private void setAdapter(){
        recyclerAdapter = new RecyclerAdapterTrip(
                partsArr,
                Add_trip.this);
        recyclerAdapter.setOnPartListChangedListener(
                new RecyclerAdapterTrip.OnPartListChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPartListChanged(ArrayList<Part> parts) {
                lengthCount = 0;
                timeCount = 0;
                for (Part p : parts) {
                    lengthCount += p.getLengthInKM();
                    timeCount += p.getLengthInMinute();
                }
                length.setText(lengthCount + " ק''מ ");
            }
        });
        recyclerAdapter.setOnImagePickerRequestedListener(
                new RecyclerAdapterTrip.OnImagePickerRequestedListener() {
            @Override
            public void onCameraRequested(
                    int position,
                    ImageView imageView) {
                // שמרי את המיקום, ואז הפעלי את cameraLauncher
                selectedPartPosition = position;
                currentDialogImageView = imageView;
                cameraLauncher.launch(
                        new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE));
            }
            @Override
            public void onGalleryRequested(
                    int position,
                    ImageView imageView) {
                selectedPartPosition = position;
                currentDialogImageView = imageView;
                galleryLauncher.launch(
                        new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            }
            @Override
            public void onDeleteRequested(
                    int position,
                    ImageView imageView) {
                selectedPartPosition = position;
                currentDialogImageView = imageView;
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }
}