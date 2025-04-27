package com.example.guided.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.guided.Helpers.RegisteretionHelper.*;

import android.annotation.SuppressLint;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.User;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Log_in extends BaseActivity implements View.OnClickListener {
    ImageView submmitBTN;
    EditText userName;
    EditText password;
    EditText email;
    TextView alartForUserName;
    TextView alartForPassword;
    TextView alartForEmail;
    TextView alartForAll;

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
        alartForAll = findViewById(R.id.alartsForAll);

        submmitBTN = findViewById(R.id.save);
        submmitBTN.setOnClickListener(this);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");
        alartForAll.setText("");
        alartForAll.setVisibility(View.INVISIBLE);

        if(v == submmitBTN){
            alartForPassword.setText(checkAlertsForPassword(password.getText().toString()));
            alartForEmail.setText(checkAlertsForEmail(email.getText().toString()));
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
                    if (x[0]){
                        // Username is in the dataBase

                        //final checks before moving to the second activity
                        if (checkUserName(userName.getText().toString()) &&
                                checkPassword(password.getText().toString()) &&
                                checkEmail(email.getText().toString())) {

                            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(Log_in.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                updateUI(user);
                                                //go to the second register activity and transport the User object as an extra
                                                Intent intent = new Intent(Log_in.this, Home_page.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                // FLAG_ACTIVITY_CLEAR_TOP- מנקה את הדרך חזרה ומונע מהמשתמש לחזור למסכים קודמים עם כפתור Back.
                                                // FLAG_ACTIVITY_NEW_TASK - פותח את האקטיביטי החדשה בתוך טסק חדש. ועוזר "לנתק" את המסך החדש מהקודמים.
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Toast.makeText(Log_in.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                            }
                                        }
                                    });
                        }
                        else {
                            alartForAll.setText("אחד או יותר מהפרטי ההזדהות שהוכנסו שגויים! ");
                            alartForAll.setVisibility(View.VISIBLE);
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


    private void updateUI(FirebaseUser user) {

    }


}