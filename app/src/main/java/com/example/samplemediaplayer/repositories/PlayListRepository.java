package com.example.samplemediaplayer.repositories;

import android.app.Application;
import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.room.MusicListDAO;
import com.example.samplemediaplayer.room.MusicListDataBase;
import com.example.samplemediaplayer.services.MusicListWorker;

import java.util.ArrayList;
import java.util.List;

public class PlayListRepository {
    MusicListDataBase musicListDataBase;
    MusicListDAO musicListDAO;
    WorkManager workManager;
    List<MusicListItemModel> daoData = new ArrayList<>();
    LiveData<List<MusicListItemModel>> liveData;
    Context context;
    LifecycleOwner lifecycleOwner;
    int _id;
    public PlayListRepository(Application application){
        musicListDataBase = MusicListDataBase.getInstance(application);
        musicListDAO = musicListDataBase.musicListItemDAO();
        workManager = WorkManager.getInstance(application);
        context = application.getApplicationContext();
        liveData = musicListDAO.getAll();
    }
    public LiveData<List<MusicListItemModel>> getAll(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA
        };

        Data projection_data = new Data.Builder()
                .putStringArray("media_projection", projection)
                .putInt("id", 0)
                .build();

        OneTimeWorkRequest queryMusicFile = new OneTimeWorkRequest.Builder(MusicListWorker.class)
                .setInputData(projection_data)
                .build();

        workManager.enqueue(queryMusicFile);
        workManager.getWorkInfoByIdLiveData(queryMusicFile.getId())
                .observe(lifecycleOwner, workInfo -> {
                    WorkInfo.State state = workInfo.getState();

                    if (state == WorkInfo.State.RUNNING &&
                            !workInfo.getState().isFinished()){
                        Log.e("WORK_PROCESS", "Runnning Worker");
                    }

                    if (workInfo.getState().isFinished()){
                        Log.e("WORK_FINISH", "finished Worker");
                        new GetAllThread().start();
                    }
                });
        return liveData;
    }
    class GetAllThread extends Thread {
        @Override
        public void run() {
            liveData=musicListDAO.getAll();
        }
    }
    public LiveData<List<MusicListItemModel>>getAllData(){
        return liveData;
    }
    public void delete(ArrayList<Integer> checkIdList){
        new PlayListRepository.DeleteThread(checkIdList).start();
    }
    class DeleteThread extends Thread {
        ArrayList<Integer> checkIdList;
        public DeleteThread(ArrayList<Integer> checkIdList){
            this.checkIdList = checkIdList;
        }
        @Override
        public void run() {
            for(int elem: checkIdList){
                musicListDAO.delete(elem);
            }
        }
    }

}
