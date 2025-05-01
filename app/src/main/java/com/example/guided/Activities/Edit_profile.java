package com.example.guided.Activities;

import static com.example.guided.Helpers.RegisteretionHelper.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.Operation;
import com.example.guided.Classes.Trip;
import com.example.guided.Classes.User;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.DateConverter;
import com.example.guided.Helpers.FireBaseOperationHelper;
import com.example.guided.Helpers.FireBaseTripHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.Helpers.RegisteretionHelper;
import com.example.guided.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;


public class Edit_profile extends BaseActivity implements View.OnClickListener {
    // רכיבי תצוגה
    private EditText userName;     // שדה להזנת שם המשתמש החדש
    private TextView organization;     // מציג את תנועת הנוער שנבחרה
    private EditText birthday;     // תאריך הלידה של המשתמש
    private EditText nickName;    // שדה להזנת כינוי המשתמש
    private ImageView profile;    // תמונת הפרופיל של המשתמש
    private ImageView saveBTN;    // כפתור שמירת פרופיל
    private ImageButton exitBTN;    // כפתור יציאה מהמסך
    private TextView alartForUserName;    // מציג התראות לשם המשתמש
    private TextView alartForNickName;    // מציג התראות לכינוי
    private TextView alartForOrganization;    // מציג התראות למסגרת
    private TextView alartForBirthday;    // מציג התראות לתאריך הלידה

    private DatabaseReference userRef;     // הפנייה למסד הנתונים של המשתמשים
    private String oldUsername;     // שם המשתמש הישן (לפני עריכה)
    private String oldPicture; // תמונת הפרופיל הישנה(לפני עריכה)

    // רשימות פעולות וטיולים לשם עדכון שם המשתמש בהן
    private ArrayList<Trip> tripsArrayList;
    private ArrayList<Operation> operationArrayList;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRef = FirebaseDatabase.
                getInstance().
                getReference("users");         // הפנייה למסד הנתונים של המשתמשים


        alartForUserName=findViewById(R.id.alartUserName);
        alartForOrganization = findViewById(R.id.alartOrganization);
        alartForBirthday = findViewById(R.id.alartBirthday);
        alartForNickName = findViewById(R.id.alartNickName);

        userName = findViewById(R.id.userName);
        nickName = findViewById(R.id.nickName);
        profile = findViewById(R.id.profile);
        birthday = findViewById(R.id.birthday);
        organization = findViewById(R.id.organization);
        saveBTN = findViewById(R.id.save);
        exitBTN = findViewById(R.id.exit);

        // מאזינים לאירועים
        birthday.setOnClickListener(this);
        profile.setOnClickListener(this);
        saveBTN.setOnClickListener(this);
        exitBTN.setOnClickListener(this);
        organization.setOnClickListener(this);

        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.
                fetchUserData(
                        new FirebaseUserHelper.
                                UserDataCallback() {
            @Override
            public void onUserDataLoaded(User u) {
                organization.setText(u.getOrganization());
                birthday.setText(
                        DateConverter.convertFullFormatToDate(
                                u.getBirthday().
                                        toString()));
                userName.setText(u.getUserName());
                oldUsername = u.getUserName();
                nickName.setText(u.getNickName());
                profile.setImageBitmap(BitmapHelper.stringToBitmap(u.getProfileImage()));
                oldPicture = u.getProfileImage();
                updatedUser.setEmail(u.getEmail());
                updatedUser.setPassword(u.getPassword());
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {

                            if (o.getResultCode() == RESULT_OK) {
                                Intent data = o.getData();
                                if (data != null) {
                                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                                    profile.setImageBitmap(bitmap);
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

                            if (o.getResultCode() == RESULT_OK) {
                                Intent data = o.getData();
                                if (data != null) {
                                    Uri imageUri = data.getData();
                                    try {
                                        Bitmap bitmap = MediaStore.
                                                Images.
                                                Media.
                                                getBitmap(
                                                        Edit_profile.this.getContentResolver(),
                                                        imageUri);
                                        profile.setImageBitmap(bitmap);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e); //מדפיס פרטי שגיאה
                                    }
                                }
                            }

                    }
                }
        );

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        alartForUserName.setText("");
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        if (v == birthday){
            RegisteretionHelper.setBirthdate(Edit_profile.this, new BirthdayCallback() {
                @Override
                public void onBirthdaySelected(String b) {
                    birthday.setText(b);
                }
            });

        }

        if (v == organization){
            RegisteretionHelper.setOrganization(Edit_profile.this, new OrganizationCallback(){
                @Override
                public void onOrganizationSelected(String o) {
                    organization.setText(o);
                }
            });
        }

        if(v == profile){
            RegisteretionHelper.setPic(Edit_profile.this, cameraLauncher, galleryLauncher);
        }

        if (v == saveBTN){
            alartForNickName.setText(RegisteretionHelper.checkAlertForNickName(nickName.getText().toString()));
            alartForBirthday.setText(RegisteretionHelper.checkAlertsForBirthday(birthday.getText().toString()));
            alartForOrganization.setText(RegisteretionHelper.checkAlertsForOrganization(organization.getText().toString()));
            alartForUserName.setText(checkAlertsForUserName(userName.getText().toString()));

            final boolean[] x = {false};
            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUsers(new FirebaseUserHelper.DataStatus() {
                @Override
                public void onDataLoaded(ArrayList<User> users) {
                    for(User user : users){
                        if (user.getUserName().equals(userName.getText().toString())) {
                            // Username is already taken
                            x[0] = true;
                        }
                    }
                    if(!x[0] || oldUsername.equals(userName.getText().toString())) {
                        //final checks of creating a new user at the database
                        if (checkUserName(userName.getText().toString()) &&
                                checkNickName(nickName.getText().toString()) &&
                                checkBirthday(birthday.getText().toString()) &&
                                RegisteretionHelper.checkPic(((BitmapDrawable)profile.getDrawable()).getBitmap())) {
                            updatedUser.setUserName(userName.getText().toString());
                            updatedUser.setNickName(nickName.getText().toString());
                            updatedUser.setOrganization(organization.getText().toString());
                            updatedUser.setBirthday(DateConverter.convertStringToDate(birthday.getText().toString()));
                            if(oldPicture.equals(BitmapHelper.bitmapToString(
                                    ((BitmapDrawable) profile.getDrawable())
                                            .getBitmap())))
                                updatedUser.setProfileImage(oldPicture);
                            else {
                                updatedUser.setProfileImage(BitmapHelper.bitmapToString(
                                        ((BitmapDrawable) profile.getDrawable())
                                                .getBitmap()));
                            }
                            setUser();
                            finish();
                        }
                    }
                    else{
                        // Username is in the dataBase
                        alartForUserName.setText("* שם המשתמש שבחרת תפוס בחר שם משתמש אחר " +
                                '\n' +
                                alartForUserName.getText().toString());
                    }
                }
            });
            //TODO: לבדוק איך לעשות שזה יעשה ריפרש ויראה ישר את הפרטים החדשים
        }

        if (v == exitBTN){
            finish();
        }
    }

    private void setUser() {

        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(updatedUser).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Edit_profile.this, "User Registered", Toast.LENGTH_LONG).show();
                           // progressBar.setVisibility(View.GONE);
                            if(!updatedUser.getUserName().equals(oldUsername)){
                            FireBaseTripHelper fireBaseTripHelper = new FireBaseTripHelper();
                            fireBaseTripHelper.fetchMyTrips(
                                    new FireBaseTripHelper.DataStatus()
                                    {

                                        @Override
                                        public void onDataLoaded(ArrayList<Trip> trips) {
                                            tripsArrayList = trips;
                                            for(Trip trip: tripsArrayList){
                                                if(trip.getUserName().equals(oldUsername)){
                                                    String tripId = trip.getKey();
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRef = database.getReference("trips");
                                                    myRef.child(tripId).child("userName").setValue(updatedUser.getUserName());
                                                }
                                            }

                                            FireBaseOperationHelper fireBaseOperationHelper = new FireBaseOperationHelper();
                                            fireBaseOperationHelper.fetchMyOperations(
                                                    new FireBaseOperationHelper.DataStatus()
                                                    {
                                                        @Override
                                                        public void onDataLoaded(ArrayList<Operation> operations) {
                                                            operationArrayList = operations;
                                                            for(Operation operation: operationArrayList) {
                                                                if (operation.getUserName().equals(oldUsername)) {
                                                                    String tripId = operation.getKey();
                                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                    DatabaseReference myRef = database.getReference("operations");
                                                                    myRef.child(tripId).child("userName").setValue(updatedUser.getUserName());
                                                                }
                                                            }
                                                        }
                                                    }, oldUsername);
                                        }
                                    }, oldUsername);
                        }
                    }
                    }
                });

    }
}