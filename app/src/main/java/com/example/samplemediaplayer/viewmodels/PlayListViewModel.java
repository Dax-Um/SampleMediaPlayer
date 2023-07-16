package com.example.samplemediaplayer.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.repositories.PlayListRepository;
import com.example.samplemediaplayer.services.PlayService;

import java.util.ArrayList;
import java.util.List;

public class PlayListViewModel extends AndroidViewModel {
    PlayListRepository playListRepository;
    LiveData<List<MusicListItemModel>> liveData;
    ArrayList<Integer> idList = new ArrayList<>();
    Application application;
    Intent serviceIntent;
    LifecycleOwner lifecycleOwner;
    public PlayListViewModel(@NonNull Application application) {
        super(application);
        playListRepository = new PlayListRepository(application);
        liveData = playListRepository.getAllData();
        this.application = application;
        serviceIntent = new Intent(application, PlayService.class);
    }
    public LiveData<List<MusicListItemModel>> getAll(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
        liveData = playListRepository.getAll(lifecycleOwner);
        return liveData;
    }
    public void delete(ArrayList<Integer> checkIdList)
    {
        playListRepository.delete(checkIdList);
    }
    public ArrayList<Integer> getIdList(){
        Thread thread = new getIdListThread();
        thread.start();
        return idList;
    }
    class getIdListThread extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < liveData.getValue().size(); i++){
                idList.add(liveData.getValue().get(i)._id);
            }
        }
    }
}
