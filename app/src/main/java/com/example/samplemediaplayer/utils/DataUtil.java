package com.example.samplemediaplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

public class DataUtil {
    public static String serializeToJson (@NonNull Bitmap bitmap){
        Gson gson = new Gson();
        return gson.toJson(bitmap);
    }
    public static Bitmap deserializeFromJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Bitmap.class);
    }
    public static Bitmap getThumbNail(Context context, Long album_id, int width, int height){
        Bitmap albumArt = null;
        Uri contentUri =  ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                album_id
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                albumArt = context
                        .getContentResolver()
                        .loadThumbnail(contentUri,
                                new Size(width, height), null);
            } catch (IOException e) {
                Log.e("EXCEPTION", e.toString());
            }
        }
        return albumArt;
    }
    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
