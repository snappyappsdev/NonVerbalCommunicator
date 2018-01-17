package com.snappyappsdev.nonverbalcommunicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class PecGridActivity extends SingleFragmentActivity {
    private static final String TAG = "PecGridActivity";

    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "creating PecGridFragment instance");
        return PecGridFragment.newInstance();
    }
}
