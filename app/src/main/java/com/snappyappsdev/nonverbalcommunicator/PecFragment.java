package com.snappyappsdev.nonverbalcommunicator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.snappyappsdev.nonverbalcommunicator.databinding.FragmentPecBinding;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by lrocha on 12/27/17.
 */

public class PecFragment extends Fragment {
    private static final String ARG_PEC_ID = "pec_id";

    private static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_SOUND = 1;

    private Pec mPec;
    private File mPhotoFile;
    private File mSoundFile;
    private ImageButton mPecPhotoButton;
    private ImageButton mCameraButton;
    private ImageButton mSoundButton;
    private EditText mNameField;
    private Button mResetButton;
    private Button mSaveButton;

    private AudioPlayer mPlayer = null;


    public static PecFragment newInstance(UUID pecId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEC_ID, pecId);

        PecFragment fragment = new PecFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PecManager pecManager = PecManager.get(getActivity());

        UUID pecId = (UUID) getArguments().getSerializable(ARG_PEC_ID);

        mPec = pecManager.getPec(pecId);

        if(mPec == null){
            mPec = new Pec(pecId);
        }

        mPhotoFile = pecManager.getPhotoFile(mPec);
        mSoundFile = pecManager.getSoundFile(mPec);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentPecBinding binding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_pec,container,false);

        if(mPec == null){
            mPec = new Pec();
        }

        mNameField = binding.enterNameText;
        mNameField.setText(mPec.getTitle());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvPecName.setText(charSequence.toString());
                mPec.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCameraButton = binding.pecCamera;
        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) !=null;
        mCameraButton.setEnabled(canTakePhoto);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.snappyappsdev.nonverbalcommunicator.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPecPhotoButton = binding.pecPhoto;
        updatePhotoView();
        mPlayer = new AudioPlayer();
        mPecPhotoButton.setOnClickListener(new View.OnClickListener() {
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


        mSaveButton = binding.pecSave;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PecManager.get(getActivity()).addPec(mPec);
            }
        });

        mSoundButton =binding.pecMicrophone;
        mSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PecAudioRecorderActivity
                        .newIntent(getActivity(), mPec.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_PEC_ID, mPec.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.snappyappsdev.nonverbalcommunicator.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPecPhotoButton.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPecPhotoButton.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlayer.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.destroy();
        mPlayer = null;
    }
}
