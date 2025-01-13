package com.example.guided;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapHelper {
    /**
     * Converts a Bitmap to a Base64 encoded string.
     *
     * @param bitmap The Bitmap to convert.
     * @return The Base64 encoded string.
     */
    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Converts a Base64 encoded string to a Bitmap.
     *
     * @param base64String The Base64 encoded string.
     * @return The Bitmap, or null if the string is invalid.
     */
    public static Bitmap stringToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
