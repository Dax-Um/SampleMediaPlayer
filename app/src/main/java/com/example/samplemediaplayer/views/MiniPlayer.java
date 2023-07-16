package com.example.samplemediaplayer.views;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.databinding.FragmentMiniPlayerBinding;
import com.example.samplemediaplayer.models.MiniPlayerModel;
import com.example.samplemediaplayer.models.MusicPlayerModel;
import com.example.samplemediaplayer.utils.DataUtil;
import com.example.samplemediaplayer.utils.ViewUtil;
import com.example.samplemediaplayer.viewmodels.MiniPlayerViewModel;

import java.util.ArrayList;

public class MiniPlayer extends Fragment {
    MiniPlayerViewModel viewModel;
    MiniPlayerModel miniPlayerModel;
    int resId = 0;
    FragmentMiniPlayerBinding fragmentMiniPlayerBinding;
    ArrayList<Integer> idList = new ArrayList<>();
    int _id;
    Context context;
    boolean startService = false;
    boolean init = true;
    Bitmap bitmap;
    public MiniPlayer() {}
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clickEvents();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMiniPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mini_player,
                container, false);
        return fragmentMiniPlayerBinding.getRoot();
    }
    public void setData(int _id){
        this._id = _id;
        idList = getArguments().getIntegerArrayList("ID LIST");
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MiniPlayerViewModel(getActivity().getApplication(),_id, idList);
            }
        };
        viewModel = new ViewModelProvider(this, factory).get(MiniPlayerViewModel.class);
        viewModel.loadAllById(_id).observe(MiniPlayer.this,
                (Observer<? super MusicPlayerModel>) musicInfo -> {
                    updateUi(musicInfo);
                    if(init){
                        controlService();
                    }
    });
    }
    public void updateUi(MusicPlayerModel musicPlayerModel){
        if(musicPlayerModel.getAlbumArt()!=null){
            bitmap = DataUtil.resizeBitmap(musicPlayerModel.getAlbumArt(),
                    80,100);
        } else{
            bitmap = BitmapFactory.decodeResource(getResources(), musicPlayerModel.getRes());
        }

        miniPlayerModel = new MiniPlayerModel(
                musicPlayerModel.getId(), musicPlayerModel.getTitle(),
                musicPlayerModel.getArtist(), bitmap,
                musicPlayerModel.getFile_path()
        );

        ViewUtil.setCustomText(fragmentMiniPlayerBinding.miniTitle, miniPlayerModel.getTitle());
        ViewUtil.setCustomText(fragmentMiniPlayerBinding.miniArtist, miniPlayerModel.getArtist());
        if(miniPlayerModel.getAlbumArt() == null) {resId = R.drawable.default_album_art;}
        ViewUtil.loadImg(fragmentMiniPlayerBinding.miniAlbumArt, miniPlayerModel.getAlbumArt(), resId);
    }
    public void controlService(){
        startService = viewModel.checkServiceList();
        if (startService){viewModel.stopPlayService();}
        else{viewModel.startPlayService();}
    }
    public void updateId(){
        init = false;
        viewModel.updateId(getArguments().getInt("current id"),
                getArguments().getIntegerArrayList("ID LIST"));
        setData(getArguments().getInt("current id"));
    }
    public void clickEvents(){
        fragmentMiniPlayerBinding.miniAlbumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PlayerIntent = new Intent(getActivity(), MusicPlayer.class);
                PlayerIntent.putExtra("id", miniPlayerModel.getId());
                PlayerIntent.putIntegerArrayListExtra("ID LIST", idList);
                PlayerIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(PlayerIntent);
            }
        });
        fragmentMiniPlayerBinding.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMiniPlayerBinding.pauseButton.setVisibility(View.INVISIBLE);
                fragmentMiniPlayerBinding.playButton.setVisibility(View.VISIBLE);
                viewModel.pausePlayService();
            }
        });
        fragmentMiniPlayerBinding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMiniPlayerBinding.pauseButton.setVisibility(View.VISIBLE);
                fragmentMiniPlayerBinding.playButton.setVisibility(View.INVISIBLE);
                viewModel.restartPlayService();
            }
        });
        fragmentMiniPlayerBinding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init = true;
                viewModel.prePlayService();
            }
        });
        fragmentMiniPlayerBinding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init = true;
                viewModel.nextPlayService();
            }
        });
    }
}