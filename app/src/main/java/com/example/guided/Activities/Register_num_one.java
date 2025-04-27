package com.example.guided.Activities;

import static com.example.guided.Helpers.RegisteretionHelper.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.User;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.FirebaseUserHelper;
import com.example.guided.R;

import java.io.Serializable;
import java.util.ArrayList;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");

        // if you click on the 'next' button
        if(v==continueBtn){

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

                    if(!x[0]) {
                        //final checks before moving to the second activity
                        if (checkUserName(userName.getText().toString()) &&
                                checkPassword(password.getText().toString()) &&
                                checkEmail(email.getText().toString())) {
                            newUser.setUserName(userName.getText().toString());
                            newUser.setPassword(password.getText().toString());
                            newUser.setEmail(email.getText().toString());
                            newUser.setProfileImage(
                                    BitmapHelper.
                                            bitmapToString(
                                                    BitmapFactory.
                                                            decodeResource(
                                                                    getResources(),
                                                                    R.drawable.profile)));

                            //go to the second register activity and transport the User object as an extra
                            Intent intent = new Intent(
                                    Register_num_one.this,
                                    Register_num_two.class);
                            intent.putExtra("newUser", newUser);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        // Username is already taken
                        alartForUserName.setText("* שם המשתמש שבחרת תפוס בחר שם משתמש אחר " +
                                '\n' +
                                alartForUserName.getText().toString());
                    }
                }
            });
        }
    }
}