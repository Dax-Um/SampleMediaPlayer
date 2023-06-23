package com.example.samplemediaplayer.Repositories;

import android.app.Application;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.Room.MusicListDAO;
import com.example.samplemediaplayer.Room.MusicListDataBase;
import com.example.samplemediaplayer.Services.MusicListWorker;

import java.util.ArrayList;
import java.util.List;

public class PlayListRepository {
    MusicListDataBase musicListDataBase;
    MusicListDAO musicListDAO;
    WorkManager workManager;
    List<MusicListItemModel> daoData = new ArrayList<>();
    MutableLiveData<List<MusicListItemModel>> liveData = new MutableLiveData<>();
    int offset = 0;
    public PlayListRepository(Application application){
        musicListDataBase = MusicListDataBase.getInstance(application);
        musicListDAO = musicListDataBase.musicListItemDAO();
        workManager = WorkManager.getInstance(application);
    }
    public MutableLiveData<List<MusicListItemModel>> getAll(LifecycleOwner lifecycleOwner){
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
        };

        Data projection_data = new Data.Builder()
                .putStringArray("media_projection", projection)
                .build();

        OneTimeWorkRequest queryMusicFile = new OneTimeWorkRequest.Builder(MusicListWorker.class)
                .setInputData(projection_data)
                .build();

        workManager.enqueue(queryMusicFile);
        workManager.getWorkInfoByIdLiveData(queryMusicFile.getId())
                .observe(lifecycleOwner, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        WorkInfo.State state = workInfo.getState();

                        if (state == WorkInfo.State.RUNNING &&
                                !workInfo.getState().isFinished()){
                            Log.e("WORK_PROCESS", "Runnning Worker");
                        }

                        if (workInfo.getState().isFinished()){
                            Log.e("WORK_FINISH", "finished Worker");
                            new PlayListRepository.GetAllThread().start();
                        }
                    }
                });
        return liveData;
    }
    class GetAllThread extends Thread {
        @Override
        public void run() {
            daoData=musicListDAO.getAll();
            liveData.postValue(daoData);
        }
    }
}
