package com.example.samplemediaplayer.Utils;

import android.database.Cursor;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class DataUtil {
    public static String serializeToJson (@NonNull Bitmap bitmap){
        Gson gson = new Gson();
        return gson.toJson(bitmap);
    }
    public static Bitmap deserializeFromJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Bitmap.class);
    }

}
