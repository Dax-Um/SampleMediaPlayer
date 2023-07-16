package com.example.samplemediaplayer.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.room.MusicListDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MusicListWorker extends Worker {
    String[] projection;
    int _id;
    Cursor cursor;
    static String MEDIA_PROJECTION = "media_projection";
    static String ID = "id";
    static int DEFAULT_VALUE = 0;
    String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
    String[] selectionArgsMp3 = new String[]{
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")};
    MusicListItemModel musicListItemModel;
    MusicListDataBase musicListDataBase;
    List<MusicListItemModel> musicListItemModelList = new ArrayList<>();
    Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Uri contentUri;
    Data data = null;
    Context context;
    public MusicListWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        projection = workerParams.getInputData().getStringArray(MEDIA_PROJECTION);
        _id = workerParams.getInputData().getInt(ID, DEFAULT_VALUE);
        contentUri = baseUri;
        musicListDataBase = MusicListDataBase.getInstance(context);
        this.context = context;
    }
    @NonNull
    @Override
    public Result doWork() {
        try{
            getCursor(contentUri);
            if (cursor != null && cursor.getCount() != 0){setMusicListData();}
            return (data == null)?Result.success():Result.success(data);
        }catch (Exception e){
            return Result.failure();
        }
    }
    private void getCursor(Uri contentUri){
        cursor = context.getContentResolver().query(contentUri, projection,
                    selectionMimeType, selectionArgsMp3, null);
    }
    private void setMusicListData(){
        if (cursor.moveToFirst()){
            do {
                musicListItemModel = new MusicListItemModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                );
                musicListItemModelList.add(musicListItemModel);
            } while (cursor.moveToNext());
        }
        musicListDataBase.musicListItemDAO().insertAll(musicListItemModelList
                .toArray(new MusicListItemModel[0]));
    }
}
