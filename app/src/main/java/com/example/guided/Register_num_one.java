package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_num_one extends AppCompatActivity implements View.OnClickListener {
    Button continueBtn;
    EditText userName;
    EditText password;
    EditText email;
    TextView alartForUserName;
    TextView alartForPassword;
    TextView alartForEmail;
    User user;



    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_num_one);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView=findViewById(R.id.tv);


        continueBtn=findViewById(R.id.continueBTN);
        userName=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        alartForUserName=findViewById(R.id.alartUserName);
        alartForPassword=findViewById(R.id.alartPassword);
        alartForEmail=findViewById(R.id.alartEmail);

        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String userNameStr = extras.getString("userName");
            String passwordStr = extras.getString("password");
            String emailStr = extras.getString("email");
            userName.setText(userNameStr);
            password.setText(passwordStr);
            email.setText(emailStr);
        }*/

        user= (User) getIntent().getSerializableExtra("newUser");

            if (user!=null){
            userName.setText(user.getUserName());
            password.setText(user.getPassword());
            email.setText(user.getEmail());
        }


        continueBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");

        //לבדוק האם כל השדות מלאים ואם לא הודעת שגיאה


        if(v==continueBtn){
//בדיקות של שם משתמש
            if(userName.getText().toString().length()!=8){
                if(userName.getText().toString().length()<8){
                    alartForUserName.setText(" * שם משתמש קצר מדי נסה שנית. "+'\n'+ alartForUserName.getText().toString());
                }
                else if(userName.getText().toString().length()>8){
                    alartForUserName.setText("* שם משתמש ארוך מדי נסה שנית."+ '\n'+alartForUserName.getText().toString());
                }
            }
            if(userName.getText().toString().isEmpty()){
                alartForUserName.setText("* שדה חובה! הכנס שם משתמש"+ '\n'+alartForUserName.getText().toString());
            }
            if(!input_Validation(userName.getText().toString())){
                alartForUserName.setText("* שם המשתמש חייב להכיל רק ספרות, אותיות באנגלית ותווים מיוחדים."+ '\n'+alartForUserName.getText().toString());
            }

            //אחרי שיהיה דאטא בייס אז זאת תהיה בדיקה האם שם המשתמש כבר קיים

            //if(checkIfOccupied(userName)){
            //alartForUserName.setText("שם המשתמש תפוס, הכנס שם אחר ונסה שוב."+ '\n'+alartForUserName.getText().toString());}

//בדיקות של סיסמה
            if(password.getText().toString().length()<6){
                alartForPassword.setText("* סיסמה קצרה מדי נסה שנית"+'\n'+ alartForPassword.getText().toString());
            }
            if(password.getText().toString().isEmpty()){
                alartForPassword.setText("* שדה חובה! הכנס סיסמה"+ '\n'+alartForPassword.getText().toString());

            }
            else if(password.getText().toString().length()>10){
                alartForPassword.setText("* סיסמה ארוכה מדי נסה שנית"+'\n'+ alartForPassword.getText().toString());
            }

//בדיקות של אימייל
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(email.getText().toString().isEmpty()){
                alartForUserName.setText("* שדה חובה! הכנס מייל"+ '\n'+alartForEmail.getText().toString());

            }
            if(!email.getText().toString().matches(emailPattern)){
                alartForEmail.setText("* המייל שהכנסת אינו תקין, נסה שנית"+'\n'+ alartForEmail.getText().toString());
            }

//בדיקות סופיות
            if((!password.getText().toString().isEmpty())&&(!userName.getText().toString().isEmpty())&&(!email.getText().toString().isEmpty())){
                if(checkUserName(userName.getText().toString())&&(checkPassword(password.getText().toString()))&&(checkEmail(email.getText().toString()))){

                    if(user!=null){
                    user.setUserName(userName.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setEmail(email.getText().toString());}

                    else {user= new User(userName.getText().toString(),password.getText().toString(),email.getText().toString());}

                    Intent intent=new Intent(this, Register_num_two.class);
                    intent.putExtra("newUser", user);
                    Toast.makeText(this, user.toString(), Toast.LENGTH_LONG).show();

                    textView.setText(user.toString());


                    startActivity(intent);
                    finish();

                }
            }


        }
    }

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
    public static boolean checkUserName(String userName){
        return ((userName.length()==8)&&(input_Validation(userName))/*&&checkIfOccupied(userName)==false*/);
    }

    public static boolean checkPassword(String password){
        return ((password.length()>=6)&&(password.length()<=10));
    }
    public static boolean checkEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return (email.matches(emailPattern));
    }




}