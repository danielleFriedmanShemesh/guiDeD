package com.example.guided.Activities;

import static com.example.guided.Helpers.RegisteretionHelper.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.guided.Classes.User;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Helpers.DateConverter;
import com.example.guided.Helpers.RegisteretionHelper;
import com.example.guided.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Register_num_two היא מחלקת Activity שמייצגת את החלק השני בתהליך ההרשמה של המשתמש.
 * במסך זה המשתמש נדרש להזין כינוי, תאריך לידה, תנועת נוער, ולבחור תמונת פרופיל.
 * הנתונים נשמרים ב-Firebase Authentication וב-Firebase Realtime Database.

 * המחלקה תומכת בבחירת תאריך לידה, תנועת נוער, ותמונה מהמצלמה(ושמירתה בגלריה) או מהגלריה,
 * מבצעת ולידציות לכל השדות, וממשיכה למסך הבית או חוזרת למסך הקודם.

 * Extends: BaseActivity
 * Implements: View.OnClickListener
 */
public class Register_num_two extends BaseActivity implements View.OnClickListener {
    private TextView organization; // שדה לבחירת תנועת נוער
    private EditText birthday; // שדה להזנת תאריך לידה
    private EditText nickName; // שדה להזנת כינוי
    private ImageView profile; // תצוגת תמונה של פרופיל
    private ImageView backBTN; // כפתור חזור
    private ImageView saveBTN; // כפתור שמירה
    private TextView alartForNickName; //טקסט שגיאה לכינוי
    private TextView alartForOrganization; // טקסט שגיאה לתנועת נוער
    private TextView alartForBirthday; // טקסט שגיאה לתאריך לידה
    private User newUser; // אובייקט User שעובר מהמסך הקודם
    private String password; // סיסמה מהמסך הקודם
    private String email; //אימייל מהשלב הקודם

    private FirebaseAuth mAuth; // מופע Firebase Authentication
    private ProgressBar progressBar; //  תצוגת עיגול טעינה

    private ActivityResultLauncher<Intent> cameraLauncher; // לאונצ'ר להפעלת המצלמה
    private ActivityResultLauncher<Intent> galleryLauncher; //  לאונצ'ר לפתיחת גלריה

    /**
     * אתחול ראשוני של ה-Activity.
     * קובע את ה-Layout, מאתחל את המשתנים הגרפיים, מאזינים לאירועים, טוען נתונים מהמסך הקודם,
     * ומכין את ה-launchers למצלמה וגלריה.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי (אם קיים)
     */
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

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(this);

        nickName = findViewById(R.id.nickName);

        birthday = findViewById(R.id.birthday);
        birthday.setOnClickListener(this);

        backBTN = findViewById(R.id.back);
        backBTN.setOnClickListener(this);

        saveBTN = findViewById(R.id.save);
        saveBTN.setOnClickListener(this);

        organization = findViewById(R.id.organization);
        organization.setOnClickListener(this);

        alartForOrganization = findViewById(R.id.alartOrganization);
        alartForBirthday = findViewById(R.id.alartBirthday);
        alartForNickName = findViewById(R.id.alartNickName);

        //מגדיר שדות עם נתונים מאובייקט User  שחזרו ממסך ההרשמה הראשון כאקסטרה
        Serializable user = getIntent().
                getSerializableExtra("newUser");
        if (user instanceof User){
            newUser = (User) user;
            organization.setText(
                    newUser.getOrganization());
            Date currentDate = new Date();
            Date thisDate = newUser.getBirthday();
            if((thisDate.getDate() == currentDate.getDate()) &&
                    (thisDate.getMonth() == currentDate.getMonth()) &&
                    (thisDate.getYear() == currentDate.getYear())) {
                birthday.setText("");
            }
            else {
                birthday.setText(
                        DateConverter.convertFullFormatToDate(
                                newUser.getBirthday().
                                        toString()));
            }
            String profileImageStr = newUser.getProfileImage();
            if(!profileImageStr.isEmpty()){
                profile.setImageBitmap(
                    BitmapHelper.stringToBitmap(profileImageStr));
            }
            nickName.setText(newUser.getNickName());
            password = newUser.getPassword();
            email = newUser.getEmail();
        }

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            Intent data = o.getData();
                            if (data != null) {
                                Bitmap bitmap = (Bitmap) data.
                                        getExtras().
                                        get("data");
                                profile.setImageBitmap(bitmap);
                                RegisteretionHelper.saveImageToGallery(
                                        bitmap,
                                        Register_num_two.this);
                            }
                        }
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            Intent data = o.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                try {
                                    Bitmap bitmap = MediaStore.
                                            Images.
                                            Media.
                                            getBitmap(
                                                    Register_num_two.this.getContentResolver(),
                                                    imageUri);
                                    profile.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    throw new RuntimeException(e); //מדפיס פרטי שגיאה
                                }
                            }
                        }
                    }
                }
        );
    }

    /**
     * מאזין ללחיצות על כל אחד מהאלמנטים הגרפיים:
     * - כפתור תמונה: מאפשר לבחור תמונת פרופיל.
     * - תאריך לידה: מציג דיאלוג לבחירת תאריך.
     * - תנועת נוער: מציג דיאלוג לבחירת תנועה.
     * - כפתור 'שמירה': מבצע ולידציה, שומר את המשתמש ב-Firebase, ופותח את דף הבית.
     * - כפתור 'חזור': שומר את הנתונים המקומיים וחוזר למסך ההרשמה הראשון.
     *
     * @param v ה-View שעליו נלחץ
     */
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View v) {
        alartForNickName.setText("");
        alartForOrganization.setText("");
        alartForBirthday.setText("");

        // האזנה ללחיצה על הIMAGEVIEW של הפרופיל
        if (v == profile) {
            RegisteretionHelper.setPic(
                    Register_num_two.this,
                    cameraLauncher, galleryLauncher, new PicDialogCallback() {
                        @Override
                        public void onResult(int pic) {
                            profile.setImageResource(pic);

                        }
                    });
        }

        // האזנה ללחיצה על הEDITTEXT של תאריך היומולדת
        if (v == birthday){
            if (birthday != null) {
                RegisteretionHelper.setBirthdate(
                        Register_num_two.this,
                        new BirthdayCallback() {
                    @Override
                    public void onBirthdaySelected(String b) {
                        birthday.setText(b);
                    }
                });
            }
        }

        // האזנה ללחיצה על הTEXTVIEW של מסגרת הדרכה
        if (v == organization){
            if (organization != null) {
                RegisteretionHelper.setOrganization(
                        Register_num_two.this,
                        new OrganizationCallback() {
                    @Override
                    public void onOrganizationSelected(String o) {
                        organization.setText(o);
                    }
                });
            }
        }

        // האזנה ללחיצה על כפתור השמירה
        if (v == saveBTN) {
            alartForNickName.setText(
                    RegisteretionHelper.
                            checkAlertForNickName(
                                    nickName.getText().toString()));
            alartForBirthday.setText(
                    RegisteretionHelper.
                            checkAlertsForBirthday(
                                    birthday.getText().toString()));
            alartForOrganization.setText(
                    RegisteretionHelper.
                            checkAlertsForOrganization(
                                    organization.getText().toString()));

            //בדיקות אחרונות לפני הוספת משתמש חדש לדאטה בייס
            if (RegisteretionHelper.
                    checkOrganization(
                            organization.getText().toString()) &&
                    RegisteretionHelper.
                            checkBirthday(
                                    birthday.getText().toString()) &&
                    RegisteretionHelper.
                            checkNickName(
                                    nickName.getText().toString()) &&
                    RegisteretionHelper.
                            checkPic(
                                    ((BitmapDrawable)profile.getDrawable()).
                                            getBitmap()))
            {

                progressBar.setVisibility(View.VISIBLE);
                saveUser();

                //open the home page after the user had been saved in the database
                Intent intent = new Intent(
                        this,
                        Home_page.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        // if you click on the 'back' button
        if(v == backBTN){

            //saving the inputs that the user entered at an User object
            newUser.setNickName(
                    nickName.getText().toString());
            newUser.setOrganization(
                    organization.getText().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy", Locale.getDefault());
            try {
                if (!birthday.getText().toString().isEmpty())
                 newUser.setBirthday(
                        dateFormat.parse(
                                birthday.getText().toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            newUser.setProfileImage(
                    BitmapHelper.bitmapToString(
                            ((BitmapDrawable)profile.getDrawable())
                                    .getBitmap()));

            //returns to the first register activity and transport the User object as an extra
            Intent intent = new Intent(
                    this,
                    Register_num_one.class);
            intent.putExtra("newUser",  newUser);
            startActivity(intent);
            finish();
        }
    }


    /**
     * שומר את המשתמש במסד הנתונים של Firebase.
     * יוצר חשבון חדש ב-Firebase Authentication עם האימייל והסיסמה שהוזנו במסך הקודם,
     * ואז שומר את כל פרטי המשתמש (כולל כינוי, תאריך לידה, תנועה, ותמונה) ב-Firebase Realtime Database.
     */
    public void saveUser() {
        // Write a message to the database

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(newUser.getUserName(),
                                    newUser.getPassword(),
                                    newUser.getEmail(),
                                    nickName.getText().toString(),
                                    organization.getText().toString(),
                                    DateConverter.
                                            convertStringToDate(
                                                    birthday.getText().toString()),
                                    BitmapHelper.bitmapToString(
                                            ((BitmapDrawable)profile.getDrawable())
                                                    .getBitmap()));

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.
                                    getReference(
                                            "users").
                                    child(FirebaseAuth.
                                            getInstance().
                                            getCurrentUser().
                                            getUid());
                            myRef.setValue(user).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });
    }
}