package com.example.guided.Activities;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button register; //כפתור להרשמה
    private Button logIn; //כפתור להתחברות

    /**
     * הפעולה onCreate מופעלת בעת יצירת האקטיביטי.
     * היא בודקת אם המשתמש כבר מחובר, ואם כן מעבירה אותו לדף הבית.
     * אחרת, היא מציגה את המסך עם כפתורי התחברות והרשמה.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי (אם קיים)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        View mainView = findViewById(R.id.main);
        setContentView(R.layout.activity_main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        else
            Log.e("MainActivity", "View with ID 'main' not found!");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // המשתמש מחובר, ממשיך לאפליקציה
            String userId = user.getUid();
            String email = user.getEmail();
            Log.e("MainActivity", "id ="+ userId+" email= "+ email);
            Intent intent = new Intent(this, Home_page.class);
            startActivity(intent);
            finish();
        }

        register=findViewById(R.id.signUp);
        register.setOnClickListener(this);
        logIn=findViewById(R.id.logIn);
        logIn.setOnClickListener(this);
    }

    /**
     * פעולה שמופעלת כאשר לוחצים על אחד הכפתורים.
     * אם נלחץ כפתור ההרשמה, עוברים למסך Register_num_one.
     * אם נלחץ כפתור ההתחברות, עוברים למסך Log_in.
     *
     * @param v ה־View שנלחץ (register או logIn)
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == register) {
            intent = new Intent(this,
                    Register_num_one.class);
            startActivity(intent);
        } else if (v == logIn) {
            intent = new Intent(this,
                    Log_in.class);
            startActivity(intent);
        }
    }
}

