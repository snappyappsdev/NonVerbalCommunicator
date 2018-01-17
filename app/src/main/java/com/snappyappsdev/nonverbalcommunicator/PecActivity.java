package com.snappyappsdev.nonverbalcommunicator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.UUID;

/**
 * Created by lrocha on 12/27/17.
 */

public class PecActivity extends SingleFragmentActivity {
    private static final String TAG = "PecActivity";
    private static final String EXTRA_PEC_ID =
            "com.snappyappsdev.nonverbalcommunicator.pec_id";

    @Override
    protected Fragment createFragment() {
        UUID pecId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PEC_ID);
        Log.d(TAG, "creating PecFragment instance with extra PecId : " + pecId.toString());
        return PecFragment.newInstance(pecId);
    }

    public static Intent newIntent(Context packageContext, UUID pecId) {
        Log.d(TAG, "creating new intent with extra PecId: " + pecId.toString());
        Intent intent = new Intent(packageContext, PecActivity.class);
        intent.putExtra(EXTRA_PEC_ID, pecId);
        return intent;
    }
}
