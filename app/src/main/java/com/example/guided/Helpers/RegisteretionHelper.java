package com.example.guided.Helpers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guided.R;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegisteretionHelper היא מחלקת עזר שמספקת פונקציות סטטיות שונות
 * עבור תהליך ההרשמה של משתמש חדש, כולל בחירת תאריך לידה, תנועת נוער,
 * העלאת תמונת פרופיל ובדיקת תקינות של שדות קלט כמו שם משתמש, סיסמה, מייל ועוד.
 * המחלקה מכילה גם ממשקים פנימיים לקריאה חוזרת (Callbacks) לצורך קבלת ערכים ממשתמש,
 * ומטפלת בדיאלוגים של בחירה בצורה ידידותית למשתמש.
 */
public class RegisteretionHelper extends AppCompatActivity {

    /**
     * ממשק לקריאה חוזרת כאשר המשתמש בחר תנועת נוער.
     */
    public interface OrganizationCallback {
        void onOrganizationSelected(String organization);
    }

    /**
     * ממשק לקריאה חוזרת כאשר המשתמש בחר תאריך לידה.
     */
    public interface BirthdayCallback {
        void onBirthdaySelected(String birthday);
    }

    /**
     * ממשק Callback לדיאלוג תמונה
     */
    public interface PicDialogCallback {
        void onResult(int pic);
    }

    /**
     * פותחת דיאלוג לבחירת תמונת פרופיל – מצלמה או גלריה או מחיקת תמונה קיימת.
     *
     * @param context ההקשר של האקטיביטי הנוכחי
     * @param cameraLauncher משגר הפעולה למצלמה
     * @param galleryLauncher משגר הפעולה לגלריה
     */
    public static void setPic(
            Context context,
            ActivityResultLauncher<Intent> cameraLauncher,
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
        title.setText("העלאת תמונת פרופיל");
        /*
          מאזין ללחיצה בדיאלוג תמונה – מפעיל את המצלמה או הגלריה או מחיקה בהתאם ללחיצה.
         */
        delete.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onResult(R.drawable.profile);
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
            }
        });
    }

    /**
     * פותחת דיאלוג לבחירת תנועת נוער מתוך רשימה.
     *
     * @param context הקשר האקטיביטי
     * @param callback קריאה חוזרת עם תנועת הנוער שנבחרה
     */
    public static void setOrganization(Context context, OrganizationCallback callback){
        String[] listOrganizations = context
                .getResources()
                .getStringArray(
                        R.array.organization_adjustment);
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.search);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.list_item,
                listOrganizations);

        listView.setAdapter(adapter);
        editText.addTextChangedListener(
                new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String organization = adapter.getItem(position);

                dialog.dismiss();
                callback.onOrganizationSelected(
                        organization);
            }
        });
    }

    /**
     * פותחת דיאלוג לבחירת תאריך לידה ומחזירה את התוצאה באמצעות callback.
     *
     * @param context ההקשר של האקטיביטי
     * @param callback קריאה חוזרת עם תאריך הלידה שנבחר
     */
    public static void setBirthdate(Context context, BirthdayCallback callback){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //פתיחת דיאלוג בחירת תאריך
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            //הצגת התאריך שהמשתמש בחר edit text יום ההולדת
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // הגדרת התאריך שיהיה מוצג בedit text
                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                callback.onBirthdaySelected(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * שומר תמונה בגלריית המכשיר תחת Pictures/MyApp.
     *
     * @param bitmap התמונה לשמירה
     * @param context ההקשר של האקטיביטי
     */
    public static void saveImageToGallery(Bitmap bitmap, Context context) {
        ContentValues values = new ContentValues(); //משתנה(values) ששומר בתוכו מידע(metadata) על התמונה כגון שם התמונה, סוגה, ומיקומה

        values.put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "IMG_" +
                        System.currentTimeMillis() +
                        ".jpg"); // שם תמונה ייחודי
        values.put(MediaStore.Images.Media.MIME_TYPE,
                "image/jpeg"); // סוג התמונה (JPEG)
        values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES +
                        "/MyApp"); // נשמר בתוך Pictures/MyApp

        //מכניס את מידע על התמונה לתוך MediaStore וקבל את Uri
        Uri imageUri = context.
                getContentResolver().
                insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);

        //try-with-resources -> מבטיח שהזרם נסגר אוטומטית
        try (OutputStream outputStream = context.
                getContentResolver().
                openOutputStream(imageUri)) {
            //כותב את נתוני התמונה מסוג bitmap לתוך הקובץ
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    outputStream); // שומר את התמונה כJPEG באיכות 100%
            Toast.makeText(context, "נשמר בהצלחה בגלריה!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "שמירה בגלריה נכשלה!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * מחזירה הודעת שגיאה עבור שדה תנועת נוער במידה ולא נבחר ערך.
     *
     * @param organization ערך שנבחר
     * @return מחרוזת שגיאה אם השדה ריק, אחרת מחרוזת ריקה
     */
    public static String checkAlertsForOrganization(String organization){
        String alartForOrganization = "";
        if (organization.isEmpty())
            alartForOrganization = "* שדה חובה! בחר תנועת נוער";

        return alartForOrganization;
    }

    /**
     * מחזירה הודעות שגיאה עבור שדה תאריך לידה – אם ריק או לא תקין.
     *
     * @param birthday מחרוזת תאריך
     * @return מחרוזת שגיאות בהתאם לתוצאה
     */
    public static String checkAlertsForBirthday(String birthday){
        String alartForBirthday = "";
        if(birthday.isEmpty())
            alartForBirthday = "* שדה חובה! הכנס תאריך לידה" + '\n' + alartForBirthday;

        if (!checkBirthday(birthday))
            alartForBirthday = "* התאריך שהוכנס לא תקין! נסה שנית" + '\n' + alartForBirthday;

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
            alartForUserName = "* שדה חובה! הכנס שם משתמש" + '\n' + alartForUserName;

        else if(userName.length() < 6)
            alartForUserName = " * שם משתמש קצר מדי נסה שנית. " + '\n' + alartForUserName;

        else if(userName.length() > 15)
            alartForUserName = "* שם משתמש ארוך מדי נסה שנית." + '\n' + alartForUserName;

        if(!input_Validation(userName))
            alartForUserName = "* שם המשתמש חייב להכיל ספרות, אותיות באנגלית ותווים מיוחדים." + '\n' + alartForUserName;

        return (alartForUserName);
    }

    /**
     * בודקת תקינות של כינוי (nick name).
     *
     * @param nickName כינוי לבדיקה
     * @return הודעת שגיאה אם יש שגיאה, אחרת ריק
     */
    public static String checkAlertForNickName(String nickName) {
        String alartForNickName = "";
        if (nickName.isEmpty())
            alartForNickName = "* שדה חובה! הכנס כינוי" + '\n' + alartForNickName;

        else if (nickName.length() < 2)
            alartForNickName = " * כינוי קצר מדי נסה שנית. " + '\n' + alartForNickName;

        else if (nickName.length() > 15)
            alartForNickName = "* כינוי ארוך מדי נסה שנית." + '\n' + alartForNickName;

        return alartForNickName;
    }

    /**
     * בודקת תקינות של סיסמה (אורך בלבד).
     *
     * @param password סיסמה לבדיקה
     * @return מחרוזת עם הודעת שגיאה במקרה הצורך
     */
    public static String checkAlertsForPassword(String password){
        String alartForPassword = "";
        if(password.isEmpty())
            alartForPassword = "* שדה חובה! הכנס סיסמה" + '\n' + alartForPassword;

        else if(password.length() < 6)
            alartForPassword = "* סיסמה קצרה מדי נסה שנית" + '\n' + alartForPassword;

        else if(password.length() > 10)
            alartForPassword = "* סיסמה ארוכה מדי נסה שנית" + '\n' + alartForPassword;

        return alartForPassword;
    }

    /**
     * בודקת תקינות של כתובת מייל.
     *
     * @param email כתובת מייל לבדיקה
     * @return מחרוזת עם הודעת שגיאה במקרה הצורך
     */
    public static String checkAlertsForEmail(String email){
        String alartForEmail = "";
        if(email.isEmpty())
            alartForEmail = "* שדה חובה! הכנס מייל" + '\n' + alartForEmail;

        else if(!checkEmail(email))
            alartForEmail = "* המייל שהכנסת אינו תקין, נסה שנית" + '\n' + alartForEmail;

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
                "dd/MM/yyyy",
                Locale.getDefault());
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

    /**
     * בודקת אם שם המשתמש עומד בדרישות אורך ותווים.
     *
     * @param userName שם המשתמש לבדיקה
     * @return true אם תקין, אחרת false
     */
    public static boolean checkUserName(String userName){
        return ((userName.length() >= 6) &&
                (userName.length() <= 15) &&
                (input_Validation(userName)));
    }

    /**
     * בודקת אם תנועת נוער נבחרה.
     *
     * @param organization תנועת נוער
     * @return true אם לא ריק
     */
    public static boolean checkOrganization(String organization){
        return !organization.isEmpty();
    }

    /**
     * בודקת אם הועלתה תמונת פרופיל.
     *
     * @param pic תמונת פרופיל
     * @return true אם לא ריקה
     */
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
        return ((nickName.length() >= 2) &&
                (nickName.length() <= 15));
    }

    /**
     * בודקת אם סיסמה באורך חוקי (6-10 תווים).
     *
     * @param password סיסמה לבדיקה
     * @return true אם תקין, אחרת false
     */
    public static boolean checkPassword(String password){
        return ((password.length() >= 6) &&
                (password.length() <= 10));
    }

    /**
     * בודקת אם מייל תקין בהתאם לתבנית בסיסית.
     *
     * @param email כתובת מייל
     * @return true אם תקין, אחרת false
     */
    public static boolean checkEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return (email.matches(emailPattern) &&
                (!email.isEmpty()));
    }

    /**
     * בודק אם קלט כולל אותיות, ספרות ותווים מיוחדים.
     * משמש לאימות תקינות של שם משתמש.
     *
     * @param input מחרוזת לבדיקה
     * @return true אם הקלט כולל את שלושת הסוגים, אחרת false
     */
    public static boolean input_Validation(String input) {
        Pattern letter = Pattern.
                compile("[a-zA-Z]");
        Pattern digit = Pattern.
                compile("[0-9]");
        Pattern special = Pattern.
                compile ("[^A-Za-z0-9]");

        Matcher hasLetter = letter.matcher(input);
        Matcher hasDigit = digit.matcher(input);
        Matcher hasSpecial = special.matcher(input);

        return hasLetter.find() &&
                hasDigit.find() &&
                hasSpecial.find();
    }
}
