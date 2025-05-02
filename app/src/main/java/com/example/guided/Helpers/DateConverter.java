package com.example.guided.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * מחלקת עזר להמרת תאריכים בין מחרוזת לאובייקט Date ולהיפך, בהתאם לפורמטים שונים.
 * עושה  טיפול בתאריכי לידה המוזנים או מתקבלים מהמשתמש או ממסדי נתונים.
 */
public class DateConverter {

    /**
     * ממירה מחרוזת תאריך בפורמט dd/MM/yyyy לאובייקט {@link Date}.
     *
     * @param birthday מחרוזת תאריך (למשל "25/04/2005")
     * @return אובייקט Date המייצג את התאריך שהוזן
     * @throws RuntimeException אם הפורמט של המחרוזת שגוי או לא ניתן לפענוח
     *
     * דוגמה:
     * {@code Date d = DateConverter.convertStringToDate("01/01/2020");}
     */
    public static Date convertStringToDate(String birthday){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault());
        Date birthdayDate;
        //A try-catch- מטפל בפורמטים לא חוקיים של קלט, ומבטיח שהאפליקציה לא תקרוס אם המשתמש מזין תאריך שגוי.
        try{
            // dateFormat.parse(dateString) ממיר את המחרוזת לאובייקט Date.
            birthdayDate = dateFormat.parse(birthday);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return birthdayDate;
    }

    /**
     * ממירה מחרוזת תאריך בפורמט מלא (כמו שמתקבל מ-{@link Date#toString()})
     * לפורמט פשוט יותר: dd/MM/yyyy.
     *
     * @param inputDateString מחרוזת תאריך בפורמט מלא לדוגמה: "Tue Apr 25 00:00:00 GMT+03:00 2023"
     * @return מחרוזת תאריך בפורמט "dd/MM/yyyy" (למשל "25/04/2023"), או null במקרה של שגיאת עיבוד
     *
     * דוגמה:
     * {@code String formatted = DateConverter.convertFullFormatToDate("Tue Apr 25 00:00:00 GMT+03:00 2023");}
     */
    public static String convertFullFormatToDate(String inputDateString) {
        //מגדיר את תבניות תאריך הקלט והפלט
        SimpleDateFormat inputFormat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy",
                Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.ENGLISH);

        try {
            // מנתח את מחרוזת תאריך הקלט לאובייקט Date
            Date date = inputFormat.parse(inputDateString);

            // מעצב את אובייקט ה-Date לפורמט הפלט הרצוי
            return outputFormat.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null; // מחזיר null אם הניתוח נכשל
        }
    }
}
