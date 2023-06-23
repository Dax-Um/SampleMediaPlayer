package com.example.samplemediaplayer.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.samplemediaplayer.Models.MusicListItemModel;

import kotlin.jvm.Synchronized;

@Database(entities = {MusicListItemModel.class}, version = 1)
public abstract class MusicListDataBase extends RoomDatabase {
    private static MusicListDataBase INSTANCE = null;
    public abstract MusicListDAO musicListItemDAO();
    @Synchronized
    public static MusicListDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MusicListDataBase.class, "MusicList.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
