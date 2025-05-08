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
    private ImageView continueBtn; //כפתור מעבר למסך הבא
    private EditText userName; //שדה קלט לשם משתמש
    private EditText password; //שדה קלט לסיסמה
    private EditText email; //שדה קלט לאימייל
    private TextView alartForUserName; // תיבת טקסט להתרעה על שגיאה בשם המשתמש
    private TextView alartForPassword; //תיבת טקסט להתרעה על שגיאה בסיסמה
    private TextView alartForEmail; //תיבת טקסט להתרעה על שגיאה באימייל
    private User newUser=new User(); //אובייקט User המייצג את המשתמש החדש בתהליך ההרשמה

    /**
     * פונקציית onCreate - מאותחלת בעת יצירת האקטיביטי.
     * מאתחלת את רכיבי הממשק, מאזינה ללחיצה על כפתור, ובודקת אם הועבר אובייקט משתמש מהמשך תהליך ההרשמה.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי (אם קיים)
     */
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

        // אתחול רכיבי ממשק
        continueBtn=findViewById(R.id.continueBTN);
        userName=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        alartForUserName=findViewById(R.id.alartUserName);
        alartForPassword=findViewById(R.id.alartPassword);
        alartForEmail=findViewById(R.id.alartEmail);

        //מגדיר שדות עם נתונים מאובייקט User  שחזרו ממסך ההרשמה השני כאקסטרה
        Serializable user=  getIntent().
                getSerializableExtra(
                        "newUser");
        if (user instanceof User){
            newUser= (User) user;
            userName.setText(newUser.getUserName());
            password.setText(newUser.getPassword());
            email.setText(newUser.getEmail());
        }
        continueBtn.setOnClickListener(this);
    }

    /**
     * מאזין ללחיצה על כפתור "המשך".
     * מבצע בדיקות תקינות לשדות ההזדהות, ובודק אם שם המשתמש פנוי.
     * אם כן, ממשיך למסך ההרשמה הבא ומעביר את המשתמש כ-extra.
     *
     * @param v הרכיב עליו התבצעה הלחיצה
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");

        if(v==continueBtn){

            alartForPassword.setText(
                    checkAlertsForPassword(
                            password.getText().toString()));
            alartForEmail.setText(
                    checkAlertsForEmail(
                            email.getText().toString()));
            alartForUserName.setText(
                    checkAlertsForUserName(
                            userName.getText().toString()));


            final boolean[] x = {false};
            FirebaseUserHelper firebaseUserHelper = new FirebaseUserHelper();
            firebaseUserHelper.fetchUsers(
                    new FirebaseUserHelper.DataStatus() {
                @Override
                public void onDataLoaded(ArrayList<User> users) {
                    for(User user : users){
                        if (user.getUserName().equals(
                                userName.getText().toString())) {
                            x[0] = true; // שם המשתמש כבר קיים
                        }
                    }

                    if(!x[0]) {
                        // עדכון פרטי המשתמש
                        if (checkUserName(
                                userName.getText().toString()) &&
                                checkPassword(
                                        password.getText().toString()) &&
                                checkEmail(
                                        email.getText().toString())) {
                            newUser.setUserName(
                                    userName.getText().toString());
                            newUser.setPassword(
                                    password.getText().toString());
                            newUser.setEmail(
                                    email.getText().toString());
                            newUser.setProfileImage(
                                    BitmapHelper.
                                            bitmapToString(
                                                    BitmapFactory.
                                                            decodeResource(
                                                                    getResources(),
                                                                    R.drawable.profile)));

                            // מעבר למסך ההרשמה השני ומעביר את האובייקט מסוג USER כEXTRA
                            Intent intent = new Intent(
                                    Register_num_one.this,
                                    Register_num_two.class);
                            intent.putExtra("newUser", newUser);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        // שם המשתמש כבר קיים
                        alartForUserName.setText(
                                "* שם המשתמש שבחרת תפוס בחר שם משתמש אחר " +
                                '\n' +
                                alartForUserName.getText().toString());
                    }
                }
            });
        }
    }
}