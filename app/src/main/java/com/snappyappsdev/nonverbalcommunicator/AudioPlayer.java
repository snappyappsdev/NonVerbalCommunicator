package com.snappyappsdev.nonverbalcommunicator;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by lrocha on 1/14/18.
 */

public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    private MediaPlayer mPlayer = null;

    public AudioPlayer(){
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
            }
        });
    }

    public void play(String path){
        if(!mPlayer.isPlaying()) {
            try {
                Log.d(TAG, "Playing " + path);
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "failed");
            }
        }
    }

    public void destroy(){
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
