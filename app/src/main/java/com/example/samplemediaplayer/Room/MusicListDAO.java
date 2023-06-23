package com.example.samplemediaplayer.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.samplemediaplayer.Models.MusicListItemModel;

import java.util.List;

@Dao
public interface MusicListDAO {
    @Query("SELECT * FROM MusicListItemModel ORDER BY title ASC ")
    List<MusicListItemModel> getAll();

    @Query("SELECT * FROM MusicListItemModel WHERE _id in (:musicId)")
    MusicListItemModel loadAllById(int musicId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MusicListItemModel...musicListItemModels);

    @Query("DELETE FROM MusicListItemModel")
    void deleteAll();

    @Delete
    void delete(MusicListItemModel musicListItemModel);
}
