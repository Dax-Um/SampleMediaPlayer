package com.example.samplemediaplayer.Views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.Room.MusicListDataBase;
import com.example.samplemediaplayer.ViewModels.PlayListViewModel;

import java.util.List;
import java.util.Objects;


public class PlayList extends AppCompatActivity {
    MusicListDataBase musicListDataBase;
    PlayListViewModel viewModel;
    boolean itemTouch;
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            itemTouch = false;
        }
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };
    private final RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            int position = rv.getChildAdapterPosition(child);
            if(MotionEvent.ACTION_UP == e.getAction() && itemTouch){
                TextView title_textView = rv.getChildViewHolder(Objects
                        .requireNonNull(rv.findChildViewUnder(e.getX(), e.getY())))
                        .itemView.findViewById(R.id.title);
                String title = (String)title_textView.getText();
                TextView artist_textView = rv.getChildViewHolder(Objects
                                .requireNonNull(rv.findChildViewUnder(e.getX(), e.getY())))
                        .itemView.findViewById(R.id.artist);
                String artist = (String)artist_textView.getText();
                int _id = (int) rv.getChildViewHolder(Objects
                        .requireNonNull(rv.findChildViewUnder(e.getX(), e.getY())))
                        .itemView.getTag();

                Intent PlayerIntent = new Intent(PlayList.this, MusicPlayer.class);
                PlayerIntent.putExtra("title", title);
                PlayerIntent.putExtra("artist", artist);
                PlayerIntent.putExtra("id", _id);
                startActivity(PlayerIntent);
                finish();
            } else if (MotionEvent.ACTION_DOWN == e.getAction()){
                itemTouch = true;
            }
            return false;
        }
        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
        @Override
        public int scrollVerticallyBy ( int dx, RecyclerView.Recycler recycler, RecyclerView.State state ) {
            int scrollRange = super.scrollVerticallyBy(dx, recycler, state);
            int overScroll = dx - scrollRange;
            if (overScroll > 0) {
                itemTouch = false; // bottom overscroll
            }
            else if (overScroll < 0) {
                itemTouch = false; // top overscroll
            }
            return scrollRange;
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_MEDIA_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_AUDIO}, 100);
        }

        musicListDataBase = Room.databaseBuilder(this,
                MusicListDataBase.class, "MusicList.db").build();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.addOnItemTouchListener(onItemTouchListener);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getApplication())
                .create(PlayListViewModel.class);
        viewModel.getAll(this).observe(this,
                (Observer<? super List<MusicListItemModel>>) music_list -> {
                PlayListAdapter adapter = new PlayListAdapter(this, music_list);
                recyclerView.setAdapter(adapter);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LIFECYCLE", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("LIFECYCLE", "DESTROY");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("LIFECYCLE", "START");
    }
}