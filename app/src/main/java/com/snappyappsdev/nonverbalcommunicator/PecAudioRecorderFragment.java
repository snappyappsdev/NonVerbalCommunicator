package com.snappyappsdev.nonverbalcommunicator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.snappyappsdev.nonverbalcommunicator.databinding.FragmentPecAudioRecorderBinding;

import java.io.File;
import java.util.UUID;

/**
 * Created by lrocha on 1/9/18.
 */

public class PecAudioRecorderFragment extends Fragment {
    private static final String TAG = "PecAudioRecorderFragmen";
    private static final String LOG_TAG = "Audio";

    private static final String ARG_PEC_ID = "pec_id";

    private boolean isRecording;
    private File mPhotoFile;
    private File mSoundFile;
    private Pec mPec;
    ImageButton pecPhoto;
    TextView pecName;
    TextView audioOptionsTv;
    ImageButton audioOptionsIb;
    ImageButton saveButton;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private AudioRecorder mRecorder = null;

    private AudioPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    public static PecAudioRecorderFragment newInstance(UUID pecId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEC_ID, pecId);

        Log.d(TAG,"Creating a Fragment with arguments pecId: " + pecId);
        PecAudioRecorderFragment fragment = new PecAudioRecorderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecorder.destroy();
        mPlayer.destroy();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PecManager pecManager = PecManager.get(getActivity());
        UUID pecId = (UUID) getArguments().getSerializable(ARG_PEC_ID);

        mPec = pecManager.getPec(pecId);

        if(mPec == null){
            Log.d(TAG,"PecId does not exist in db");
            mPec = new Pec(pecId);
        }

        mPhotoFile = pecManager.getPhotoFile(mPec);
        mSoundFile = pecManager.getSoundFile(mPec);
        this.requestPermissions(permissions,REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.destroy();
        mPlayer = null;
        mRecorder.destroy();
        mRecorder = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPecAudioRecorderBinding binding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_pec_audio_recorder,container,false);

        mPlayer = new AudioPlayer();
        pecPhoto = binding.ivPecPhoto;
        Bitmap bitmap = PictureUtils.getScaledBitmap(
                mPhotoFile.getPath(), getActivity());
        pecPhoto.setImageBitmap(bitmap);
        pecPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    if(mSoundFile.exists()) {
                        Toast.makeText(getActivity(),"Start playing",Toast.LENGTH_SHORT).show();
                        mPlayer.play(mSoundFile.getAbsolutePath());
                    }else {
                        Toast.makeText(getActivity(),"Already Playing",Toast.LENGTH_SHORT).show();
                    }
                }
        });

        pecName = binding.tvPecName;
        pecName.setText(mPec.getTitle());


        audioOptionsTv = binding.tvAudioOptions;
        audioOptionsIb = binding.ibAudioOption;

        mRecorder = new AudioRecorder();

        audioOptionsIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = !isRecording;
                if(isRecording){
                    mRecorder.startRecording(mSoundFile.getAbsolutePath());
                }else{
                    mRecorder.stopRecording();
                }
                displayState();
            }
        });

        displayState();

        saveButton = binding.ibAudioSaveOption;
        //TODO save sound file

        return binding.getRoot();
    }
    private void displayState(){
        if (isRecording){
            Log.d(TAG,"Currently recording displaying state");
            recordState();
        }else{
            Log.d(TAG,"Currently idle displaying state");
            idleState();
        }
    }

    private void idleState(){
        audioOptionsTv.setText(R.string.record_audio);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_btn_speak_now);
        audioOptionsIb.setImageDrawable(drawable);
    }

    private void recordState(){
        audioOptionsTv.setText(R.string.stop_audio);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_media_pause);
        audioOptionsIb.setImageDrawable(drawable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(!permissionToRecordAccepted){
            Log.d(TAG, "onRequestPermissionsResult failed");
        }
    }
}
