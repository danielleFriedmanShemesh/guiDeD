package com.example.guided.RecyclerAdapters;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guided.Activities.Add_trip;
import com.example.guided.Helpers.BitmapHelper;
import com.example.guided.Classes.Part;
import com.example.guided.R;

import java.util.ArrayList;

public class RecyclerAdapterTrip  extends RecyclerView.Adapter<RecyclerAdapterTrip.ViewHolder>{

    private ArrayList<Part> partsArrayList;
    private Context context;

    private RecyclerAdapterTrip.OnPartListChangedListener listener;

    public void setOnPartListChangedListener(RecyclerAdapterTrip.OnPartListChangedListener listener) {
        this.listener = listener;
    }

    public interface OnPartListChangedListener {
        void onPartListChanged(ArrayList<Part> parts);
    }

    public interface OnImagePickerRequestedListener {
        void onCameraRequested(int position, ImageView imageView);
        void onGalleryRequested(int position, ImageView imageView);
    }

    private OnImagePickerRequestedListener imagePickerListener;

    public void setOnImagePickerRequestedListener(OnImagePickerRequestedListener listener) {
        this.imagePickerListener = listener;
    }




    public RecyclerAdapterTrip(ArrayList<Part> partsArrayList, Context context) //, Map<Integer, Bitmap> tempImages
    {
        this.partsArrayList = partsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapterTrip.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.one_part_layout, parent, false);
        RecyclerAdapterTrip.ViewHolder viewHolder = new RecyclerAdapterTrip.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterTrip.ViewHolder holder, int position) {
//TODO: לעשות אפשרות מחיקת תמונה
        Part part = partsArrayList.get(position);
        holder.activityType.setText(part.getActivityType());
        holder.lengthInMinute.setText(String.valueOf(part.getLengthInMinute()));
        holder.lengthInKM.setText(String.valueOf(part.getLengthInKM()));
        holder.description.setText(part.getDescription());
        holder.equipment.setText(part.getEquipment());
        //if (part.getPicture() != null)
            holder.picture.setImageBitmap(BitmapHelper.stringToBitmap(part.getPicture()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partsArrayList != null){
                    int id;
                    Part part = null;
                    //Edit the part
                    part = partsArrayList.get(holder.getBindingAdapterPosition());
                    id = part.getId();

                    Dialog partDialog;
                    partDialog = new Dialog(context);
                    partDialog.setContentView(R.layout.part_layout);
                    partDialog.setCancelable(true);

                    TextView activityTypeTV;
                    EditText lengthInMinuteET;
                    EditText lengthInKM_ET;
                    EditText descriptionET;
                    EditText equipmentET;
                    ImageView pictureIV;
                    Button savePartBTN;

                    activityTypeTV = partDialog.findViewById(R.id.activity);
                    lengthInMinuteET = partDialog.findViewById(R.id.lengthInMinutes);
                    lengthInKM_ET = partDialog.findViewById(R.id.lengthInKM);
                    descriptionET = partDialog.findViewById(R.id.description);
                    equipmentET = partDialog.findViewById(R.id.equipment);
                    pictureIV = partDialog.findViewById(R.id.picture);
                    savePartBTN = partDialog.findViewById(R.id.savePart);

                    activityTypeTV.setText(part.getActivityType());
                    lengthInMinuteET.setText(String.valueOf(part.getLengthInMinute()));
                    lengthInKM_ET.setText(String.valueOf(part.getLengthInKM()));
                    descriptionET.setText(part.getDescription());
                    equipmentET.setText(part.getEquipment());
                    pictureIV.setImageBitmap(BitmapHelper.stringToBitmap(part.getPicture()));
                    ((Add_trip) context).currentDialogImageView = pictureIV;

                    //       if (tempImages.containsKey(position))
              //          pictureIV.setImageBitmap(tempImages.get(position));

                    String[] listActivityTypeAdjustments = context.getResources().getStringArray(R.array.activity_type_adjustment);;
                    boolean[] checkedActivityTypeAdjustments = new boolean[listActivityTypeAdjustments.length];;
                    ArrayList<Integer> userActivityTypeAdjustments = new ArrayList<>();
                    activityTypeTV.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
                            builder.setTitle("בחר את סוג הפעילות ");
                            builder.setMultiChoiceItems(listActivityTypeAdjustments, checkedActivityTypeAdjustments, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if(isChecked){
                                        if(! userActivityTypeAdjustments.contains(which)){
                                            userActivityTypeAdjustments.add(which);
                                            if (listActivityTypeAdjustments.equals("אחר")){
                                                //TODO: לתת אפשרות לכתוב משהו שלא מופיע כאחד מהאופציות
                                            }
                                        }

                                    }
                                    else if (userActivityTypeAdjustments.contains(which)){
                                        userActivityTypeAdjustments.remove(Integer.valueOf(which));
                                    }
                                }
                            });
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String type = "";
                                    for(int i = 0; i < userActivityTypeAdjustments.size(); i++){
                                        type = type + listActivityTypeAdjustments[userActivityTypeAdjustments.get(i)];
                                        if (i != userActivityTypeAdjustments.size() - 1){
                                            type = type + ", ";
                                        }
                                    }
                                    activityTypeTV.setText(type);
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
                    });


                    pictureIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_add_pic);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.show();

                            Button galery = dialog.findViewById(R.id.galery);
                            Button camera = dialog.findViewById(R.id.camera);
                            TextView title = dialog.findViewById(R.id.titleDialog);
                            title.setText("העלאת תמונה");

                            galery.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (imagePickerListener != null) {
                                        dialog.dismiss();
                                        imagePickerListener.onGalleryRequested(holder.getBindingAdapterPosition(),pictureIV);
                                    }
                                }
                            });
                            camera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (imagePickerListener != null) {
                                        dialog.dismiss();
                                        imagePickerListener.onCameraRequested(holder.getBindingAdapterPosition(),pictureIV);
                                    }
                                }
                            });
                        }
                    });




                    savePartBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int partLengthInMinuteInt = Integer.parseInt(lengthInMinuteET.getText().toString());
                            double partLengthInKMInt = Double.parseDouble(lengthInKM_ET.getText().toString());
                            String activityTypeStr = activityTypeTV.getText().toString();
                            String descriptionStr = descriptionET.getText().toString();
                            String equipmentStr = equipmentET.getText().toString();
                            String pictureSTR = BitmapHelper.bitmapToString(
                                    ((BitmapDrawable)pictureIV.getDrawable())
                                            .getBitmap());

                            Part editPart = new Part(activityTypeStr, partLengthInMinuteInt, partLengthInKMInt, descriptionStr, equipmentStr, pictureSTR, id);

                            partsArrayList.set(id, editPart);

                            holder.activityType.setText(editPart.getActivityType());
                            holder.lengthInMinute.setText(String.valueOf(editPart.getLengthInMinute()));
                            holder.lengthInKM.setText(String.valueOf(editPart.getLengthInKM()));
                            holder.description.setText(editPart.getDescription());
                            holder.equipment.setText(editPart.getEquipment());
                            holder.picture.setImageBitmap(BitmapHelper.stringToBitmap(editPart.getPicture()));

                            if (listener != null) {
                                listener.onPartListChanged(partsArrayList);
                            }

                            partDialog.dismiss();
                        }
                    });
                    partDialog.show();
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        if(partsArrayList == null){
            return 0;
        }
        else{
            return partsArrayList.size();}
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView activityType;
        TextView lengthInMinute;
        TextView lengthInKM;
        TextView description;
        TextView equipment;
        ImageView picture;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            activityType = itemView.findViewById(R.id.activity);
            lengthInMinute = itemView.findViewById(R.id.lengthInMinutes);
            lengthInKM = itemView.findViewById(R.id.lengthInKM);
            description = itemView.findViewById(R.id.description);
            equipment = itemView.findViewById(R.id.equipment);
            parentLayout = itemView.findViewById(R.id.onePartLayout);
            picture = itemView.findViewById(R.id.picture);
        }


        public TextView getActivityType() {
            return activityType;
        }

        public TextView getLengthInMinute() {
            return lengthInMinute;
        }

        public TextView getLengthInKM() {
            return lengthInKM;
        }

        public ImageView getPicture() {
            return picture;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getEquipment() {
            return equipment;
        }


    }
}
