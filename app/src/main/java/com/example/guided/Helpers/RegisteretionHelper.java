package com.example.guided.Helpers;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guided.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisteretionHelper extends AppCompatActivity {

    private static final Bitmap[] picture = new Bitmap[1];
    private static ActivityResultLauncher<Intent> cameraLauncher;
    private static ActivityResultLauncher<Intent> galleryLauncher;

    public RegisteretionHelper() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            picture[0] = bitmap;
                        }
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.
                                        Images.
                                        Media.
                                        getBitmap(
                                                this.getContentResolver(),
                                                imageUri);
                                picture[0] = bitmap;

                            } catch (IOException e) {
                                throw new RuntimeException(e); //מדפיס פרטי שגיאה
                            }
                        }
                    }
                }
        );
    }

    //TODO: לעשות גם לתמונה

    public static Bitmap setPic(Context context){
        //creating a dialog for adding a profile picture from gallery or for taking a picture at the camera
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("העלאת תמונת פרופיל");
        builder.setMessage("תרצו להעלות תמונה מהגלריה או לצלם תמונה במצלמה?");
        builder.setCancelable(true);
        builder.setPositiveButton("מצלמה", new HandleAlartDialogLostener());
        builder.setNegativeButton("גלריה", new HandleAlartDialogLostener());
        AlertDialog dialog = builder.create();
        dialog.show();
        return picture[0];
    }

    public static String setOrganization(Context context){
        final String[] organization = new String[1];
        String[] listOrganizations = context.
                getResources().
                getStringArray(
                        R.array.organization_adjustment);
        Dialog dialog = new Dialog(context);

        // set custom dialog
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        // show dialog
        dialog.show();

        // Initialize and assign variable
        EditText editText = dialog.findViewById(R.id.search);
        ListView listView = dialog.findViewById(R.id.list_view);

        // Initialize array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.list_item,
                listOrganizations);

        // set adapter
        listView.setAdapter(adapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when item selected from list
                // set selected item on textView
                organization[0] = adapter.getItem(position);

                // Dismiss dialog
                dialog.dismiss();
            }
        });
        return organization[0];
    }

    public static String setBirthdate(Context context){
        final String[] date = new String[1];
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //open a Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context
                , new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            //show the date that the user chose at the birthday edit text
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // on below line we are setting date to our edit text.
                date[0] = DateConverter.convertFullFormatToDate(
                        dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        }, year, month, day);
        datePickerDialog.show();
        return date[0];
    }

    public static String checkAlertsForOrganization(String organization){
        String alartForOrganization = "";
        if (organization.isEmpty())
            alartForOrganization = "* שדה חובה! בחר תנועת נוער";
        return alartForOrganization;
    }

    public static String checkAlertsForBirthday(String birthday){
        String alartForBirthday = "";
        if(birthday.isEmpty())
            alartForBirthday = "* שדה חובה! הכנס תאריך לידה"
                    + '\n'
                    + alartForBirthday;

        if (!checkBirthday(birthday)) {
            alartForBirthday = "* התאריך שהוכנס לא תקין! נסה שנית" +
                    '\n' +
                    alartForBirthday;
        }
        return alartForBirthday;
    }

    /**
     * בודק אם שם משתמש עומד בכל הדרישות:
     * אורך בין 8 ל-15 תווים, כולל אותיות, מספרים ותווים מיוחדים,
     * וגם לא תפוס על ידי משתמש אחר (בדיקה א-סינכרונית).
     *
     * @param userName שם המשתמש לבדיקה
     * @return true אם עומד בדרישות, אחרת false
     */
    public static String checkAlertsForUserName(String userName){
        String alartForUserName = "";
        if(userName.isEmpty())
        {
            alartForUserName = "* שדה חובה! הכנס שם משתמש" +
                    '\n' +
                    alartForUserName;
        }
        else if(userName.length() < 6){
            alartForUserName = " * שם משתמש קצר מדי נסה שנית. " +
                    '\n' +
                    alartForUserName;
        }
        else if(userName.length() > 15){
            alartForUserName = "* שם משתמש ארוך מדי נסה שנית." +
                    '\n' +
                    alartForUserName;
        }

        if(!input_Validation(userName))
        {
            alartForUserName = "* שם המשתמש חייב להכיל ספרות, אותיות באנגלית ותווים מיוחדים." +
                    '\n' +
                    alartForUserName;
        }
        return (alartForUserName);
    }

    public static String checkAlertForNickName(String nickName) {
        String alartForNickName = "";
        if (nickName.isEmpty())
            alartForNickName = "* שדה חובה! הכנס כינוי"
                    + '\n'
                    + alartForNickName;
        else if (nickName.length() < 2)
            alartForNickName = " * כינוי קצר מדי נסה שנית. "
                    + '\n'
                    + alartForNickName;
        else if (nickName.length() > 15)
            alartForNickName = "* כינוי ארוך מדי נסה שנית."
                    + '\n'
                    + alartForNickName;
        return alartForNickName;
    }

    public static String checkAlertsForPassword(String password){
        String alartForPassword = "";
        if(password.isEmpty())
        {
            alartForPassword = "* שדה חובה! הכנס סיסמה" +
                    '\n' +
                    alartForPassword;
        }
        else if(password.length() < 6){
            alartForPassword = "* סיסמה קצרה מדי נסה שנית" +
                    '\n' +
                    alartForPassword;
        }
        else if(password.length() > 10){
            alartForPassword = "* סיסמה ארוכה מדי נסה שנית" +
                    '\n' +
                    alartForPassword;
        }
        return alartForPassword;
    }

    public static String checkAlertsForEmail(String email){
        String alartForEmail = "";
        if(email.isEmpty())
        {
            alartForEmail = "* שדה חובה! הכנס מייל" +
                    '\n' +
                    alartForEmail;
        }
        else if(!checkEmail(email))
        {
            alartForEmail = "* המייל שהכנסת אינו תקין, נסה שנית" +
                    '\n' +
                    alartForEmail;
        }
        return alartForEmail;
    }

    /**
     * בודק אם תאריך הלידה חוקי – כלומר קטן מהשנה הנוכחית וגדול מ-1900.
     *
     * @param birthday מחרוזת של תאריך בפורמט dd/MM/yyyy
     * @return true אם תאריך תקין, אחרת false
     */
    public static boolean checkBirthday(String birthday){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date birthdayDate;
        try {
            birthdayDate = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (birthdayDate == null)
            return false;
        return (birthdayDate.getYear() + 1900) < year &&
                (birthdayDate.getYear() + 1900) > 1900;
    }
    //checks if username is stand at all the terms
    public static boolean checkUserName(String userName){

        return ((userName.length() >= 6) && (userName.length() <= 15) && (input_Validation(userName)));
    }
    public static boolean checkOrganization(String organization){
        return !organization.isEmpty();
    }

    public static boolean checkPic(Bitmap pic){
        return !BitmapHelper.bitmapToString(pic).isEmpty();

    }

    /**
     * בודק אם הכינוי באורך חוקי (בין 2 ל-15 תווים).
     *
     * @param nickName כינוי לבדיקה
     * @return true אם חוקי, אחרת false
     */
    public static boolean checkNickName(String nickName){
        return ((nickName.length() >= 2) && (nickName.length() <= 15));
    }

    //checks if password is stand at all the terms
    public static boolean checkPassword(String password){
        return ((password.length() >= 6) && (password.length() <= 10));
    }

    //checks if email is stand at all the terms
    // TODO: המייל לא מקבל מסיבה כלשהי מספרים ותויים מיוחדים
    public static boolean checkEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return (email.matches(emailPattern) && (!email.isEmpty()));
    }

    /**
     * בודק אם קלט כולל אותיות, ספרות ותווים מיוחדים.
     * משמש לאימות תקינות של שם משתמש.
     *
     * @param input מחרוזת לבדיקה
     * @return true אם הקלט כולל את שלושת הסוגים, אחרת false
     */
    public static boolean input_Validation(String input)
    {
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[^A-Za-z0-9]");

        Matcher hasLetter = letter.matcher(input);
        Matcher hasDigit = digit.matcher(input);
        Matcher hasSpecial = special.matcher(input);

        return hasLetter.find() && hasDigit.find() && hasSpecial.find();
    }

    public static class HandleAlartDialogLostener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //camera
            if (which == -1) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(cameraIntent);
            }
            //gallery
            else if (which == -2) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Intent.ACTION_PICK → Opens an app to let the user pick something.
                //MediaStore.Images.Media.EXTERNAL_CONTENT_URI → Gets images from external storage (Gallery).
                galleryLauncher.launch(galleryIntent);
            }
        }
    }
}
