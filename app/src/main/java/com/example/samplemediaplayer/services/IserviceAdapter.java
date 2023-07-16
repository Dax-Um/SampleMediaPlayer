package com.example.samplemediaplayer.services;

import android.content.Intent;

import java.util.ArrayList;

public interface IserviceAdapter {
    Intent PlayerIntent = new Intent("com.example.PLAY_TO_SERVICE");
    void stopPlayService();
    void startPlayService();
    void pausePlayService();
    void restartPlayService();
    void nextPlayService();
    void prePlayService();
}
