package com.example.elabelle.cp282final.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.elabelle.cp282final.R;

import java.io.IOException;

public class ContactActivity extends AppCompatActivity {
    Uri targetUri;
    MediaPlayer songPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        songPlayer = new MediaPlayer();
        targetUri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.rageagainst);
        songPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            songPlayer.setDataSource(getApplicationContext(), targetUri);
            songPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        songPlayer.start();
    }

    public void onDestroy() {
        super.onDestroy();
        songPlayer.release();
    }
}
