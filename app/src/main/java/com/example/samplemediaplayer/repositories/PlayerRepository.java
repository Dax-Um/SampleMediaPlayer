package com.example.samplemediaplayer.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.models.MusicPlayerModel;
import com.example.samplemediaplayer.room.MusicListDAO;
import com.example.samplemediaplayer.room.MusicListDataBase;
import com.example.samplemediaplayer.utils.DataUtil;


public class PlayerRepository {
    MusicListDataBase musicListDataBase;
    MusicListDAO musicListDAO;
    MutableLiveData<MusicPlayerModel> liveData = new MutableLiveData<>();
    MusicListItemModel daoData = new MusicListItemModel(0,null, null, 0L, null);
    MusicPlayerModel musicPlayerModel;
    Context context;
    int _id;
    public PlayerRepository(Application application){
        musicListDataBase = MusicListDataBase.getInstance(application);
        musicListDAO = musicListDataBase.musicListItemDAO();
        context = application.getApplicationContext();
    }
    public MutableLiveData<MusicPlayerModel> loadAllById(int _id){
        this._id = _id;
        new PlayerRepository.GetAllThread().start();
        return liveData;
    }

    class GetAllThread extends Thread {
        @Override
        public void run() {
            daoData=musicListDAO.loadAllById(_id);
            musicPlayerModel = new MusicPlayerModel(
                    _id, daoData.title, daoData.artist,
                    DataUtil.getThumbNail(context, daoData.album_id, 227, 229),
                    daoData.file_path);
            liveData.postValue(musicPlayerModel);
        }
    }
}
