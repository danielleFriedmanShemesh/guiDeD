package com.example.guided.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.example.guided.Activities.Add_trip;
import com.example.guided.Classes.Operation;
import com.example.guided.Classes.Trip;
import com.example.guided.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * מחלקה זו מסייעת בניהול דיאלוגים ופעולות הקשורות לטיולים ופעולות במערכת.
 * כוללת דיאלוגים לבחירת גיל, אזור, סוג פעילות, הוספת תמונה ושמירת מידע במסד הנתונים של Firebase.
 */
public class OperationsAndTripsHelper {
    private String[] listAgeAdjustments; // מערך גילאים לבחירה
    private boolean[] checkedAgeAdjustments; //מערך בוליאני למעקב אחרי הבחירות של המשתמש בגילאים של החניכים
    private ArrayList<Integer> userAgeAdjustments; //רשימת אינדקסים של גילאים שהמשתמש בחר
    private String[] listArea; // מערך אזורים לבחירה
    private boolean[] checkedArea; //מערך בוליאני למעקב אחרי הבחירות של המשתמש באזורי הטיול
    private ArrayList<Integer> userArea; //רשימת אינדקסים של האזורים שהמשתמש בחר
    private String[] listActivityTypeAdjustments; // מערך סוגי פעילויות לבחירה
    private boolean[] checkedActivityTypeAdjustments; //מערך בוליאני למעקב אחרי הבחירות של המשתמש בסוג הפעילות במקטע
    private ArrayList<Integer> userActivityTypeAdjustments; //רשימת אינדקסים של סוג הפעילויות שהמשתמש בחר
    private Context context; //הקשר (Context) של האקטיביטי או הפרגמנט שממנו נקראת המחלקה

    /**
     * בנאי המקבל את הקשר ומממש את כל המערכים והנתונים הדרושים לדיאלוגים.
     * @param context הקשר שממנו מופעלת המחלקה
     */
    public OperationsAndTripsHelper(Context context) {
        this.context = context;

        this.userAgeAdjustments = new ArrayList<>();
        this.listAgeAdjustments = context
                .getResources()
                .getStringArray(
                        R.array.age_adjustment);
        this.checkedAgeAdjustments = new boolean[listAgeAdjustments.length];

        this.userArea = new ArrayList<>();
        this.listArea = context
                .getResources()
                .getStringArray(
                        R.array.area_adjustment);
        this.checkedArea = new boolean[listArea.length];

        this.userActivityTypeAdjustments = new ArrayList<>();
        this.listActivityTypeAdjustments = context
                .getResources()
                .getStringArray(
                        R.array.activity_type_adjustment);
        this.checkedActivityTypeAdjustments = new boolean[listActivityTypeAdjustments.length];
    }

    /** ממשק Callback לדיאלוג יציאה */
    public interface ExitDialogCallback {
        void onResult(boolean exitAndSave);
    }

    /** ממשק Callback לדיאלוג תמונה */
    public interface PicDialogCallback {
        void onResult(ImageView pic);
    }

    /** ממשק Callback לבחירת גיל */
    public interface AgeDialogCallback {
        void onResult(String age);
    }

    /** ממשק Callback לבחירת אזור */
    public interface AreaDialogCallback {
        void onResult(String area);
    }

    /** ממשק Callback לבחירת סוג פעילות */
    public interface ActivityTypeDialogCallback {
        void onResult(String ActivityType);
    }

    /**
     * מציג דיאלוג לבחירת תמונה – מצלמה, גלריה או מחיקה.
     * @param cameraLauncher המשגר להפעלת המצלמה
     * @param galleryLauncher המשגר לפתיחת הגלריה
     * @param callback פונקציה חוזרת עם התמונה שנבחרה
     */
    public void showTripPic(
            ActivityResultLauncher< Intent > cameraLauncher,
            ActivityResultLauncher<Intent> galleryLauncher,
            PicDialogCallback callback){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_pic);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button galery = dialog.findViewById(R.id.galery);
        Button camera = dialog.findViewById(R.id.camera);
        Button delete = dialog.findViewById(R.id.delete);
        TextView title = dialog.findViewById(R.id.titleDialog);
        title.setText("העלאת תמונה");
        /*
          מאזין ללחיצה בדיאלוג תמונה – מפעיל את המצלמה או הגלריה או מחיקה בהתאם ללחיצה.
         */
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory
                        .decodeResource(
                                context.getResources(),
                                R.drawable.add_image); // טוען את התמונה
                ((Add_trip) context).currentDialogImageView.setImageBitmap(bitmap); // מציג אותה בתוך ה-ImageView
                dialog.dismiss();
                callback.onResult(((Add_trip) context).currentDialogImageView);
            }
        });
            galery.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Intent.ACTION_PICK → Opens an app to let the user pick something.
                    //MediaStore.Images.Media.EXTERNAL_CONTENT_URI → Gets images from external storage (Gallery).
                    galleryLauncher.launch(galleryIntent);
                    dialog.dismiss();
                    callback.onResult(((Add_trip) context).currentDialogImageView);
                }
            });
            camera.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraLauncher.launch(cameraIntent);
                    dialog.dismiss();
                    callback.onResult(((Add_trip) context).currentDialogImageView);
                }
            });
    }

    /**
     * מציג דיאלוג רב-בחירה לבחירת גילאים מתוך רשימת גילאים.
     * @param callback פונקציה חוזרת עם הגילאים שנבחרו כמחרוזת
     */
    public void showAgeAdjustmentsDialog(
            AgeDialogCallback callback){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context,
                    R.style.AlertDialog);
            builder.setTitle("בחר את גיל החניכים: ");
            builder.setMultiChoiceItems(listAgeAdjustments,
                    checkedAgeAdjustments,
                    new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        if(! userAgeAdjustments.contains(which)){
                            userAgeAdjustments.add(which);
                        }
                    }
                    else if (userAgeAdjustments.contains(which)){
                        userAgeAdjustments.remove(Integer.valueOf(which));
                    }
                }
            });
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(
                        DialogInterface dialog,
                        int which) {
                    String age = "";
                    for(int i = 0; i < userAgeAdjustments.size(); i++){
                        age = age + listAgeAdjustments[userAgeAdjustments.get(i)];
                        if (i != userAgeAdjustments.size() - 1)
                            age = age + ", ";
                    }
                    callback.onResult(age);
                }
            });
            builder.setNegativeButton(
                    "Dismiss",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(
                        DialogInterface dialog,
                        int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    /**
     * מציג דיאלוג רב-בחירה לבחירת סוגי פעילות.
     * @param callback פונקציה חוזרת עם סוגי הפעילויות שנבחרו כמחרוזת
     */
    public void showActivityDialog(ActivityTypeDialogCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context,
                R.style.AlertDialog);
        builder.setTitle("בחר את סוג הפעילות ");
        builder.setMultiChoiceItems(
                listActivityTypeAdjustments,
                checkedActivityTypeAdjustments,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    if(! userActivityTypeAdjustments.contains(which)){
                        userActivityTypeAdjustments.add(which);
                        if (listActivityTypeAdjustments[which].equals("אחר")){
                            // TODO: לתת אפשרות לכתוב משהו שלא מופיע כאחד מהאופציות
                        }
                    }
                }
                else if (userActivityTypeAdjustments.contains(which))
                    userActivityTypeAdjustments.remove(Integer.valueOf(which));
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "";
                for(int i = 0; i < userActivityTypeAdjustments.size(); i++){
                    type = type + listActivityTypeAdjustments[userActivityTypeAdjustments.get(i)];
                    if (i != userActivityTypeAdjustments.size() - 1)
                        type = type + ", ";
                }
                callback.onResult(type);
            }
        });
        builder.setNegativeButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * מציג דיאלוג רב-בחירה לבחירת אזורי טיול.
     * @param callback פונקציה חוזרת עם האזורים שנבחרו כמחרוזת
     */
    public void showAreaDialog(
            AreaDialogCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context,
                R.style.AlertDialog);
        builder.setTitle("בחר את אזור הטיול: ");
        builder.setMultiChoiceItems(
                listArea,
                checkedArea,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(
                    DialogInterface dialog,
                    int which,
                    boolean isChecked) {
                if(isChecked){
                    if(! userArea.contains(which))
                        userArea.add(which);
                }
                else if (userArea.contains(which))
                    userArea.remove(Integer.valueOf(which));
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = "";
                for(int i = 0; i < userArea.size(); i++){
                    a = a + listArea[userArea.get(i)];
                    if (i != userArea.size() - 1)
                        a = a + ", ";
                }
                callback.onResult(a);
            }
        });
        builder.setNegativeButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * מציג דיאלוג לשאלה האם לצאת מהמסך תוך שמירה או בלי שמירה.
     * @param callback פונקציה חוזרת עם התשובה (true = לצאת ולשמור)
     */
    public void showExitDialog(ExitDialogCallback callback){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.exit_activity_dialog_layout);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button closeAndSave = dialog.findViewById(R.id.savedBtn);
        Button close = dialog.findViewById(R.id.unsavedBtn);

        closeAndSave.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onResult(true);
            }
        });
        close.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onResult(false);
            }
        });
    }

    /**
     * שומר טיול במסד הנתונים Firebase. אם לא ניתן מפתח, נוצר חדש.
     * @param t אובייקט הטיול לשמירה
     * @param keyTrip המפתח של הטיול (יכול להיות null)
     */
    public void saveTrip(Trip t,String keyTrip){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("trips");
                String key;
                if(keyTrip == null)
                    key = myRef.push().getKey();
                else
                    key = keyTrip;
                t.setKey(key);
                myRef.child(key).setValue(t);
    }

    /**
     * שומר פעולה במסד הנתונים Firebase. אם לא ניתן מפתח, נוצר חדש.
     * @param operation אובייקט הפעולה לשמירה
     * @param operationKey המפתח של הפעולה (יכול להיות null)
     */
    public void saveOperation(Operation operation, String operationKey){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("operations");

        String key;
        if(operationKey == null)
            key = myRef.push().getKey();
        else
            key = operationKey;
        operation.setKey(key);
        myRef.child(key).setValue(operation);
    }
}
