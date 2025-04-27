package com.example.guided.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.User;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.DateConverter;
import com.example.guided.Helpers.RegisteretionHelper;
import com.example.guided.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Register_num_two extends BaseActivity implements View.OnClickListener {
    TextView organization;
    EditText birthday;
    EditText nickName;
    ImageView profile;
    String[] listOrganizations;
    ImageView backBTN;
    ImageView saveBTN;
    TextView alartForNickName;
    TextView alartForOrganization;
    TextView alartForBirthday;
    TextView alartForProfile;
    User newUser;
    String userName;
    String password;
    String email;

    private FirebaseAuth mAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_num_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(this);

        nickName = findViewById(R.id.nickName);

        birthday = findViewById(R.id.birthday);
        birthday.setOnClickListener(this);

        backBTN = findViewById(R.id.back);
        backBTN.setOnClickListener(this);

        saveBTN = findViewById(R.id.save);
        saveBTN.setOnClickListener(this);

        organization = findViewById(R.id.organization);
        organization.setOnClickListener(this);
        //crating a list of youth organizations in israel
        listOrganizations = getResources().getStringArray(R.array.organization_adjustment);
        alartForOrganization = findViewById(R.id.alartOrganization);
        alartForBirthday = findViewById(R.id.alartBirthday);
        alartForNickName = findViewById(R.id.alartNickName);
        alartForProfile = findViewById(R.id.alartProfile);


        //set fields with data from User object that returned from the first register activity as an extra
        Intent intent = getIntent();
        Serializable user = intent.getSerializableExtra("newUser");
        if (user instanceof User){
            newUser = (User) user;
            organization.setText(newUser.getOrganization());
            Date currentDate = new Date();
            Date thisDate = newUser.getBirthday();
            if((thisDate.getDate() == currentDate.getDate()) &&
                    (thisDate.getMonth() == currentDate.getMonth()) &&
                    (thisDate.getYear() == currentDate.getYear())) {
                birthday.setText("");
            }
            else {
                birthday.setText(
                        DateConverter.convertFullFormatToDate(
                                newUser.getBirthday().
                                        toString()));
            }
            String profileImageStr = newUser.getProfileImage();
            if(!profileImageStr.isEmpty()){
                profile.setImageBitmap(
                    BitmapHelper.stringToBitmap(profileImageStr));
            }
            nickName.setText(newUser.getNickName());
            userName = newUser.getUserName();
            password = newUser.getPassword();
            email = newUser.getEmail();
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View v) {
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        // if user click on the profile image view
        if (v == profile) {
            profile.setImageBitmap(RegisteretionHelper.setPic(Register_num_two.this));
        }

        //if user click on the birthday edit text
        if (v == birthday){
            birthday.setText(RegisteretionHelper.setBirthdate(Register_num_two.this));
        }

        if (v == organization){
            organization.setText(RegisteretionHelper.setOrganization(Register_num_two.this));
        }

        // if you click on the 'finish' button
        if (v == saveBTN) {
            alartForNickName.setText(RegisteretionHelper.checkAlertForNickName(nickName.getText().toString()));
            alartForBirthday.setText(RegisteretionHelper.checkAlertsForBirthday(birthday.getText().toString()));
            alartForOrganization.setText(RegisteretionHelper.checkAlertsForOrganization(organization.getText().toString()));

            //final checks of creating a new user at the database
            if (RegisteretionHelper.checkOrganization(organization.getText().toString()) &&
                    RegisteretionHelper.checkBirthday(birthday.getText().toString()) &&
                    RegisteretionHelper.checkNickName(nickName.getText().toString()) &&
                    RegisteretionHelper.checkPic(((BitmapDrawable)profile.getDrawable()).getBitmap()))
            {

                progressBar.setVisibility(View.VISIBLE);
                saveUser();

                //open the home page after the user had been saved in the database
                Intent intent = new Intent(this, Home_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        // if you click on the 'back' button
        if(v == backBTN){

            //saving the inputs that the user entered at an User object
            newUser.setNickName(nickName.getText().toString());
            newUser.setOrganization(organization.getText().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy", Locale.getDefault());
            try {
                if (!birthday.getText().toString().isEmpty())
                 newUser.setBirthday(
                        dateFormat.parse(
                                birthday.getText().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            newUser.setProfileImage(
                    BitmapHelper.bitmapToString(
                            ((BitmapDrawable)profile.getDrawable())
                                    .getBitmap()));

            //returns to the first register activity and transport the User object as an extra
            Intent intent = new Intent(this, Register_num_one.class);
            intent.putExtra("newUser",  newUser);
            startActivity(intent);
            finish();
        }
    }


    //save user in data base
    public void saveUser() {
        // Write a message to the database


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(newUser.getUserName(),
                                    newUser.getPassword(),
                                    newUser.getEmail(),
                                    nickName.getText().toString(),
                                    organization.getText().toString(),
                                    DateConverter.convertStringToDate(birthday.getText().toString()),
                                    BitmapHelper.bitmapToString(
                                            ((BitmapDrawable)profile.getDrawable())
                                                    .getBitmap()));

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register_num_two.this, "User Registered", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                        }
                        else
                            Toast.makeText(Register_num_two.this, "XXXX" + task.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void saveImageToGallery(Bitmap bitmap, Context context) {
        ContentValues values = new ContentValues(); //a container (values) that store information (metadata) about the image, such as its name, type, and location.

        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg"); // Unique image name
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // Image type (JPEG)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp"); // Save inside Pictures/MyApp

        //Insert the image metadata into MediaStore and get the Uri
        Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //try-with-resources → Ensures the stream closes automatically.
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(imageUri)) {
            //Write the bitmap image data into the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // Save as JPEG with 100% quality

            //Show success message
            Toast.makeText(context, "נשמר בהצלחה!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "שמירה נכשלה!", Toast.LENGTH_SHORT).show();
        }
    }
}