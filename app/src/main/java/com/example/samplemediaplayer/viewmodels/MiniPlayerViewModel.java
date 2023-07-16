package com.example.samplemediaplayer.viewmodels;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.samplemediaplayer.models.MusicPlayerModel;
import com.example.samplemediaplayer.repositories.PlayerRepository;
import com.example.samplemediaplayer.services.IserviceAdapter;
import com.example.samplemediaplayer.services.PlayService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MiniPlayerViewModel extends AndroidViewModel implements IserviceAdapter {
    PlayerRepository playRepository;
    MutableLiveData<MusicPlayerModel> liveData = new MutableLiveData<>();
    Application application;
    int _id;
    boolean startService=false;
    ArrayList<Integer> idList;
    Intent serviceIntent=null;
    String filePath;

    public MiniPlayerViewModel(@NonNull Application application, int _id, ArrayList<Integer> idList)
    {
        super(application);
        this.application = application;
        this._id = _id;
        this.idList = idList;
        playRepository = new PlayerRepository(this.application);
        this.application.registerReceiver(receiver,
                new IntentFilter("com.example.PLAY_TO_ACTIVITY"));
        serviceIntent = new Intent(application, PlayService.class);
    }
    public MutableLiveData<MusicPlayerModel> loadAllById (int id){
        liveData = playRepository.loadAllById(id);
        return liveData;
    }
    public void updateId(int _id, ArrayList<Integer> idList){
        this._id = _id;
        this.idList = idList;
    }
    @Override
    public void stopPlayService() {
        PlayerIntent.putExtra("mode","stop");
        application.sendBroadcast(PlayerIntent);
        application.stopService(serviceIntent);
        startPlayService();
    }

    @Override
    public void startPlayService() {
        filePath = liveData.getValue().getFile_path();
        serviceIntent.putExtra("file_path", filePath);
        application.startService(serviceIntent);
    }

    @Override
    public void pausePlayService() {
        PlayerIntent.putExtra("mode","pause");
        application.sendBroadcast(PlayerIntent);
    }

    @Override
    public void restartPlayService() {
        PlayerIntent.putExtra("mode", "restart");
        application.sendBroadcast(PlayerIntent);
    }

    @Override
    public void nextPlayService() {
        int index = idList.indexOf(_id);
        _id = idList.get(index+1);
        loadAllById(_id);
    }

    @Override
    public void prePlayService() {
        int index = idList.indexOf(_id);
        _id = idList.get(index-1);
        loadAllById(_id);
    }
    public boolean checkServiceList(){
        ActivityManager am = (ActivityManager)application
                .getApplicationContext().getSystemService(application.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(1000);

        for(int i=0; i<rs.size(); i++){
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.e("run service","Package Name : " + rsi.service.getPackageName());
            Log.e("run service","Class Name : " + rsi.service.getClassName());
            if (Objects.equals(rsi.service.getClassName(),
                    "com.example.samplemediaplayer.services.PlayService")){
                startService = true;
            }
        }
        return startService;
    }
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mode = intent.getStringExtra("mode");
            if(mode != null){
                if(mode.equals("start")){
                    int duration = intent.getIntExtra("duration",0);
                }else if(mode.equals("stop")){
                } else if(mode.equals("pause")) {
                }else if(mode.equals("prepare")){
                    Intent playIntent = new Intent("com.example.PLAY_TO_SERVICE");
                    playIntent.putExtra("mode", "start");
                    application.sendBroadcast(playIntent);
                }
            }
        }
    };
}
