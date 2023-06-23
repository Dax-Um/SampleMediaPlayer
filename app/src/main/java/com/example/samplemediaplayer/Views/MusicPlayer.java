package com.example.samplemediaplayer.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.samplemediaplayer.R;

public class MusicPlayer extends AppCompatActivity {
    TextView titleTextView;
    TextView artistTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        int _id = intent.getIntExtra("id", 0);

        titleTextView = findViewById(R.id.title_textView);
        artistTextView = findViewById(R.id.artist_textView);

        titleTextView.setHorizontallyScrolling(true);
        titleTextView.setSelected(true);

        artistTextView.setHorizontallyScrolling(true);
        artistTextView.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ListIntent = new Intent(MusicPlayer.this, PlayList.class);
        startActivity(ListIntent);
        finish();

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