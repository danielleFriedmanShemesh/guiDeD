package com.example.guided.Helpers;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

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

import com.example.guided.Activities.Add_operation;
import com.example.guided.Activities.Add_trip;
import com.example.guided.R;

public class OperationsAndTripsHelper {
    public interface ExitDialogCallback {
        void onResult(boolean exitAndSave);
    }
    public interface PicDialogCallback {
        void onResult(ImageView pic);
    }

    public static void showTripPic(
                Context context,
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

//    public static String showAgeAdjustmentsDialog(Context context){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
//            builder.setTitle("בחר את גיל החניכים: ");
//            builder.setMultiChoiceItems(listAgeAdjustments, checkedAgeAdjustments, new DialogInterface.OnMultiChoiceClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                    if(isChecked){
//                        if(! userAgeAdjustments.contains(which)){
//                            userAgeAdjustments.add(which);
//                        }
//                    }
//                    else if (userAgeAdjustments.contains(which)){
//                        userAgeAdjustments.remove(Integer.valueOf(which));
//                    }
//                }
//            });
//            builder.setCancelable(false);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String age = "";
//                    for(int i = 0; i < userAgeAdjustments.size(); i++){
//                        age = age + listAgeAdjustments[userAgeAdjustments.get(i)];
//                        if (i != userAgeAdjustments.size() - 1){
//                            age = age + ", ";
//                        }
//                    }
//                    ageAdjustments.setText(age);
//                }
//            });
//            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//    }
    public static void showExitDialog(Context context,
                                         ExitDialogCallback callback){

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


//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
//        builder.setTitle("סגירת חלון");
//        builder.setMessage("תרצו לשמור את השינויים?");
//        builder.setCancelable(true);
//        builder.setPositiveButton("שמור",new AlartDialogLostenerSave());
//        builder.setNegativeButton("אל תשמור", new AlartDialogLostenerSave());
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }
//    public static class AlartDialogLostenerSave implements DialogInterface.OnClickListener {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            if(which == BUTTON_POSITIVE){
//                exitAndSave = true;
//
//            }
//            else if(which == BUTTON_NEGATIVE){
//                exitAndSave = false;
//            }
//        }
//    }
}
