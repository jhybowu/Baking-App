package com.example.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeStep;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class RecipeStepDetailFragment extends Fragment /* implements ExoPlayer.EventListener */ {
    private FragmentRecipeStepDetailBinding mBinding = null;
    private RecipeStep mRecipeStep = null;
    private ExoPlayer mVideoPlayer = null;
    private ExoPlayer mImagePlayer = null;
    private MediaSessionCompat mMediaSession = null;
    private PlaybackStateCompat.Builder mStateBuilder = null;
    private long mPosition = 0;

    public RecipeStepDetailFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && mPosition == 0) {
            if (savedInstanceState.containsKey(getString(R.string.key_video_position))) {
                mPosition = savedInstanceState.getLong(getString(R.string.key_video_position));
            }
            else {
                mPosition = 0;
            }
        }

        if (mVideoPlayer != null) {
            mVideoPlayer.seekTo(mPosition);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_step_detail,
                container,
                false);
        Bundle fragmentArgs = getArguments();

        if (fragmentArgs != null) {
            if (fragmentArgs.containsKey(getString(R.string.key_recipe_step))) {
                mRecipeStep = fragmentArgs.getParcelable(getString(R.string.key_recipe_step));
            }
        }

        if (mRecipeStep != null) {
            mBinding.description.tvRecipeDescription.setText(mRecipeStep.getDescription());

            if (mRecipeStep.getVideoURL() != null && !mRecipeStep.getVideoURL().isEmpty()) {
                if (mVideoPlayer != null) {
                    releasePlayer();
                }
                mVideoPlayer = initializePlayer(mRecipeStep.getVideoURL(),
                        mBinding.video.pvRecipeStepVideo);
            }
            else {
                mBinding.llVideo.setVisibility(View.GONE);
            }

            if (mRecipeStep.getThumbnailURL() != null && !mRecipeStep.getThumbnailURL().isEmpty()) {
                if (mImagePlayer != null) {
                    releaseImagePlayer();
                }

                mImagePlayer = initializePlayer(mRecipeStep.getThumbnailURL(),
                        mBinding.thumbnail.pvRecipeStepThumbnail);
            }
            else {
                mBinding.llThumbnail.setVisibility(View.GONE);
            }
        }

        if (mBinding != null) {
            return mBinding.getRoot();
        }
        else {
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.key_video_position), mPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseImagePlayer();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseImagePlayer();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoPlayer != null) {
            mVideoPlayer.setPlayWhenReady(false);
            mPosition = mVideoPlayer.getCurrentPosition();
        }

        releaseImagePlayer();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVideoPlayer == null && mRecipeStep != null && mRecipeStep.getVideoURL() != null &&
                !mRecipeStep.getVideoURL().isEmpty()) {
            // initializeMediaSession();
            mVideoPlayer = initializePlayer(mRecipeStep.getVideoURL(),
                    mBinding.video.pvRecipeStepVideo);
            mVideoPlayer.seekTo(mPosition);
        }

        if (mImagePlayer == null && mRecipeStep != null && mRecipeStep.getThumbnailURL() != null &&
                !mRecipeStep.getThumbnailURL().isEmpty()) {
            mImagePlayer = initializePlayer(mRecipeStep.getThumbnailURL(),
                    mBinding.thumbnail.pvRecipeStepThumbnail);
        }
    }

    private ExoPlayer initializePlayer(String urlString, PlayerView playerView) {
        ExoPlayer player = null;

        if (urlString != null && !urlString.isEmpty()) {
            player = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultTrackSelector());
            playerView.setPlayer(player);
            // player.addListener(this);
            playerView.setControllerShowTimeoutMs(0);
            playerView.setControllerHideOnTouch(false);

            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(),
                                    "com.example.bakingapp"))).
                    createMediaSource(Uri.parse(urlString).buildUpon().build());

            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }

        return player;
    }

    private void releaseImagePlayer() {
        if (mImagePlayer != null) {
            mImagePlayer.setPlayWhenReady(false);
            mImagePlayer.stop();
            mImagePlayer.release();
            mImagePlayer = null;
        }
    }

    private void releasePlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer.setPlayWhenReady(false);
            mVideoPlayer.stop();
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }
}
