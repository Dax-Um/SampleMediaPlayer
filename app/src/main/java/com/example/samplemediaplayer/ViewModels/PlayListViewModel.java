package com.example.samplemediaplayer.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.Repositories.PlayListRepository;

import java.util.List;

public class PlayListViewModel extends AndroidViewModel {
    PlayListRepository playListRepository;
    MutableLiveData<List<MusicListItemModel>> liveData = new MutableLiveData<>();
    public PlayListViewModel(@NonNull Application application) {
        super(application);
        playListRepository = new PlayListRepository(application);
        Log.e("CREATE DB", "NEW DB");
    }
    public MutableLiveData<List<MusicListItemModel>> getAll(LifecycleOwner lifecycleOwner){
        liveData = playListRepository.getAll(lifecycleOwner);
        return liveData;
    }
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//    }
}
