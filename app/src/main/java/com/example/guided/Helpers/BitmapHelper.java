package com.example.guided.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class BitmapHelper {

    /**
     * ממירה תמונה מסוג Bitmap למחרוזת מקודדת בפורמט Base64.
     *
     * @param bitmap האובייקט מסוג Bitmap שאותו רוצים להמיר.
     * @return מחרוזת Base64 המייצגת את התמונה, או null אם bitmap הוא null.
     */
    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                50, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(
                byteArray,
                Base64.DEFAULT);
    }

    /**
     * ממירה מחרוזת מקודדת בפורמט Base64 לאובייקט מסוג Bitmap.
     *
     * @param base64String מחרוזת Base64 המכילה את ייצוג התמונה.
     * @return האובייקט מסוג Bitmap, או null אם המחרוזת שגויה או ריקה.
     */
    public static Bitmap stringToBitmap(String base64String) {
        if (base64String == null ||
                base64String.isEmpty())
            return null;
        try {
            byte[] decodedBytes = Base64.decode(
                    base64String,
                    Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(
                    decodedBytes,
                    0,
                    decodedBytes.length);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
