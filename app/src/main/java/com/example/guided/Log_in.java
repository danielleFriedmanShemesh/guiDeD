package com.example.guided;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log_in extends AppCompatActivity implements View.OnClickListener {
    ImageView submmitBTN;
    EditText userName;
    EditText password;
    EditText email;
    TextView alartForUserName;
    TextView alartForPassword;
    TextView alartForEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();


        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        alartForUserName = findViewById(R.id.alartUserName);
        alartForPassword = findViewById(R.id.alartPassword);
        alartForEmail = findViewById(R.id.alartEmail);

        submmitBTN = findViewById(R.id.save);
        submmitBTN.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");

        if(v == submmitBTN){
            //בדיקות סיסמה
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
//בדיכות שם משתמש
            if(userName.getText().toString().isEmpty())
            {
                alartForUserName.setText("* שדה חובה! הכנס שם משתמש" +
                        '\n' +
                        alartForUserName.getText().toString());
            }
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
                        // Username is in the dataBase

                        //final checks before moving to the second activity
                        if ((!password.getText().toString().isEmpty()) &&
                                (!userName.getText().toString().isEmpty()) &&
                                (!email.getText().toString().isEmpty())) {
                            if (checkUserName(userName.getText().toString()) &&
                                    (checkPassword(password.getText().toString())) &&
                                    (checkEmail(email.getText().toString()))) {

                                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                        .addOnCompleteListener(Log_in.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d(TAG, "signInWithEmail:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    updateUI(user);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(Log_in.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    updateUI(null);
                                                }
                                            }
                                        });
                                //go to the second register activity and transport the User object as an extra
                                Intent intent = new Intent(Log_in.this, Home_page.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // FLAG_ACTIVITY_CLEAR_TOP- מנקה את הדרך חזרה ומונע מהמשתמש לחזור למסכים קודמים עם כפתור Back.
                                // FLAG_ACTIVITY_NEW_TASK - פותח את האקטיביטי החדשה בתוך טסק חדש. ועוזר "לנתק" את המסך החדש מהקודמים.
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    if(!x[0]) {

                        alartForUserName.setText("* שם המשתמש שבחרת לא קיים " +
                                '\n' +
                                alartForUserName.getText().toString());


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

    private void updateUI(FirebaseUser user) {

    }


}