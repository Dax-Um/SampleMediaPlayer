package com.example.samplemediaplayer.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.samplemediaplayer.models.MusicListItemModel;

import java.util.List;

@Dao
public interface MusicListDAO {
    @Query("SELECT * FROM MusicListItemModel ORDER BY title ASC ")
    LiveData<List<MusicListItemModel>> getAll();

    @Query("SELECT * FROM MusicListItemModel WHERE _id in (:musicId)")
    MusicListItemModel loadAllById(int musicId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MusicListItemModel...musicListItemModels);

    @Query("DELETE FROM MusicListItemModel")
    void deleteAll();

    @Query("DELETE FROM MusicListItemModel WHERE _id in (:musicId)")
    void delete(int musicId);
}
