package com.snappyappsdev.nonverbalcommunicator;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by lrocha on 1/14/18.
 */

public class AudioRecorder {
    private static final String TAG = "AudioRecorder";
    private MediaRecorder mMediaRecorder = null;


    public void destroy(){
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void startRecording(String path) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(path);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setMaxDuration(R.integer.max_audio_duration);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        mMediaRecorder.start();
    }

    public void stopRecording() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

}
