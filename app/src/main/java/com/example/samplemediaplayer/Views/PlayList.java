package com.example.samplemediaplayer.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;

import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.Services.MusicFileWorker;

public class PlayList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        OneTimeWorkRequest queryMusicFile = new OneTimeWorkRequest.Builder(MusicFileWorker.class)
                .build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(queryMusicFile);
    }
}