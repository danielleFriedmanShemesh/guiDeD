package com.example.guided.Helpers;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class OperationsAndTripsHelper {
    private String ages = "";

    private String[] listAgeAdjustments; // מערך גילאים לבחירה
    private boolean[] checkedAgeAdjustments; //מערך בוליאני למעקב אחרי הבחירות של המשתמש בגילאים של החניכים
    private ArrayList<Integer> userAgeAdjustments; //רשימת אינדקסים של גילאים שהמשתמש בחר
    private String[] listArea; // מערך אזורים לבחירה
    private boolean[] checkedArea; //מערך בוליאני למעקב אחרי הבחירות של המשתמש באזורי הטיול
    private ArrayList<Integer> userArea; //רשימת אינדקסים של האזורים שהמשתמש בחר

    private Context context;


    public OperationsAndTripsHelper(Context context) {
        this.context = context;

        this.userAgeAdjustments = new ArrayList<>();
        this.listAgeAdjustments = context.getResources().getStringArray(R.array.age_adjustment);
        this.checkedAgeAdjustments = new boolean[listAgeAdjustments.length];

        this.userArea = new ArrayList<>();
        this.listArea = context.getResources().getStringArray(R.array.area_adjustment);
        this.checkedArea = new boolean[listArea.length];
    }



    public interface ExitDialogCallback {
        void onResult(boolean exitAndSave);
    }
    public interface PicDialogCallback {
        void onResult(ImageView pic);
    }
    public interface AgeDialogCallback {
        void onResult(String age);
    }
    public interface AreaDialogCallback {
        void onResult(String area);
    }

    public void showTripPic(
                ActivityResultLauncher< Intent > cameraLauncher,
                ActivityResultLauncher<Intent> galleryLauncher, PicDialogCallback callback){
            //creating a dialog for adding a profile picture from gallery or for taking a picture at the camera

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_add_pic);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            Button galery = dialog.findViewById(R.id.galery);
            Button camera = dialog.findViewById(R.id.camera);
            TextView title = dialog.findViewById(R.id.titleDialog);
            title.setText("העלאת תמונה");
        /*
          מאזין ללחיצה בדיאלוג תמונה – מפעיל את המצלמה או הגלריה בהתאם ללחיצה.
         */
            galery.setOnClickListener(new View.OnClickListener() {
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
            camera.setOnClickListener(new View.OnClickListener() {
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

    public void showAgeAdjustmentsDialog(AgeDialogCallback callback){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
            builder.setTitle("בחר את גיל החניכים: ");
            builder.setMultiChoiceItems(listAgeAdjustments, checkedAgeAdjustments, new DialogInterface.OnMultiChoiceClickListener() {
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
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String age = "";
                    for(int i = 0; i < userAgeAdjustments.size(); i++){
                        age = age + listAgeAdjustments[userAgeAdjustments.get(i)];
                        if (i != userAgeAdjustments.size() - 1){
                            age = age + ", ";
                        }
                    }
                    callback.onResult(age);
                }
            });
            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    public void shoeAreaDialog(AreaDialogCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("בחר את אזור הטיול: ");
        builder.setMultiChoiceItems(listArea, checkedArea, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    if(! userArea.contains(which)){
                        userArea.add(which);
                    }
                }
                else if (userArea.contains(which)){
                    userArea.remove(Integer.valueOf(which));
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = "";
                for(int i = 0; i < userArea.size(); i++){
                    a = a + listArea[userArea.get(i)];
                    if (i != userArea.size() - 1){
                        a = a + ", ";
                    }
                }
                callback.onResult(a);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showExitDialog(ExitDialogCallback callback){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.exit_activity_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button closeAndSave = dialog.findViewById(R.id.savedBtn);
        Button close = dialog.findViewById(R.id.unsavedBtn);

        closeAndSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onResult(true);

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onResult(false);
            }
        });
    }


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

    public void saveOperation(Operation operation, String operationKey){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("operations");

        String key;
        if(operationKey == null)
            key = myRef.push().getKey();
        else key = operationKey;
        operation.setKey(key);
        myRef.child(key).setValue(operation);
    }
}
