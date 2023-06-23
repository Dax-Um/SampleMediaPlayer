package com.example.samplemediaplayer.Services;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class SongInfoWorker extends Worker {
    String[] projection;
    int _id;
    Cursor cursor;
    static String MEDIA_PROJECTION = "media_projection";
    static String _ID = "ID";
    String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
    String[] selectionArgsMp3 = new String[]{
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")};
    Data output;

    public SongInfoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        projection = workerParams.getInputData().getStringArray(MEDIA_PROJECTION);
        _id = workerParams.getInputData().getInt(_ID, 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            getCursor();
            if (cursor != null && cursor.getCount() != 0){output= setSongInfoData();}
            return Result.success(output);
        } catch (Exception e){

            return Result.failure();
        }
    }
    private void getCursor(){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;;

        if (_id != 0){
            contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, _id);
        }
        cursor = getApplicationContext()
                .getContentResolver()
                .query(contentUri,
                        projection, selectionMimeType,
                        selectionArgsMp3, null);
    }
    private Data setSongInfoData(){
        Data data = new Data.Builder()
                .putString("album", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)))
                .putString("genre", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)))
                .putString("year", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)))
                .putString("track", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)))
                .putString("mime_type", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)))
                .putLong("size", cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)))
                .build();
        return data;
    }
}
