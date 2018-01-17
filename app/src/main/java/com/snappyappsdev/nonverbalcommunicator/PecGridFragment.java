package com.snappyappsdev.nonverbalcommunicator;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.snappyappsdev.nonverbalcommunicator.databinding.FragmentPecGridBinding;
import com.snappyappsdev.nonverbalcommunicator.databinding.GridItemPecBinding;

import java.io.File;
import java.util.List;

/**
 * Created by lrocha on 12/25/17.
 */

public class PecGridFragment extends Fragment{
    private static final String TAG = "PecGridFragment";

    private PecSoundPool mPecSoundPool;
    private PecAdapter mAdapter;
    private RecyclerView mPecRecyclerView;


    public static PecGridFragment newInstance(){
        Log.d(TAG, "Returning a new PecGridFragment");
        return new PecGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mPecSoundPool = new PecSoundPool(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        FragmentPecGridBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pec_grid, container, false);

        mPecRecyclerView = binding.recyclerView;
        mPecRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mPecRecyclerView.setAdapter(new PecAdapter(PecManager.get(getActivity()).getPecs()));

        updateUI();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPecSoundPool.release();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        updateUI();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_pec_grid , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()){
            case R.id.new_pec:
                Pec pec = new Pec();
                Log.d(TAG, "New Pec menu item selected");
                Log.d(TAG, "New Pec id: " + pec.getId().toString());
                Intent intent = PecActivity
                        .newIntent(getActivity(), pec.getId());
                startActivity(intent);
                return true;
            case R.id.edit_pec:
                return true;
            case R.id.delete_pec:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        Log.d(TAG, "updateUI");
        PecManager pecManager = PecManager.get(getActivity());
        List<Pec> pecs = pecManager.getPecs();

        if (mAdapter == null){
            Log.d(TAG, "mAdapter is null");
            mAdapter = new PecAdapter(pecs);
            mPecRecyclerView.setAdapter(mAdapter);
            Log.d(TAG, "mAdapter has " + mAdapter.getItemCount() + " pecs");
        }else {
            Log.d(TAG, "mAdapter is not null setting pecs");
            mAdapter.setPecs(pecs);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "mAdapter has " + mAdapter.getItemCount() + " pecs");
        }
    }


    private class PecHolder extends RecyclerView.ViewHolder{
        GridItemPecBinding mBinding;

        public PecHolder(GridItemPecBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new PecViewModel(mPecSoundPool));
        }

        public void bind(Pec pec){
            mBinding.getViewModel().setPec(pec);
            updatePhotoView();
            mBinding.executePendingBindings();
        }

        private void updatePhotoView() {
            File mPhotoFile = PecManager.get(getActivity()).getPhotoFile(mBinding.getViewModel().getPec());

            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mBinding.pecHolderPhoto.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity());
                mBinding.pecHolderPhoto.setImageBitmap(bitmap);
            }
        }
    }
    private class PecAdapter extends RecyclerView.Adapter<PecHolder>{
        private List<Pec> mPecs;

        public PecAdapter(List<Pec> pecs) {
            mPecs = pecs;
        }

        @Override
        public PecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            GridItemPecBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.grid_item_pec, parent, false);
            return new PecHolder(binding);
        }

        @Override
        public void onBindViewHolder(PecHolder holder, int position) {
            Pec pec = mPecs.get(position);
            holder.bind(pec);
        }

        @Override
        public int getItemCount() {
            return mPecs.size();
        }

        public void setPecs(List<Pec> pecs){
            mPecs = pecs;
        }
    }
}