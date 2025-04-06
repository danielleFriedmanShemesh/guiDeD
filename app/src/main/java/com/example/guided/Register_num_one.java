package com.example.guided;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_num_one extends BaseActivity implements View.OnClickListener {
    ImageView continueBtn;
    EditText userName;
    EditText password;
    EditText email;
    TextView alartForUserName;
    TextView alartForPassword;
    TextView alartForEmail;
    User newUser=new User();

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

        continueBtn=findViewById(R.id.continueBTN);
        userName=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        alartForUserName=findViewById(R.id.alartUserName);
        alartForPassword=findViewById(R.id.alartPassword);
        alartForEmail=findViewById(R.id.alartEmail);

        //set fields with data from User object that returned from the second register activity as an extra
        Serializable user=  getIntent().getSerializableExtra("newUser");
        if (user instanceof User){
            newUser= (User) user;
            userName.setText(newUser.getUserName());
            password.setText(newUser.getPassword());
            email.setText(newUser.getEmail());
        }

        continueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");

        // if you click on the 'next' button
        if(v==continueBtn){

            //בדיקות של סיסמה
            if(password.getText().toString().length() < 6){
                alartForPassword.setText("* סיסמה קצרה מדי נסה שנית" +
                        '\n' +
                        alartForPassword.getText().toString());
            }
            if(password.getText().toString().isEmpty())
            {
                alartForPassword.setText("* שדה חובה! הכנס סיסמה" +
                        '\n' +
                        alartForPassword.getText().toString());
            }
            else if(password.getText().toString().length() > 10){
                alartForPassword.setText("* סיסמה ארוכה מדי נסה שנית" +
                        '\n' +
                        alartForPassword.getText().toString());
            }

            //בדיקות של אימייל
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(email.getText().toString().isEmpty())
            {
                alartForUserName.setText("* שדה חובה! הכנס מייל" +
                        '\n' +
                        alartForEmail.getText().toString());
            }
            if(!email.getText().toString().matches(emailPattern))
            {
                alartForEmail.setText("* המייל שהכנסת אינו תקין, נסה שנית" +
                        '\n' +
                        alartForEmail.getText().toString());
            }

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

            // הבדיקה של האם השם משתמש קיים או לא לא עובד צריך לסדר את זה

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
                    if (x[0]){
                        // Username is already taken
                        alartForUserName.setText("* שם המשתמש שבחרת תפוס בחר שם משתמש אחר " +
                                '\n' +
                                alartForUserName.getText().toString());
                    }

                    if(!x[0]) {
                        //final checks before moving to the second activity
                        if ((!password.getText().toString().isEmpty()) &&
                                (!userName.getText().toString().isEmpty()) &&
                                (!email.getText().toString().isEmpty())) {
                            if (checkUserName(userName.getText().toString()) &&
                                    (checkPassword(password.getText().toString())) &&
                                    (checkEmail(email.getText().toString()))) {
                                newUser.setUserName(userName.getText().toString());
                                newUser.setPassword(password.getText().toString());
                                newUser.setEmail(email.getText().toString());

                                //go to the second register activity and transport the User object as an extra
                                Intent intent = new Intent(Register_num_one.this, Register_num_two.class);
                                intent.putExtra("newUser", newUser);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            });



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
        return ((userName.length() >= 8) && (userName.length() <= 15) && (input_Validation(userName)) && !checkIfOccupied(userName));
    }

    //checks if password is stand at all the terms
    public static boolean checkPassword(String password){
        return ((password.length() >= 6) && (password.length() <= 10));
    }

    //checks if email is stand at all the terms
    public static boolean checkEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return (email.matches(emailPattern));
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
            }
        });

        return x[0];
    }
}