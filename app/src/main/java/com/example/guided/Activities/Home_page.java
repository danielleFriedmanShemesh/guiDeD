package com.example.guided.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.guided.Fragments.HomeFragment;
import com.example.guided.Fragments.LibraryOperationsFragment;
import com.example.guided.Fragments.LibraryTripsFragment;
import com.example.guided.Fragments.ProfileFragment;
import com.example.guided.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
        * מחלקה זו מייצגת את מסך הבית של האפליקציה לאחר שהמשתמש התחבר.
 * מציג את המסך הראשי ומאפשר ניווט דרך התפריט העליון ותפריט התחתון (BottomNavigationView).
 * המחלקה מטפלת בלחיצות בתפריט, מציגה תפריטים קופצים, ומחליפה פרגמנטים בהתאם.
 * בנוסף, מוודאת שלא ניתן לחזור אחורה במסכים אם המשתמש כבר מחובר.
 */
public class Home_page extends BaseActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ImageView menuBTN; //כפתור התפריט שנמצא בפינה העליונה
    private BottomNavigationView navView; //תפריט ניווט תחתון - האלמנט שמאפשר לעבור בין אזורים שונים

    /**
     * מופעלת בעת יצירת המסך. אחראית על אתחול התצוגה והרכיבים הגרפיים,
     * כולל הטענת פרגמנט הבית כברירת מחדל, קישור כפתורים והאזנה ללחיצות.
     *
     * @param savedInstanceState מצב שמור של הפעילות (אם קיים).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // גורם לתצוגה להימתח עד קצה המסך (כולל מתחת לסטטוס בר)
        setContentView(R.layout.activity_home_page);
        // מאזין ל-Insets (כמו שולי סטטוס בר וניווט) כדי להוסיף Padding לרכיב הראשי
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // נטען כברירת מחדל את הפרגמנט הראשי
        replaceFragment(new HomeFragment());

        // איתחול כפתור התפריט העליון
        menuBTN = findViewById(R.id.option_btn);
        menuBTN.setOnClickListener(this); // מאזין ללחיצה

        // איתחול התפריט התחתון
        navView = findViewById(R.id.bottomNavigationView);
        navView.setItemIconTintList(null);  // נותן אייקונים בצבע מקורי ולא אפור

        navView.setOnNavigationItemSelectedListener(this); //האזנה לאירועים


    }

    /**
     * פעולה שמופעלת כאשר נלחץ אחד מהכפתורים במסך.
     * משמשת כאן לזיהוי לחיצה על כפתור התפריט (menuBTN).
     *
     * @param v הרכיב שעליו נלחץ.
     */
    @Override
    public void onClick(View v) {
        if(v == menuBTN){
            showMenu(v);
        }
    }

    /**
     * מציג תפריט קופץ (PopupMenu) עבור המשתמש עם אפשרויות ניווט שונות.
     *
     * @param v הרכיב שלחיצה עליו מפעילה את התפריט.
     */
    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener.
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_main);

        popup.show();
    }

    /**
     * פעולה שמופעלת כאשר נבחר פריט מתפריט הקונטקסט.
     * מבצעת פעולות ניווט בהתאם לבחירת המשתמש.
     *
     * @param item הפריט שנבחר בתפריט.
     * @return true אם הבחירה טופלה, אחרת false.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        int id= item.getItemId();
        if(id == R.id.action_profile){
            return true;
        }
        else if(id ==  R.id.action_add_operation){
            intent = new Intent(this, Add_operation.class);
            startActivity(intent);
            return true;
        }
        else if(id ==  R.id.action_add_trip){
            intent = new Intent(this, Add_trip.class);
            startActivity(intent);
            return true;
        }
        else if(id ==  R.id.action_library_operations){
            replaceFragment(new LibraryOperationsFragment());
            return true;
        }
        else if(id ==  R.id.action_library_trips){
            replaceFragment(new LibraryTripsFragment());
            return true;
        }
        else if(id ==  R.id.action_home){
            replaceFragment(new HomeFragment());
            return true;
        }
        else if (id == R.id.log_out){
            FirebaseAuth.getInstance().signOut();

            Intent intent1 = new Intent(this,
                    MainActivity.class);
            intent1.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            // FLAG_ACTIVITY_CLEAR_TOP- מנקה את הדרך חזרה ומונע מהמשתמש לחזור למסכים קודמים עם כפתור Back.
            // FLAG_ACTIVITY_NEW_TASK - פותח את האקטיביטי החדשה בתוך טסק חדש. ועוזר "לנתק" את המסך החדש מהקודמים.
            startActivity(intent1);
            finish();

        }
        return false;
    }

    /**
            * מחליף את הפרגמנט המוצג בפריים הראשי בפרגמנט חדש.
            *
            * @param fragment הפרגמנט החדש שצריך להציג.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * פעולה המופעלת בעת בחירה בפריט מתפריט הניווט התחתון.
     * מציגה דיאלוג מותאם לבחירה או מחליפה פרגמנט בהתאם.
     *
     * @param item פריט שנבחר בניווט התחתון.
     * @return true אם הבחירה טופלה, אחרת false.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        int id= item.getItemId();
        if(id == R.id.profile){
            replaceFragment(new ProfileFragment());
            return true;
        }
        else if(id ==  R.id.add){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.bottom_sheet_layout);
            TextView addOpTv = dialog.findViewById(R.id.op);
            TextView addTrTv = dialog.findViewById(R.id.tr);
            addTrTv.setText("הוספת טיול");
            addOpTv.setText("הוספת פעולה");

            LinearLayout addOP = dialog.findViewById(R.id.opLl);
            addOP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(Home_page.this,
                            Add_operation.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            LinearLayout addTR = dialog.findViewById(R.id.trLl);
            addTR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(Home_page.this,
                            Add_trip.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(
                            Color.TRANSPARENT));
            dialog.getWindow().setGravity(
                    Gravity.BOTTOM);
            return true;
        }

        else if(id ==  R.id.library){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.bottom_sheet_layout);
            TextView libraryOpTv = dialog.findViewById(R.id.op);
            TextView libraryTrTv = dialog.findViewById(R.id.tr);
            libraryTrTv.setText("מאגר טיולים");
            libraryOpTv.setText("מאגר פעולות");

            LinearLayout libraryOP = dialog.findViewById(R.id.opLl);
            libraryOP.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(
                            new LibraryOperationsFragment());
                    dialog.dismiss();
                }
            });
            LinearLayout libraryTR = dialog.findViewById(R.id.trLl);
            libraryTR.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceFragment(
                            new LibraryTripsFragment());
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(
                            Color.TRANSPARENT));
            dialog.getWindow().setGravity(
                    Gravity.BOTTOM);

            return true;
        }

        else if(id ==  R.id.home){
            replaceFragment(
                    new HomeFragment());
            return true;
        }
        return false;
    }

    /**
     * פעולה שמטפלת בלחיצה על כפתור החזרה (Back).
     * אם המשתמש מחובר - לא מאפשרת חזרה למסכים קודמים.
     */
    @Override
    public void onBackPressed() {
        FirebaseUser user = FirebaseAuth.
                getInstance().
                getCurrentUser();
        if (user == null) {
            super.onBackPressed();  // במידה ואין משתמש מחובר, נשאיר את ההתנהגות הרגילה
        }
    }
}