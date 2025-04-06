package com.example.guided;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_profile extends AppCompatActivity implements View.OnClickListener {
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
    ProgressBar progressBar;

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

       // progressBar = findViewById(R.id.progressBar);



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
        listOrganizations = getResources().getStringArray(R.array.organization_adjustment);

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
                nickName.setText(u.getNickName());
                profile.setImageBitmap(BitmapHelper.stringToBitmap(u.getProfileImage()));
                updatedUser.setEmail(u.getEmail());
                updatedUser.setPassword(u.getPassword());
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        // Camera Launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            profile.setImageBitmap(bitmap);
                        }
                    }
                }
        );

        // Gallery Launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            profile.setImageURI(imageUri);
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        //TODO: ליצור מחלקה של הרשמה שיש לה פונקציות שמתפלות בכל מה שקשור לרישום/ התחברות ושאני פשוטל אקרע לה גם פה וגם בהרשמה ובהתחברת כדי שלא יהיה כפילות של קוד

        alartForUserName.setText("");
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        if (v == birthday){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //open a Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Edit_profile.this, new DatePickerDialog.OnDateSetListener() {
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

        if (v == organization){
            dialog = new Dialog(Edit_profile.this);

            // set custom dialog
            dialog.setContentView(R.layout.dialog_searchable_spinner);

            // show dialog
            dialog.show();

            // Initialize and assign variable
            EditText editText = dialog.findViewById(R.id.search);
            ListView listView = dialog.findViewById(R.id.list_view);

            // Initialize array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Edit_profile.this,
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

        if(v == profile){

            //creating a dialog for adding a profile picture from gallery or for taking a picture at the camera
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("העלאת תמונת פרופיל");
            builder.setMessage("תרצו להעלות תמונה מהגלריה או לצלם תמונה במצלמה?");
            builder.setCancelable(true);
            builder.setPositiveButton("מצלמה", new Edit_profile.HandleAlartDialogLostener());
            builder.setNegativeButton("גלריה", new Edit_profile.HandleAlartDialogLostener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (v == saveBTN){
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

            //בדיקות של שם משתמש
            if(userName.getText().toString().length() != 8){
                if(userName.getText().toString().length() < 8){
                    alartForUserName.setText(" * שם משתמש קצר מדי נסה שנית. " +
                            '\n' +
                            alartForUserName.getText().toString());
                }
                else if(userName.getText().toString().length() > 15){
                    alartForUserName.setText("* שם משתמש ארוך מדי נסה שנית." +
                            '\n' +
                            alartForUserName.getText().toString());
                }
            }
            if(userName.getText().toString().isEmpty())
            {
                alartForUserName.setText("* שדה חובה! הכנס שם משתמש" +
                        '\n' +
                        alartForUserName.getText().toString());
            }
            if(!input_Validation(userName.getText().toString()))
            {
                alartForUserName.setText("* שם המשתמש חייב להכיל ספרות, אותיות באנגלית ותווים מיוחדים." +
                        '\n' +
                        alartForUserName.getText().toString());
            }

            boolean x = checkIfOccupied(userName.getText().toString());

            if (x){
                // Username is already taken
                alartForUserName.setText("* שם המשתמש שבחרת תפוס בחר שם משתמש אחר " +
                        '\n' +
                        alartForUserName.getText().toString());
            }

            //TODO: לבדוק איך לעשות שזה יעשה ריפרש ויראה ישר את הפרטים החדשים

            if(!x) {
                //final checks of creating a new user at the database
                if (!organization.getText().toString().isEmpty() &&
                        !birthday.getText().toString().isEmpty() &&
                        !nickName.getText().toString().isEmpty() &&
                        !BitmapHelper.bitmapToString(
                                        ((BitmapDrawable) profile.getDrawable())
                                                .getBitmap())
                                .isEmpty() &&
                        !userName.getText().toString().isEmpty()) {
                    if (checkUserName(userName.getText().toString()) &&
                            checkNickName(nickName.getText().toString()) &&
                            checkBirthday(birthday.getText().toString())) {
                        updatedUser.setUserName(userName.getText().toString());
                        updatedUser.setNickName(nickName.getText().toString());
                        updatedUser.setOrganization(organization.getText().toString());
                        updatedUser.setProfileImage(BitmapHelper.bitmapToString(
                                ((BitmapDrawable) profile.getDrawable())
                                        .getBitmap()));

                        updatedUser.setBirthday(birthdayDate);
                        //progressBar.setVisibility(View.VISIBLE);
                        setUser();
                        finish();
                    }
                }
            }
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

                        }
                    }
                }
        );
    }

    private class HandleAlartDialogLostener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //camera
            if (which == -1) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);

            }
            //gallery
            else if (which == -2) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Intent.ACTION_PICK → Opens an app to let the user pick something.
                //MediaStore.Images.Media.EXTERNAL_CONTENT_URI → Gets images from external storage (Gallery).

                galleryLauncher.launch(galleryIntent);
            }
        }
    }

    //checks if input has letters and digits and special characters
    public static boolean input_Validation(String input)
    {
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[^A-Za-z0-9]");

        Matcher hasLetter = letter.matcher(input);
        Matcher hasDigit = digit.matcher(input);
        Matcher hasSpecial = special.matcher(input);

        return hasLetter.find() && hasDigit.find() && hasSpecial.find();
    }

    //checks if username is stand at all the terms
    public static boolean checkUserName(String userName){
        return ((userName.length() >= 8) && (userName.length() <= 15) && (input_Validation(userName))&& !checkIfOccupied(userName));
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





    public static boolean checkIfOccupied(String userName){
        final boolean[] x = {false};
        FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
        firebaseUserHelper.fetchUsers(new FirebaseUserHelper.DataStatus() {
            @Override
            public void onDataLoaded(ArrayList<User> users) {
                for(User user : users){
                    if (user.getUserName().equals(userName)) {
                        // Username is already taken
                        x[0] = true;
                    }
                }
                x[0] = false;
            }
        });

        return x[0];
    }


}