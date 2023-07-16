package com.example.samplemediaplayer.views;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.samplemediaplayer.models.MusicPlayerModel;
import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.utils.ViewUtil;
import com.example.samplemediaplayer.viewmodels.MusicPlayerViewModel;
import com.example.samplemediaplayer.databinding.ActivityMusicPlayerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicPlayer extends AppCompatActivity{
    MusicPlayerViewModel viewModel;
    MusicPlayerModel musicPlayerModel;
    ActivityMusicPlayerBinding binding;
    int resId = 0;
    static int DEFAULT_ID = 123;
    boolean startService = false;
    boolean init = true;
    ArrayList<Integer> idList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        int _id = intent.getIntExtra("id", DEFAULT_ID);
        idList = intent.getIntegerArrayListExtra("ID LIST");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        binding.setMusicInfo(musicPlayerModel);
        binding.setLifecycleOwner(this);

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MusicPlayerViewModel(getApplication(),_id, binding, idList);
            }
        };
        viewModel = new ViewModelProvider(this, factory).get(MusicPlayerViewModel.class);
        viewModel.loadAllById().observe(this, (Observer<?super MusicPlayerModel>)musicInfo ->{
            musicPlayerModel = musicInfo;

            ViewUtil.setCustomText(binding.titleTextView, musicPlayerModel.getTitle());
            ViewUtil.setCustomText(binding.artistTextView, musicPlayerModel.getArtist());
            if(musicPlayerModel.getAlbumArt() == null) {resId = R.drawable.default_album_art;}
            ViewUtil.loadImg(binding.albumArtImageView, musicPlayerModel.getAlbumArt(), resId);
            viewModel.setCurrentProgress();

            if(init){init = false;}
            else{
                startService = viewModel.checkServiceList();
                if (startService){viewModel.stopPlayService();}
                else{viewModel.startPlayService();}
            }
        });
        clickEvents();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.unregisterPlayReceiver();
        int _id = viewModel.getId();
        Intent listIntent = new Intent(MusicPlayer.this, PlayList.class);
        listIntent.putExtra("current id", _id);
        listIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(listIntent);
    }
    public void clickEvents(){
        binding.informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        binding.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pauseButton.setVisibility(View.INVISIBLE);
                binding.playButton.setVisibility(View.VISIBLE);
                viewModel.pausePlayService();
            }
        });
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pauseButton.setVisibility(View.VISIBLE);
                binding.playButton.setVisibility(View.INVISIBLE);
                viewModel.restartPlayService();
            }
        });
        binding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService = viewModel.checkServiceList();
                if (startService){viewModel.stopPlayService();}
                else{viewModel.startPlayService();}
                viewModel.prePlayService();
            }
        });
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService = viewModel.checkServiceList();
                if (startService){viewModel.stopPlayService();}
                else{viewModel.startPlayService();}
                viewModel.nextPlayService();
            }
        });
    }
}