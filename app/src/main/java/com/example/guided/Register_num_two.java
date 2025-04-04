package com.example.guided;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Register_num_two extends AppCompatActivity implements View.OnClickListener {
    TextView organization;
    EditText birthday;
    EditText nickName;
    ImageView profile;
    String[] listOrganizations;
    Dialog dialog;
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

        //set fields with data from User object that returned from the first register activity as an extra
        Intent intent=getIntent();
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

        alartForOrganization = findViewById(R.id.alartOrganization);
        alartForBirthday = findViewById(R.id.alartBirthday);
        alartForNickName = findViewById(R.id.alartNickName);
        alartForProfile = findViewById(R.id.alartProfile);

        organization = findViewById(R.id.organization);
        //crating a list of youth organizations in israel

        listOrganizations = getResources().getStringArray(R.array.organization_adjustment);

        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Register_num_two.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.search);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Register_num_two.this,
                        R.layout.list_item,
                        listOrganizations);


                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        organization.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View v) {
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        // if user click on the profile image view
        if (v == profile) {

            //creating a dialog for adding a profile picture from gallery or for taking a picture at the camera
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("העלאת תמונת פרופיל");
            builder.setMessage("תרצו להעלות תמונה מהגלריה או לצלם תמונה במצלמה?");
            builder.setCancelable(true);
            builder.setPositiveButton("מצלמה", new HandleAlartDialogLostener());
            builder.setNegativeButton("גלריה", new HandleAlartDialogLostener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //if user click on the birthday edit text
        if (v == birthday){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //open a Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Register_num_two.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        //show the date that the user chose at the birthday edit text
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our edit text.
                            birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, year, month, day);

            datePickerDialog.show();
        }

        // if you click on the 'finish' button
        if (v == saveBTN) {
            // בדיקות של כינוי
            if (nickName.getText().toString().length() < 2)
                alartForNickName.setText(" * כינוי קצר מדי נסה שנית. "
                        + '\n'
                        + alartForNickName.getText().toString());
            else if (nickName.getText().toString().length() > 15)
                alartForNickName.setText("* כינוי ארוך מדי נסה שנית."
                        + '\n'
                        + alartForNickName.getText().toString());
            if (nickName.getText().toString().isEmpty())
                alartForNickName.setText("* שדה חובה! הכנס כינוי"
                        + '\n'
                        + alartForNickName.getText().toString());

            //בדיקות של יום הולדת

            if(birthday.getText().toString().isEmpty())
                alartForBirthday.setText("* שדה חובה! הכנס תאריך לידה"
                        + '\n'
                        + alartForBirthday.getText().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy", Locale.getDefault());
            Date birthdayDate;
            try {
                birthdayDate = dateFormat.parse(birthday.getText().toString());
                Log.e("year", "yearThis: " + (birthdayDate.getYear()+1900));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            Log.e("year", "year: " + year);

            if (birthdayDate != null &&
                    ((birthdayDate.getYear() + 1900) >= year ||
                            (birthdayDate.getYear() + 1900) <= 1900)) {
                alartForBirthday.setText("* התאריך שהוכנס לא תקין! נסה שנית"
                        + '\n'
                        + alartForBirthday.getText().toString());

            }


            //בדיקות של מסגרת הדרכה
            if (organization.getText().equals(""))
                alartForOrganization.setText("* שדה חובה! בחר תנועת נוער");

            //final checks of creating a new user at the database
            if (!organization.getText().toString().isEmpty() &&
                !birthday.getText().toString().isEmpty() &&
                !nickName.getText().toString().isEmpty() &&
                !BitmapHelper.bitmapToString(
                        ((BitmapDrawable)profile.getDrawable())
                                .getBitmap())
                        .isEmpty())
            {
                if (checkNickName(nickName.getText().toString()) &&
                        checkBirthday(birthday.getText().toString())) {
                    Log.e("year", "year: " + birthday.getText().toString());
                    Log.e("name", "mane: " + nickName.getText().toString());

                    progressBar.setVisibility(View.VISIBLE);
                    saveUser();

                    //open the home page after the user had been saved in the database
                    Intent intent = new Intent(this, Home_page.class);
                    startActivity(intent);
                    finish();
                }
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

    private class HandleAlartDialogLostener implements DialogInterface.OnClickListener {
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
        //get image from camera
        if(requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(bitmap);
            }
        }
        //get image from gallery
        else if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(bitmap);
            }
        }
    }

    //save user in data base
    public void saveUser() {
        // Write a message to the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date birthdayDate;
        //A try-catch block handles invalid input formats, ensuring the app doesn't crash if the user enters an incorrect date.
        try{
            // dateFormat.parse(dateString) converts the string into a Date object.
            birthdayDate = dateFormat.parse(birthday.getText().toString());}
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

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
                                    birthdayDate,
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
    public static boolean checkNickName(String nickName){
        return ((nickName.length() >= 2) && (nickName.length() <= 15));
    }

    public static boolean checkBirthday(String birthday){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date birthdayDate;
        try {
            birthdayDate = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return (birthdayDate.getYear() + 1900) < year &&
                (birthdayDate.getYear() + 1900) > 1900;
    }
}