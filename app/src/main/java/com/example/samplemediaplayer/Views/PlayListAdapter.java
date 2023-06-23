package com.example.samplemediaplayer.Views;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private List<MusicListItemModel> musicListItemModelList = new ArrayList<>();
    static Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView albumArtImageView;
        TextView titleTextView;
        TextView artistTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            albumArtImageView = itemView.findViewById(R.id.album_art);
            titleTextView = itemView.findViewById(R.id.title);
            artistTextView = itemView.findViewById(R.id.artist);
        }
        public ImageView getAlbumArtImageView() {
            return albumArtImageView;
        }
        public TextView getTitleTextView() {
            return titleTextView;
        }
        public TextView getArtistTextView() {
            return artistTextView;
        }
    }
    public PlayListAdapter (Context context, List<MusicListItemModel> musicListItemModelList){
        this.musicListItemModelList = musicListItemModelList;
        this.context = context;
    }
    @NonNull
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.play_list_item, parent, false);
        PlayListAdapter.ViewHolder viewHolder = new PlayListAdapter.ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.ViewHolder holder, int position) {
        Bitmap albumArt = getThumbNail(musicListItemModelList.get(position).album_id);
        String title = musicListItemModelList.get(position).title;
        String artist = musicListItemModelList.get(position).artist;
        int _id = musicListItemModelList.get(position)._id;

        if (albumArt != null){holder.albumArtImageView.setImageBitmap(albumArt);}
        else{holder.albumArtImageView.setImageResource(R.drawable.default_album_art);}
        holder.titleTextView.setText(title);
        holder.artistTextView.setText(artist);
        holder.itemView.setTag(_id);
        Log.e("POSITION", String.valueOf(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("CLICK","ID: "+_id);
            }
        });
    }
    private Bitmap getThumbNail(Long album_id){
        Bitmap albumArt = null;
        Uri contentUri =  ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                album_id
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                albumArt = context
                        .getContentResolver()
                        .loadThumbnail(contentUri,
                                new Size(40, 40), null);
            } catch (IOException e) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 120;
//                albumArt = BitmapFactory.decodeResource((context
//                        .getResources()), R.drawable.default_thumbnail);
                albumArt = null;
                Log.e("EXCEPTION", e.toString());
            }
        }
        return albumArt;
    }
    @Override
    public int getItemCount() {
        return musicListItemModelList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
