package com.example.guided.Activities;

import static com.example.guided.Helpers.RegisteretionHelper.*;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

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


import java.util.ArrayList;


public class Edit_profile extends BaseActivity implements View.OnClickListener {
    EditText userName;
    TextView organization;
    EditText birthday;
    EditText nickName;
    ImageView profile;
    String[] listOrganizations;
    Dialog dialog;
    ImageView saveBTN;
    ImageButton exitBTN;
    TextView alartForUserName;
    TextView alartForNickName;
    TextView alartForOrganization;
    TextView alartForBirthday;
    DatabaseReference userRef;

    String oldUsername;
    String oldUserId;

    ArrayList<Trip> tripsArrayList;
    ArrayList<Operation> operationArrayList;

    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;
    //TODO: כשמשנית את השם משתמש צריך לשנות אותו גם בכל הפעולות וטיולים של המשתמש הזה!

    User updatedUser = new User();

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

        userRef = FirebaseDatabase.getInstance().getReference("users");


        alartForUserName=findViewById(R.id.alartUserName);
        alartForOrganization = findViewById(R.id.alartOrganization);
        alartForBirthday = findViewById(R.id.alartBirthday);
        alartForNickName = findViewById(R.id.alartNickName);

        userName = findViewById(R.id.userName);
        nickName = findViewById(R.id.nickName);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(this);

        birthday = findViewById(R.id.birthday);
        birthday.setOnClickListener(this);


        saveBTN = findViewById(R.id.save);
        saveBTN.setOnClickListener(this);

        exitBTN = findViewById(R.id.exit);
        exitBTN.setOnClickListener(this);

        organization = findViewById(R.id.organization);
        organization.setOnClickListener(this);

        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.fetchUserData(new FirebaseUserHelper.UserDataCallback() {
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

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        //TODO: ליצור מחלקה של הרשמה שיש לה פונקציות שמתפלות בכל מה שקשור לרישום/ התחברות ושאני פשוטל אקרע לה גם פה וגם בהרשמה ובהתחברת כדי שלא יהיה כפילות של קוד

        alartForUserName.setText("");
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        if (v == birthday){
            birthday.setText(RegisteretionHelper.setBirthdate(Edit_profile.this));

        }

        if (v == organization){
            organization.setText(RegisteretionHelper.setOrganization(Edit_profile.this));
        }

        if(v == profile){
            profile.setImageBitmap(RegisteretionHelper.setPic(Edit_profile.this));
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