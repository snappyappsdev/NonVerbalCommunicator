package com.snappyappsdev.nonverbalcommunicator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

/**
 * Created by lrocha on 1/12/18.
 */

public class PecAudioRecorderActivity extends SingleFragmentActivity {
    private static final String TAG = "PecAudioRecorderActivit";


    private static final String EXTRA_PEC_ID =
            "com.snappyappsdev.nonverbalcommunicator.pec_id";

    @Override
    protected Fragment createFragment() {
        UUID pecId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PEC_ID);
        Log.d(TAG, "Creating PecAudioRecorderFragment with extra PecId:" + pecId.toString());
        return PecAudioRecorderFragment.newInstance(pecId);
    }

    public static Intent newIntent(Context packageContext, UUID pecId) {
        Intent intent = new Intent(packageContext, PecAudioRecorderActivity.class);
        Log.d(TAG, "Creating intent with extra PecId:" + pecId.toString());
        intent.putExtra(EXTRA_PEC_ID, pecId);
        return intent;
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        UUID pecId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PEC_ID);

        Log.d(TAG, "Adding to PecActivity parent intent, extra PecId:" + pecId.toString());
        Intent intent = PecActivity
                .newIntent(this, pecId);
        return intent;
    }
}
