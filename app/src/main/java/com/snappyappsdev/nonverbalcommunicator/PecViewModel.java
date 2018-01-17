package com.snappyappsdev.nonverbalcommunicator;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * Created by lrocha on 12/25/17.
 */

public class PecViewModel extends BaseObservable{
    private PecSoundPool mPecSoundPool;
    private Pec mPec;

    public PecViewModel(PecSoundPool pecSoundPool) {
        mPecSoundPool = pecSoundPool;
    }

    public Pec getPec() {
        return mPec;
    }

    public void setPec(Pec pec) {
        mPec = pec;
        notifyChange();
    }
    public Drawable getPhoto(){
        return null;
    }

    @Bindable
    public String getTitle(){
        return mPec.getTitle();
    }

    public void onButtonClicked(){
        mPecSoundPool.play(mPec);
    }
}
