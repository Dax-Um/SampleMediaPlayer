package com.example.samplemediaplayer.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemediaplayer.databinding.PlayListItemBinding;
import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.utils.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    List<MusicListItemModel> musicListItemModelList=new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    static Context context;
    OnCheckBoxClickEventListener checkBoxClickListener;
    static OnItemClickEventListener itemClickListener;
    HashMap<Integer, CheckBox> checkBoxHashMap = new HashMap<>();
    public interface OnCheckBoxClickEventListener {
        void onCheckBoxClick(int position, HashMap<Integer, CheckBox> checkBoxHashMap);
    }
    public void setOnCheckBoxClickListener(OnCheckBoxClickEventListener onItemClickListener) {
        checkBoxClickListener = onItemClickListener;
    }
    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickEventListener onItemClickListener){
        itemClickListener = onItemClickListener;
    }
    private static SparseBooleanArray mClickCheckBoxItems = new SparseBooleanArray(0);
    public static class ViewHolder extends RecyclerView.ViewHolder{
        PlayListItemBinding itemBinding;
        public ViewHolder(@NonNull PlayListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            this.itemBinding.getRoot().setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){itemClickListener.onItemClick(v, pos);}
            });
        }

    }
    public PlayListAdapter (Context context){
        PlayListAdapter.context = context;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setMusicListData(List<MusicListItemModel> musicListItemModelList){
        this.musicListItemModelList = musicListItemModelList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlayListItemBinding itemBinding = PlayListItemBinding.inflate(LayoutInflater
                                                            .from(parent.getContext()));
        return new ViewHolder(itemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.ViewHolder holder, int position) {
        MusicListItemModel musicListItemModel = musicListItemModelList.get(position);
        Bitmap albumArt = DataUtil.getThumbNail(context, musicListItemModel.album_id,40,40);
        String title = musicListItemModel.title;
        String artist = musicListItemModel.artist;
        int _id = musicListItemModel._id;

        if (albumArt != null){holder.itemBinding.albumArt.setImageBitmap(albumArt);}
        else{holder.itemBinding.albumArt.setImageResource(R.drawable.default_album_art);}

        holder.itemBinding.title.setText(title);
        holder.itemBinding.artist.setText(artist);
        holder.itemBinding.getRoot().setTag(_id);

        holder.itemBinding.checkbox.setOnClickListener(v -> {
            if (holder.itemBinding.checkbox.isChecked()){
                checkBoxHashMap.put(position, holder.itemBinding.checkbox);
            }else {checkBoxHashMap.remove(position);}

            checkBoxClickListener.onCheckBoxClick(position, checkBoxHashMap);

            if(mClickCheckBoxItems.get(position)) {
                mClickCheckBoxItems.delete(position);
                mClickCheckBoxItems.put(position, false);
            }else {mClickCheckBoxItems.put(position, true);}

            checkStatus(position, holder);
        });
        checkStatus(position, holder);
    }
    public void checkStatus(int position, PlayListAdapter.ViewHolder holder){
        if(mClickCheckBoxItems.get(position)){
            holder.itemBinding.checkbox.setChecked(true);
        }else{
            holder.itemBinding.checkbox.setChecked(false);
        }
    }
    public void selectAll(){
        mClickCheckBoxItems.clear();
        for(int i=0;i<musicListItemModelList.size();i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mClickCheckBoxItems.put(i,true);
            }
        }
        notifyDataSetChanged();
    }
    public void cancelAll(){
        for(int i=0;i<mClickCheckBoxItems.size();i++){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mClickCheckBoxItems.setValueAt(i, false);
            }
        }
        notifyDataSetChanged();
    }
    public void setClear(){
        for (Map.Entry<Integer, CheckBox> elem: checkBoxHashMap.entrySet()){
            elem.getValue().setChecked(false);}
        checkBoxHashMap.clear();
        cancelAll();
    }
    public int getItemTag(int pos){
        return musicListItemModelList.get(pos).getId();
    }
    @Override
    public int getItemCount() {
        return musicListItemModelList.size();
    }
    @Override
    public int getItemViewType(int position) {return super.getItemViewType(position);}
}
