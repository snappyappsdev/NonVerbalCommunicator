package com.snappyappsdev.nonverbalcommunicator;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lrocha on 1/15/18.
 */

public class PecSoundPool {
    private static final String TAG = "PecSoundPool";

    private static final int MAX_SOUNDS = 1;

    private PecManager mPecManager;
    private Map<UUID,Integer> mPecMap;
    private SoundPool mSoundPool;

    public PecSoundPool(Context context) {
        mPecManager = PecManager.get(context);
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        mPecMap = new HashMap<>();
        loadSounds();
    }

    public void play(Pec pec) {
        Integer soundId = mPecMap.get(pec.getId());
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release(){
        mSoundPool.release();
    }

    private void loadSounds() {
        List<Pec> pecs = mPecManager.getPecs();
        for (Pec pec : pecs) {
            try {
                load(pec);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + pec.getSoundFileName(), ioe);
            }
        }
    }

    private void load(Pec pec) throws IOException {
        String soundPath = mPecManager.getSoundFile(pec).getAbsolutePath();
        int soundId = mSoundPool.load(soundPath, 1);
        mPecMap.put(pec.getId(),soundId);
    }
}
