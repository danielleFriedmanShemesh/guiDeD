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

/**
 * מחלקת Log_in אחראית על מסך ההתחברות של המשתמש.
 * במסך זה המשתמש מזין שם משתמש, אימייל וסיסמה – ואם המידע תקין, הוא מועבר לעמוד הבית.
 * מתבצעות בדיקות תקינות מקומיות ולאחר מכן גם אימות דרך Firebase Authentication.
 */
public class Log_in extends BaseActivity implements View.OnClickListener {
    private ImageView submmitBTN; //כפתור שליחה להתחברות
    private EditText userName; //שדה קלט לשם משתמש
    private EditText password; //שדה קלט לסיסמה
    private EditText email; //שדה קלט לאימייל
    private TextView alartForUserName; // תיבת טקסט להתרעה על שגיאה בשם המשתמש
    private TextView alartForPassword; //תיבת טקסט להתרעה על שגיאה בסיסמה
    private TextView alartForEmail; //תיבת טקסט להתרעה על שגיאה באימייל
    private TextView alartForAll; // תיבת טקסט להתרעה כללית במקרה של שגיאה

    private FirebaseAuth mAuth; //אובייקט Firebase לאימות משתמשים

    /**
     * הפעולה onCreate מופעלת כאשר האקטיביטי נוצר.
     * היא מאתחלת את רכיבי הממשק, מאזינה ללחיצה על כפתור ההתחברות,
     * ומכינה את Firebase לאימות משתמשים.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי (אם קיים)
     */
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

        // אתחול רכיבי הממשק
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

    /**
     * פעולה שמופעלת כאשר נלחץ כפתור ההתחברות(submmitBTN).
     * מבצעת בדיקה לשדות הקלט, בודקת האם המשתמש קיים במסד הנתונים,
     * ואם כן, מתחברת ל־Firebase ומעבירה את המשתמש לעמוד הבית.
     *
     * @param v הרכיב שנלחץ (במקרה הזה – כפתור ההתחברות)
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        // איפוס הודעות שגיאה
        alartForUserName.setText("");
        alartForPassword.setText("");
        alartForEmail.setText("");
        alartForAll.setText("");
        alartForAll.setVisibility(View.INVISIBLE);

        if(v == submmitBTN){
            // הצגת שגיאות ולידציה
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
                            // שם משתמש קיים כבר
                            x[0] = true;
                        }
                    }
                    if (x[0]){
                        // שם משתמש קיים - ממשיכים לבדוק
                        if (checkUserName(
                                userName.getText().toString()) &&
                                checkPassword(
                                        password.getText().toString()) &&
                                checkEmail(
                                        email.getText().toString())) {

                            mAuth.signInWithEmailAndPassword(
                                    email.getText().toString(),
                                            password.getText().toString())
                                    .addOnCompleteListener(Log_in.this,
                                            new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // התחברות הצליחה
                                                Log.d(TAG, "התחברות הצליחה!");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                updateUI(user);
                                                //עובר לדף הבית
                                                Intent intent = new Intent(
                                                        Log_in.this,
                                                        Home_page.class);
                                                intent.addFlags(
                                                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                                // FLAG_ACTIVITY_CLEAR_TOP- מנקה את הדרך חזרה ומונע מהמשתמש לחזור למסכים קודמים עם כפתור Back.
                                                // FLAG_ACTIVITY_NEW_TASK - פותח את האקטיביטי החדשה בתוך טסק חדש. ועוזר "לנתק" את המסך החדש מהקודמים.
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // התחברות נכשלה
                                                Log.w(TAG, "התחברות נכשלה", task.getException());
                                                Toast.makeText(Log_in.this, "התחברות נכשלה", Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                            }
                                        }
                                    });
                        }
                        else {
                            alartForAll.setText(
                                    "אחד או יותר מהפרטי ההזדהות שהוכנסו שגויים! ");
                            alartForAll.setVisibility(View.VISIBLE);
                        }
                    }

                    if(!x[0]) {
                        alartForUserName.setText(
                                "* שם המשתמש שבחרת לא קיים " +
                                '\n' +
                                alartForUserName.getText().toString());
                    }
                }
            });
        }
    }

    /**
     * פעולה זו אחראית על עדכון ממשק המשתמש לאחר התחברות.
     * (כרגע לא ממומשת – אפשר להוסיף פעולות במידת הצורך)
     *
     * @param user המשתמש שהתחבר, או null במקרה של כישלון
     */
    private void updateUI(FirebaseUser user) {
        //  לממש בעתיד אם נרצה לעדכן את המסך לפי המשתמש
    }
}