package com.example.elabelle.cp282final.activities;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.elabelle.cp282final.R;

import java.io.IOException;

public class AboutActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    Uri targetUri;

    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    boolean pausing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button buttonPlayVideo = (Button)findViewById(R.id.play_button_about);
        Button buttonPauseVideo = (Button)findViewById(R.id.pause_button_about);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.about_video);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();

        targetUri = Uri.parse("android.resource://" + getPackageName() + "/"
        + R.raw.testvid);

        buttonPlayVideo.setOnClickListener(arg0 -> {
            pausing = false;

            if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(surfaceHolder);

            try {
                mediaPlayer.setDataSource(getApplicationContext(), targetUri);
                mediaPlayer.prepare();
            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();

        });

        buttonPauseVideo.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(pausing){
                    pausing = false;
                    mediaPlayer.start();
                }
                else{
                    pausing = true;
                    mediaPlayer.pause();
                }
            }});
    }


    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}