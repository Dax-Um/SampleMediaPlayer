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

import com.example.samplemediaplayer.Utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayInfoWorker extends Worker {
    String[] projection;
    int _id;
    Cursor cursor;
    static String MEDIA_PROJECTION = "media_projection";
    static String _ID = "ID";
    String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
    String[] selectionArgsMp3 = new String[]{
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")};
    Data output;

    public PlayInfoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        projection = workerParams.getInputData().getStringArray(MEDIA_PROJECTION);
        _id = workerParams.getInputData().getInt(_ID, 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            getCursor();
            if (cursor != null && cursor.getCount() != 0){output = setPlayData();}
            return Result.success(output);
        }catch (Exception e){

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
    private Data setPlayData(){
        Data data = new Data.Builder()
                .putString("data", cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)))
                .putLong("duration", cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)))
                .build();
        return data;
    }

}
