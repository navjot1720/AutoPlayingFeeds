package com.autoplayvideo.helper;

import android.net.Uri;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.autoplayvideo.Constants.FeedsConstant;
import com.autoplayvideo.FeedsApplication;
import com.autoplayvideo.views.WrapContentViewPager;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;


/**
 * Created by Navjot Singh
 * on 14/1/19.
 */

public abstract class AutoPlayViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, ViewPager.OnPageChangeListener, Playable.EventListener {

    private WrapContentViewPager viewPager;
    private Container mContainer;

    public ExoPlayerViewHelper helper;

    private int oldPagePosition;
    private int currentPagePosition;

    private boolean isMute;


    public AutoPlayViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public abstract int getPostType(int pagePosition);

    public abstract int getPostType(int position, int pagePosition);

    public abstract WrapContentViewPager setViewPager();

    public abstract void pausePreviouslyPlayingVideoOnPageSelected(int oldPagePosition);

    public abstract String setVideoUrlOnPageSelected(int currentPagePosition);


    /**
     * initializing required views
     */
    @CallSuper
    public void initView() {
        viewPager = setViewPager();

        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
        }
    }


    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        if (helper != null)
            return helper.getLatestPlaybackInfo();
        else
            return new PlaybackInfo();
    }


    @NonNull
    @Override
    public abstract PlayerView getPlayerView();


    @Override
    public abstract void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo);


    /**
     * method to initialize ExoPlayerViewHelper
     *
     * @param container    {@link Container}
     * @param playbackInfo {@link PlaybackInfo}
     * @param uri          {@link Uri}
     */
    public void initHelper(Container container, PlaybackInfo playbackInfo, Uri uri) {
        mContainer = container;
        if (helper == null) {
            helper = new ExoPlayerViewHelper(this, uri, null, FeedsApplication.getInstance().getToroConfig());
        }

        helper.initialize(container, playbackInfo);
        helper.addEventListener(this);

    }


    public boolean isMute() {
        return isMute;
    }


    public void setMute(boolean mute) {
        isMute = mute;
    }


    public float setVisibleAreaOffsetToPlay() {
        return ((float) 0.60);
    }


    @CallSuper
    @Override
    public void play() {
        if (helper != null)
            helper.play();

    }


    @CallSuper
    @Override
    public void pause() {
        if (helper != null)
            helper.pause();

    }


    @Override
    public boolean isPlaying() {
        return (helper != null && helper.isPlaying());
    }


    @CallSuper
    @Override
    public void release() {
        if (helper != null) {
            helper.release();
            helper = null;
        }
    }


    /**
     * method to set  visible Area Offset for player
     */
    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) > setVisibleAreaOffsetToPlay();
    }


    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }


    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @CallSuper
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (getAdapterPosition() >= 0) {
            if (getPostType(currentPagePosition) == FeedsConstant.FEED_TYPE_VIDEO) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        break;

                    case Player.STATE_ENDED:
                        break;

                    case Player.STATE_IDLE:
                        break;

                    case Player.STATE_READY:
                        if (helper != null) {
                            helper.setVolume(isMute ? 0F : 1F);
                        }
                        break;

                    default:
                        break;

                }
            }
        }
    }


    @CallSuper
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @CallSuper
    @Override
    public void onPageSelected(int position) {
        currentPagePosition = position;

        // pause the previously playing video in viewPager
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > oldPagePosition) {
            if (getPostType(oldPagePosition) == FeedsConstant.FEED_TYPE_VIDEO) {
                pausePreviouslyPlayingVideoOnPageSelected(oldPagePosition);
            }
        }

        // play new video in viewPager
        if (getPostType(position) == FeedsConstant.FEED_TYPE_VIDEO) {
            /*
            It 's not optimised but a workaround to overcome disadvantage to toro lib container
            to support multiple video playing in a row in viewPager
            first release the helper for video for previous page,
            then init new helper for video for current page
            */
            release();
            if (mContainer != null) {
                String currentVideoUrl = setVideoUrlOnPageSelected(position);
                if (currentVideoUrl != null) {
                    Uri uri = Uri.parse(currentVideoUrl);
                    initHelper(mContainer, new PlaybackInfo(), uri);
                    play(); // play explicitly
                }
            }
        }

        oldPagePosition = position;
    }


    @CallSuper
    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onRepeatModeChanged(int repeatMode) {
//            Log.e("exo", "onRepeatModeChanged");

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//            Log.e("exo", "onShuffleModeEnabledChanged");

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
//            Log.e("exo", "onPlayerError");

    }

    @Override
    public void onPositionDiscontinuity(int reason) {
//            Log.e("exo", "onPositionDiscontinuity");

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//            Log.e("exo", "onPlaybackParametersChanged");

    }

    @Override
    public void onSeekProcessed() {
//            Log.e("exo", "onSeekProcessed");

    }

    @Override
    public void onMetadata(Metadata metadata) {
//            Log.e("exo", "onMetadata");

    }

    @Override
    public void onCues(List<Cue> cues) {
//            Log.e("exo", "onCues");

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//            Log.e("exo", "onVideoSizeChanged");

    }

    @Override
    public void onRenderedFirstFrame() {
//            Log.e("exo", "onRenderedFirstFrame");

    }

}
