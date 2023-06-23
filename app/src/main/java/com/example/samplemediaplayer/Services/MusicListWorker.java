package com.example.samplemediaplayer.Services;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.Room.MusicListDataBase;
import com.example.samplemediaplayer.Utils.DataUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MusicListWorker extends Worker {
    String[] projection;
    Cursor cursor;
    static String MEDIA_PROJECTION = "media_projection";
    String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
    String[] selectionArgsMp3 = new String[]{
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")};
    MusicListItemModel musicListItemModel;
    MusicListDataBase musicListDataBase;
    List<MusicListItemModel> musicListItemModelList = new ArrayList<>();

    public MusicListWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        projection = workerParams.getInputData().getStringArray(MEDIA_PROJECTION);
        musicListDataBase = MusicListDataBase.getInstance(context);
    }
    @NonNull
    @Override
    public Result doWork() {
        try{
            getCursor();
            if (cursor != null && cursor.getCount() != 0){setMusicListData();}
            return Result.success();
        }catch (Exception e){

            return Result.failure();
        }
    }
    private void getCursor(){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;;
        cursor = getApplicationContext()
                .getContentResolver()
                .query(contentUri,
                        projection, selectionMimeType,
                        selectionArgsMp3, null);
    }
    private void setMusicListData(){
        if (cursor.moveToFirst()){
            do {
                musicListItemModel = new MusicListItemModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                );
                musicListItemModelList.add(musicListItemModel);
            } while (cursor.moveToNext());
        }
        musicListDataBase.musicListItemDAO().insertAll(musicListItemModelList
                .toArray(new MusicListItemModel[musicListItemModelList.size()]));
    }
}
