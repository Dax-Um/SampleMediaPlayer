package com.example.samplemediaplayer.Services;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.example.samplemediaplayer.Models.MiniPlayerModel;
import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.Models.MusicPlayerModel;
import com.example.samplemediaplayer.Models.SongInformationModel;
import com.example.samplemediaplayer.Utils.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;

public class MusicFileWorker extends Worker {
    WorkerParameters workerParameters;
    MusicUtil musicUtil;
    Cursor media_cursor;
    ArrayList<MiniPlayerModel> miniPlayerModelArrayList = new ArrayList<>();
    ArrayList<MusicListItemModel> musicListItemModelArrayList = new ArrayList<>();
    ArrayList<MusicPlayerModel> musicPlayerModelArrayList = new ArrayList<>();
    ArrayList<SongInformationModel> songInformationModelArrayList = new ArrayList<>();

    public static String LOG_WORKER;


    public MusicFileWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);
        LOG_WORKER = MusicFileWorker.class.getSimpleName();

        workerParameters = params;
        Log.d(LOG_WORKER, "ID: " + workerParameters.getId());
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            media_cursor = getCursor();
            if (media_cursor != null && media_cursor.getCount() != 0){
                if(media_cursor.moveToFirst()){
                    do {
                        Bitmap albumArt = getThumbNail();
                        musicUtil = new MusicUtil(
                                media_cursor.getLong(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media._ID)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)),
                                media_cursor.getLong(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)),
                                media_cursor.getString(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)),
                                media_cursor.getLong(media_cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                                albumArt
                        );

                        miniPlayerModelArrayList.add(musicUtil.getMiniPlayerModel());
                        musicListItemModelArrayList.add(musicUtil.getMusicListItemModel());
                        musicPlayerModelArrayList.add(musicUtil.getMusicPlayerModel());
                        songInformationModelArrayList.add(musicUtil.getSongInformationModel());

                    }while(media_cursor.moveToNext());
                }
            }
            media_cursor.close();

            for (int i = 0; i < miniPlayerModelArrayList.size(); i++){
                Log.e("MINIPLAYER", "ID: " + miniPlayerModelArrayList.get(i).getId() + "\n" +
                        "ALBUM ART: " + miniPlayerModelArrayList.get(i).getAlbumArt());
            }

            return Result.success();
        }catch (Exception e){

            return Result.failure();
        }
    }

    private Cursor getCursor(){
        String[] media_projection = {
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.GENRE,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE,
        };

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] selectionArgsMp3 = new String[]{
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")
        };

        Cursor media_cursor = getApplicationContext()
                                .getContentResolver()
                                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        media_projection, selectionMimeType,
                                        selectionArgsMp3,  null);
        return media_cursor;
    }

    private Bitmap getThumbNail(){
        Bitmap albumArt = null;

        Uri contentUri =  ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                media_cursor.getLong(media_cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AssetFileDescriptor afd = null;
            try {
                albumArt = getApplicationContext()
                        .getContentResolver()
                        .loadThumbnail(contentUri,
                                new Size(60, 40), null);
            } catch (IOException e) {
                Log.e("EXCEPTION", e.toString());
            }
        }
        return albumArt;
    }
}
