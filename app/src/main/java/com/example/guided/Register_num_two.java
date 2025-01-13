package com.example.guided;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Register_num_two extends AppCompatActivity implements View.OnClickListener {
    TextView organization;
    EditText birthday;
    EditText nickName;
    ImageView profile;
    ArrayList<String> arrayList;
    Dialog dialog;
    Button backBTN;
    Button saveBTN;
    String userName;
    String password;
    String email;
    TextView alartForNickName;
    TextView alartForOrganization;
    TextView alartForBirthday;
    TextView alartForProfile;

    User newUser;

    TextView textView;




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


        profile=findViewById(R.id.profile);
        profile.setOnClickListener(this);

        nickName=findViewById(R.id.nickName);


        birthday=findViewById(R.id.birthday);
        birthday.setOnClickListener(this);


        organization = findViewById(R.id.organization);
        arrayList = new ArrayList<>();

        backBTN= findViewById(R.id.back);
        backBTN.setOnClickListener(this);

        saveBTN=findViewById(R.id.save);
        saveBTN.setOnClickListener(this);

        textView=findViewById(R.id.tv);



        Intent intent = getIntent();
        newUser= (User) intent.getSerializableExtra("newUser");
        textView.setText(newUser.toString());

      /*  if (newUser!=null){
            userName= newUser.getUserName();
            password=newUser.getPassword();
            email=newUser.getEmail();
            nickName.setText(newUser.getNickName());
            birthday.setText(newUser.getBirthday().toString());
            organization.setText(newUser.getOrganization());
            profile.setImageBitmap(newUser.getProfileImage());
        }*/


         alartForOrganization=findViewById(R.id.alartOrganization);
         alartForBirthday=findViewById(R.id.alartBirthday);
         alartForNickName=findViewById(R.id.alartNickName);
         alartForProfile=findViewById(R.id.alartProfile);


        arrayList.add("הצופים");
        arrayList.add("המכבי הצעיר");
        arrayList.add("התנועה החדשה");
        arrayList.add("בני המושבים");
        arrayList.add("השומר החדש");
        arrayList.add("אור לחינוך");
        arrayList.add("האות הבינלאומי לנוער וצעירים");
        arrayList.add("אחריי!");
        arrayList.add("האיגוד החקלאי");
        arrayList.add("יוניסטרים");
        arrayList.add("כנפיים של קרמבו");
        arrayList.add("קצ\"ב");
        arrayList.add("איגי- ארגון הנוער הגאה");
        arrayList.add("נוער חובב תנ\"ך");
        arrayList.add("נוער לנוער");
        arrayList.add("נוער לנוער");
        arrayList.add("פסגות");
        arrayList.add("צמר\"ת");
        arrayList.add("שומרי המפרץ");
        arrayList.add("תנועת הנוער הדרוזי");
        arrayList.add("נטע@");
        arrayList.add("השומר הצעיר");
        arrayList.add("הנוער העובד והלומד");
        arrayList.add("ברית הנוער הקומוניסטי הישראלי");
        arrayList.add("החלוץ הצעיר");
        arrayList.add("המחנות העולים");
        arrayList.add("נוער מרצ");
        arrayList.add("נוער העבודה");
        arrayList.add("הנוער הלאומי");
        arrayList.add("הנוער הציוני");
        arrayList.add("נוער הליכוד");
        arrayList.add("בני עקיבא");
        arrayList.add("אריאל");
        arrayList.add("עוז");
        arrayList.add("עזרא");
        arrayList.add("אור ישראלי");
        arrayList.add("בני תורה");
        arrayList.add("בנות בתיה");
        arrayList.add("היכלי ענ\"ג");
        arrayList.add("פרחי הדגל");
        arrayList.add("צבאות השם");
        arrayList.add("נוער חב\"ד");
        arrayList.add("יחדיו");
        arrayList.add("נוע\"ם");
        arrayList.add("נוער תל\"ם");
        arrayList.add("גדנ\"ע");
        arrayList.add("גדנ\"ע אוויר");
        arrayList.add("חוגי סיירות של החברה להגנת הטבע");
        arrayList.add("חוגי סיירות של קק\"ל ע\"ש אורי מימון");
        arrayList.add("מועצת התלמידים והנוער הארצית");
        arrayList.add("מש\"צים");
        arrayList.add("נוער מד\"א");
        arrayList.add("מד\"צים");
        arrayList.add("צופי אש");
        arrayList.add("בית\"ר");
        arrayList.add("נוער המחנה הממלכתי");
        arrayList.add("נוער יש עתיד");
        arrayList.add("תנועת תרבות");
        arrayList.add("הצבעים שלנו");
        arrayList.add("סדאקה רעות");
        arrayList.add("אחר");
        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog =new Dialog(Register_num_two.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width\
                dialog.getWindow().setLayout(650,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(Register_num_two.this, android.R.layout.simple_list_item_1,arrayList);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
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

    @Override
    public void onClick(View v) {
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        if (v == profile) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("select one option");
            builder.setMessage("do you want to take a picture or to add a picture from your galery?");
            builder.setCancelable(true);
            builder.setPositiveButton("CAMERA", new HandleAlartDialogLostener());
            builder.setNegativeButton("galery", new HandleAlartDialogLostener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (v==birthday){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Register_num_two.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our edit text.
                            birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, year, month, day);

            datePickerDialog.show();

        }

        if (v == saveBTN) {



            // בדיקות של כינוי
            if (nickName.getText().toString().length() < 2)
                alartForNickName.setText(" * כינוי קצר מדי נסה שנית. " + '\n' + alartForNickName.getText().toString());
            else if (nickName.getText().toString().length() > 15)
                alartForNickName.setText("* כינוי ארוך מדי נסה שנית." + '\n' + alartForNickName.getText().toString());
            if (nickName.getText().toString().isEmpty())
                alartForNickName.setText("* שדה חובה! הכנס כינוי" + '\n' + alartForNickName.getText().toString());

            //בדיקות של יום הולדת

            if(birthday.getText().toString().isEmpty())
                alartForBirthday.setText("* שדה חובה! הכנס תאריך לידה" + '\n' + alartForBirthday.getText().toString());

            //בדיקות של מסגרת הדרכה
            if (organization.getText().equals(""))
                alartForOrganization.setText("* שדה חובה! בחר מסגרת הדרכה");

            //בדיקות סופיות
            if (!organization.getText().toString().isEmpty()&&(!birthday.getText().toString().isEmpty())&&!nickName.getText().toString().isEmpty()){
                saveUser();
                Toast.makeText(this, newUser.toString(), Toast.LENGTH_LONG).show();

                Intent intent=new Intent(this, Home_page.class);
                startActivity(intent);
                finish();

            }
        }



        if(v == backBTN){
            //אין לי מושג איך חוזרים למסך הראשון ומראים את הנתונים שהמשתמש כבר הכניס

            newUser.setNickName(nickName.getText().toString());
            newUser.setOrganization(organization.getText().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                newUser.setBirthday(dateFormat.parse(birthday.getText().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            newUser.setProfileImage(((BitmapDrawable)profile.getDrawable()).getBitmap());


            Intent intent=new Intent(this, Register_num_one.class);
            intent.putExtra("user",  newUser);
            Toast.makeText(this, newUser.toString(), Toast.LENGTH_LONG).show();

            startActivity(intent);
            finish();


        }
    }

    private class HandleAlartDialogLostener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(Register_num_two.this, "YOU SELECTED" + which, Toast.LENGTH_SHORT).show();
            //camera
            if (which == -1) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            } else if (which == -2) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "picture"), 1);
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==0) {//coming from camera
            if (resultCode == RESULT_OK) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(bitmap);

            }
        }
        else if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(bitmap);
            }

        }



    }


    public void saveUser(){
        // Write a message to the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date birthdayDate;
        //A try-catch block handles invalid input formats, ensuring the app doesn't crash if the user enters an incorrect date.
        try{
            // dateFormat.parse(dateString) converts the string into a Date object.
            birthdayDate = dateFormat.parse(birthday.getText().toString());} catch (
                ParseException e) {
            throw new RuntimeException(e);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        /*newUser.setNickName(nickName.getText().toString());
        newUser.setOrganization(organization.getText().toString());
        newUser.setBirthday(birthdayDate);
        newUser.setProfileImage(((BitmapDrawable)profile.getDrawable()).getBitmap());*/
        User user= new User(newUser.getUserName(), newUser.getPassword(),newUser.getEmail(),nickName.getText().toString(),organization.getText().toString(),birthdayDate,((BitmapDrawable)profile.getDrawable()).getBitmap());

        myRef.setValue(user);
    }



}