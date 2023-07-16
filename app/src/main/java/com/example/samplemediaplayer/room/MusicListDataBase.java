package com.example.samplemediaplayer.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.samplemediaplayer.models.MusicListItemModel;


@Database(entities = {MusicListItemModel.class}, version = 2)
public abstract class MusicListDataBase extends RoomDatabase {
    private static MusicListDataBase INSTANCE = null;
    public abstract MusicListDAO musicListItemDAO();
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
