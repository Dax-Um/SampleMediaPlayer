package com.example.samplemediaplayer.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener{
    MediaPlayer player;
    String filePath;
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mode=intent.getStringExtra("mode");
            Log.e("PLAYSERVICE","receive intent");
            if(mode != null){
                if(mode.equals("start")){
                    try{
                        if(player != null && player.isPlaying()){
                            player.stop();;
                            player.release();
                            player=null;
                        }
                        player=new MediaPlayer();
                        player.setDataSource(filePath);
                        player.prepare();
                        player.start();

                        Intent aIntent=new Intent("com.example.PLAY_TO_ACTIVITY");
                        aIntent.putExtra("mode","start");
                        aIntent.putExtra("duration", player.getDuration());
                        sendBroadcast(aIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(mode.equals("stop")){
                    if(player != null && player.isPlaying()){
                        player.stop();
                        player.release();
                        player=null;
                    }
                }else if(mode.equals("pause")){
                    if(player != null && player.isPlaying()){
                        player.pause();
                    }
                } else if(mode.equals("restart")) {
                    if(player != null){
                        player.start();
                    }
                } else if(mode.equals("current progress")){
                    if(player != null){
                    setCurrentProgress(player.getCurrentPosition());
                    }
                }
            }
        }
    };
    public PlayService() {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Intent intent=new Intent("com.example.PLAY_TO_ACTIVITY");
        intent.putExtra("mode", "stop");
        sendBroadcast(intent);
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter("com.example.PLAY_TO_SERVICE"));
        Intent intent=new Intent("com.example.PLAY_TO_ACTIVITY");
        intent.putExtra("mode", "prepare");
        sendBroadcast(intent);
        Log.e("PLAYSERVICE","create service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        filePath=intent.getStringExtra("file_path");
        Log.e("PLAYSERVICE","start service");
        if(player != null){
            Intent aIntent=new Intent("com.example.PLAY_TO_ACTIVITY");
            aIntent.putExtra("mode", "restart");
            aIntent.putExtra("duration", player.getDuration());
            sendBroadcast(aIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void setCurrentProgress(int currentProgress){
        if(player != null){
            Intent aIntent=new Intent("com.example.PLAY_TO_ACTIVITY");
            aIntent.putExtra("mode", "current progress");
            aIntent.putExtra("duration", player.getDuration());
            aIntent.putExtra("current progress", currentProgress);
            sendBroadcast(aIntent);
        }
    }
}
