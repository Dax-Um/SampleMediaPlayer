package com.example.samplemediaplayer.viewmodels;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.samplemediaplayer.services.IserviceAdapter;
import com.example.samplemediaplayer.databinding.ActivityMusicPlayerBinding;
import com.example.samplemediaplayer.models.MusicPlayerModel;
import com.example.samplemediaplayer.repositories.PlayerRepository;
import com.example.samplemediaplayer.services.PlayService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MusicPlayerViewModel extends AndroidViewModel implements IserviceAdapter {
    PlayerRepository playRepository;
    MutableLiveData<MusicPlayerModel> liveData = new MutableLiveData<>();
    ActivityMusicPlayerBinding binding;
    Application application;
    int _id;
    String file_path;
    boolean runThread;
    boolean hasThread=false;
    boolean startService=false;
    ArrayList<Integer> idList;
    Intent serviceIntent=null;
    public MusicPlayerViewModel(@NonNull Application application, int _id,
                                ActivityMusicPlayerBinding binding, ArrayList<Integer> idList)
    {
        super(application);
        this.application = application;
        this._id = _id;
        this.binding = binding;
        this.idList = idList;
        playRepository = new PlayerRepository(this.application);
        this.application.registerReceiver(receiver,
                new IntentFilter("com.example.PLAY_TO_ACTIVITY"));
        serviceIntent = new Intent(application, PlayService.class);
    }
    public MutableLiveData<MusicPlayerModel> loadAllById (){
        liveData = playRepository.loadAllById(_id);
        return liveData;
    }
    public void setCurrentProgress(){
        PlayerIntent.putExtra("mode","current progress");
        application.sendBroadcast(PlayerIntent);
    }
    public void startPlayService(){
        file_path = liveData.getValue().getFile_path();
        serviceIntent.putExtra("file_path", file_path);
        application.startService(serviceIntent);
    }
    public void stopPlayService(){
        PlayerIntent.putExtra("mode","stop");
        application.sendBroadcast(PlayerIntent);
        runThread=false;
        application.stopService(serviceIntent);
        startPlayService();
    }
    public void pausePlayService(){
        PlayerIntent.putExtra("mode","pause");
        application.sendBroadcast(PlayerIntent);
        runThread=false;
    }
    public void restartPlayService(){
        PlayerIntent.putExtra("mode", "restart");
        application.sendBroadcast(PlayerIntent);

        runThread=true;
        ProgressThread thread=new ProgressThread();
        thread.start();
    }
    @Override
    public void nextPlayService() {
        int index = idList.indexOf(_id);
        _id = idList.get(index+1);
        loadAllById();
        runThread=true;
    }
    @Override
    public void prePlayService() {
        int index = idList.indexOf(_id);
        _id = idList.get(index-1);
        loadAllById();
        runThread=true;
    }
    public int getId(){return _id;}
    public void unregisterPlayReceiver(){
        application.unregisterReceiver(receiver);
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
    @Override
    protected void onCleared() {
        super.onCleared();
    }
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mode = intent.getStringExtra("mode");
            if(mode != null){
                if(mode.equals("start")){
                    int duration = intent.getIntExtra("duration",0);
                    binding.lab1Progress.setMax(duration);
                    binding.lab1Progress.setProgress(0);
                }else if(mode.equals("stop")){
                    runThread = false;
                } else if(mode.equals("pause")) {
                    runThread=false;
                }else if(mode.equals("prepare")){
                    Intent playIntent = new Intent("com.example.PLAY_TO_SERVICE");
                    playIntent.putExtra("mode", "start");
                    application.sendBroadcast(playIntent);
                    runThread = true;
                    if (!hasThread){
                        ProgressThread thread=new ProgressThread();
                        thread.start();
                        hasThread = true;
                    }
                } else if (mode.equals("current progress")) {
                    int duration = intent.getIntExtra("duration",0);
                    int current_progress = intent.getIntExtra("current progress",0);
                    binding.lab1Progress.setMax(duration);
                    binding.lab1Progress.setProgress(current_progress);
                    runThread = true;
                    ProgressThread thread=new ProgressThread();
                    thread.start();
                    hasThread = true;
                }
            }
        }
    };
    class ProgressThread extends Thread{
        @Override
        public void run() {
            while (runThread){
                binding.lab1Progress.incrementProgressBy(1000);
                SystemClock.sleep(1000);
                if(binding.lab1Progress.getProgress()==binding.lab1Progress.getMax()){
                    runThread=false;
                }
            }
        }
    }
}
