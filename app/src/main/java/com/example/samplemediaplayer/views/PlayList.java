package com.example.samplemediaplayer.views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.example.samplemediaplayer.databinding.ActivityPlayListBinding;
import com.example.samplemediaplayer.models.MusicListItemModel;
import com.example.samplemediaplayer.R;
import com.example.samplemediaplayer.room.MusicListDataBase;
import com.example.samplemediaplayer.viewmodels.PlayListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayList extends AppCompatActivity {
    MusicListDataBase musicListDataBase;
    PlayListViewModel viewModel;
    ArrayList<Integer> idList = new ArrayList<>();
    int current_id = 0;
    ActivityPlayListBinding binding;
    MiniPlayer miniPlayer;
    List<MusicListItemModel> musicInfo;
    static HashMap<Integer, CheckBox> checkBoxHashMap = new HashMap<>();
    static PlayListAdapter adapter;
    Bundle bundle;
    Toolbar toolbar;
    Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Uri contentUri;
    List<Uri> mediaUris = new ArrayList<>();
    ArrayList<Integer> checkIdList = new ArrayList<>();
    boolean selectAll;
    boolean visibleState;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        miniPlayer = new MiniPlayer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentFrame, miniPlayer);
        fragmentTransaction.commit();

        binding = ActivityPlayListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        toolbar = binding.myToolbar;
        setSupportActionBar(toolbar);

        adapter = new PlayListAdapter(this);
        adapter.setOnCheckBoxClickListener(new PlayListAdapter.OnCheckBoxClickEventListener() {
            @Override
            public void onCheckBoxClick(int position,
                                        HashMap<Integer, CheckBox> checkBoxHashMap)
            {
                PlayList.checkBoxHashMap = checkBoxHashMap;
                CheckBox checkBox;

                if(PlayList.checkBoxHashMap.containsKey(position)){
                    checkBox = checkBoxHashMap.get(position);
                    visibleState = checkBox.isChecked();}
                else{visibleState = false;}

                setVisible(visibleState);
            }
        });
        adapter.setOnItemClickListener(new PlayListAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View view, int position) {
                current_id = (int) view.getTag();
                miniPlayer.setData(current_id);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getApplication())
                .create(PlayListViewModel.class);
        viewModel.getAll(this).observe(this, new Observer<List<MusicListItemModel>>() {
            @Override
            public void onChanged(List<MusicListItemModel> musicListItemModels) {
                musicInfo = musicListItemModels;
                adapter.setMusicListData(musicInfo);
                binding.testText.setText("전체: "+ String.valueOf(musicInfo.size()));
                idList = viewModel.getIdList();
                bundle = new Bundle();
                bundle.putIntegerArrayList("ID LIST", idList);
                miniPlayer.setArguments(bundle);
            }
        });

        binding.selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.selectAll.isChecked()){
                    adapter.selectAll();
                    selectAll = true;
                }else{
                    adapter.cancelAll();
                    selectAll = false;
                }
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        Drawable deleteIcon = ContextCompat.getDrawable(this, R.drawable.baseline_delete_24);
        menu.getItem(0).setIcon(deleteIcon);
        Drawable cancelIcon = ContextCompat.getDrawable(this, R.drawable.baseline_cancel_24);
        menu.getItem(2).setIcon(cancelIcon);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_cancel){
            adapter.setClear();
            setVisible(false);
            binding.selectAll.setChecked(false);
        } else if (item.getItemId() == R.id.action_delete) {
            if(selectAll){
                for(MusicListItemModel elem: musicInfo){checkIdList.add(elem.getId());}
            }else {
                for(int elem : checkBoxHashMap.keySet()){checkIdList.add(adapter.getItemTag(elem));}
                adapter.setClear();
            }

            for (int elem : checkIdList) {
                contentUri = ContentUris.withAppendedId(baseUri, elem);
                mediaUris.add(contentUri);
            }

            PendingIntent deletePendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                deletePendingIntent = MediaStore.createDeleteRequest(getContentResolver(), mediaUris);
                IntentSenderRequest intentSenderRequest =
                        new IntentSenderRequest.Builder(deletePendingIntent.getIntentSender()).build();
                activityResultLauncher.launch(intentSenderRequest);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_MEDIA_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_AUDIO}, 100);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE}, 300);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!MediaStore.canManageMedia(this)){
                Intent intent = new Intent(Settings.ACTION_REQUEST_MANAGE_MEDIA);
                startActivity(intent);
            }
        }
    }
    public void setVisible(boolean visibleState){
        if (visibleState){
            toolbar.setVisibility(View.VISIBLE);
            binding.selectAll.setVisibility(View.VISIBLE);
            binding.testText.setVisibility(View.INVISIBLE);
        } else{
            toolbar.setVisibility(View.INVISIBLE);
            binding.selectAll.setVisibility(View.INVISIBLE);
            binding.testText.setVisibility(View.VISIBLE);
        }
    }
    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.e("SUCCESS INTENT ","RESULT OK");
                    viewModel.delete(checkIdList);
                    setVisible(false);
                }
            });
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("current id")){
            Intent intent = getIntent();
            current_id = intent.getIntExtra("current id", 0);
            Bundle updateBundle = new Bundle();
            updateBundle.putInt("current id", current_id);
            updateBundle.putIntegerArrayList("ID LIST", idList);
            miniPlayer.setArguments(updateBundle);
            miniPlayer.updateId();
        }
    }
}