package com.autoplayvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autoplayvideo.Constants.FeedsConstant;
import com.autoplayvideo.FeedsApplication;
import com.autoplayvideo.R;
import com.autoplayvideo.activities.MainActivity;
import com.autoplayvideo.interfaces.FeedsClickListener;
import com.autoplayvideo.interfaces.VolumeStateChangedListener;
import com.autoplayvideo.model.FeedsModel;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;


/**
 * Created by Navjot Singh
 * on 11/1/19.
 * Adapter class for RecyclerView in {@link MainActivity}
 * <p>
 * this class depicts the manual handling of the operations
 * and managing the states by overriding the necessary methods directly from third party library
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder> implements VolumeStateChangedListener {

    private Context context;
    private List<FeedsModel> mPostList;
    private FeedsClickListener listener;

    private Container mContainer;

    private int oldPosition;
    private boolean isMute;


    public FeedsAdapter(List<FeedsModel> postList, FeedsClickListener listener) {
        this.mPostList = postList;
        this.listener = listener;
        isMute = true;
    }


    @NonNull
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FeedsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_feed, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, int position) {

        FeedsModel feedsEntity = mPostList.get(position);

        if (feedsEntity != null) {

            holder.tvName.setText(feedsEntity.getName());

            //setting up view pager for images & videos
            MultipleMediaPagerAdapter mediaPagerAdapter = new MultipleMediaPagerAdapter(feedsEntity.getPostMedia(), isMute, FeedsAdapter.this);
            holder.viewPager.setAdapter(mediaPagerAdapter);

            // setting current visible page per item position
            if (holder.viewPager.getCurrentItem() != mPostList.get(position).getCurrentPagePosition())
                holder.viewPager.setCurrentItem(feedsEntity.getCurrentPagePosition());

            // setting post count per feed
            if (feedsEntity.getPostMedia() != null && feedsEntity.getPostMedia().size() > 1) {
                String str = (holder.viewPager.getCurrentItem() + 1) + "/" + feedsEntity.getPostMedia().size();
                holder.tvImageNumber.setText(str);
                holder.tvImageNumber.setVisibility(View.VISIBLE);
            } else {
                holder.tvImageNumber.setVisibility(View.INVISIBLE);
            }

        }
    }


    @Override
    public int getItemCount() {
        return mPostList.size();
    }


    /**
     * @param isMute is true if volume is mute else false
     */
    @Override
    public void onVolumeStateChanged(boolean isMute) {
        this.isMute = isMute;
    }


    class FeedsViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, ViewPager.OnPageChangeListener, Playable.EventListener {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_image_no)
        TextView tvImageNumber;
        @BindView(R.id.vp_media_slider)
        WrapContentViewPager viewPager;
        @BindView(R.id.player)
        PlayerView playerView;

        ExoPlayerViewHelper helper;

        FeedsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewPager.addOnPageChangeListener(this);

        }

        @OnClick({R.id.tv_name,})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.tv_name:
                    if (listener != null) {
                        listener.onNameClicked(getAdapterPosition());
                    }
                    break;

            }
        }


        @NonNull
        @Override
        public View getPlayerView() {
            PlayerView s;
            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null)
                s = view.findViewById(R.id.player);
            else
                s = playerView;

            return s;
        }


        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            if (helper != null)
                return helper.getLatestPlaybackInfo();
            else
                return new PlaybackInfo();
        }


        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            initHelper(container, playbackInfo, Uri.parse(mPostList.get(getAdapterPosition()).getPostMedia().get(viewPager.getCurrentItem()).getUrl()));
            mContainer = container;

        }


        /**
         * method to initialize ExoPlayerViewHelper
         *
         * @param container    {@link Container}
         * @param playbackInfo {@link PlaybackInfo}
         * @param uri          {@link Uri}
         */
        private void initHelper(Container container, PlaybackInfo playbackInfo, Uri uri) {
            if (helper == null) {
                helper = new ExoPlayerViewHelper(this, uri, null, FeedsApplication.getInstance().getToroConfig());
            }

            helper.initialize(container, playbackInfo);
            helper.addEventListener(this);

        }


        @Override
        public void play() {
            if (helper != null)
                helper.play();

            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null) {
                ProgressBar progressBar = view.findViewById(R.id.pb_loader);
                progressBar.setVisibility(View.GONE);
            }
            listener.setUpHelper(helper, getAdapterPosition());
        }


        @Override
        public void pause() {
            if (helper != null)
                helper.pause();

            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null) {
                ProgressBar progressBar = view.findViewById(R.id.pb_loader);
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
            }
            listener.setUpHelper(helper, getAdapterPosition());
        }


        @Override
        public boolean isPlaying() {
            return (helper != null && helper.isPlaying());
        }


        @Override
        public void release() {
            if (helper != null) {
                helper.release();
                helper = null;
            }
            listener.setUpHelper(helper, getAdapterPosition());
        }


        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) > 0.60;
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        public void setMute(boolean isMute) {
            // update mute status in adapter
            if (viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter() instanceof MultipleMediaPagerAdapter) {
                MultipleMediaPagerAdapter adapter = (MultipleMediaPagerAdapter) viewPager.getAdapter();
                adapter.setMute(isMute);
            }
        }


        @Override
        public void onPageSelected(int position) {
            setMute(isMute);
            if (mPostList.get(getAdapterPosition()) != null) {
                FeedsModel feedsEntity = mPostList.get(getAdapterPosition());

                // setting currently visible page position to manage state of visible page in viewPager while scrolling
                feedsEntity.setCurrentPagePosition(position);

                // updating post position per feed count
                String str = (position + 1) + "/" + feedsEntity.getPostMedia().size();
                tvImageNumber.setText(str);

                // pause the previously playing video in viewPager
                if (feedsEntity.getPostMedia().size() > oldPosition) {
                    if (feedsEntity.getPostMedia().get(oldPosition).getPostType() != FeedsConstant.FEED_TYPE_IMAGE) {
                        View view = viewPager.findViewWithTag(oldPosition);
                        if (view != null) {
                            PlayerView playerView = view.findViewById(R.id.player);
                            if (playerView.getPlayer() != null) {
                                playerView.getPlayer().setPlayWhenReady(false);
//                                playerView.getPlayer().stop(true);
                            }
                        }
                    }
                }

                // play new video in viewPager
                if (feedsEntity.getPostMedia().get(position).getPostType() != FeedsConstant.FEED_TYPE_IMAGE) {
                    /*
                     * It's not optimised but a workaround to overcome disadvantage to toro lib container
                     * to support multiple video playing in a row in viewPager
                     * first release the helper for video for previous page,
                     * then init new helper for video for current page
                     */
                    release();
                    if (mContainer != null) {
                        Uri uri = Uri.parse(feedsEntity.getPostMedia().get(viewPager.getCurrentItem()).getUrl());
                        initHelper(mContainer, new PlaybackInfo(), uri);
                        play(); // play explicitly
                    }
                }
            }

            oldPosition = position;
        }


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
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (getAdapterPosition() >= 0 && getAdapterPosition() < mPostList.size()) {
                FeedsModel feedsEntity = mPostList.get(getAdapterPosition());
                if (feedsEntity != null) {
                    if (feedsEntity.getPostMedia().get(viewPager.getCurrentItem()).getPostType() == FeedsConstant.FEED_TYPE_VIDEO) {
                        View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
                        ProgressBar progressBar = null;

                        if (view != null) {
                            progressBar = view.findViewById(R.id.pb_loader);
                            ImageView ivSound = view.findViewById(R.id.iv_sound);

                            ivSound.setImageResource(isMute ? R.drawable.ic_volume_off : R.drawable.ic_volume_on);
                        }

                        switch (playbackState) {
                            case Player.STATE_BUFFERING:
                                if (progressBar != null)
                                    progressBar.setVisibility(View.VISIBLE);

                                break;

                            case Player.STATE_ENDED:
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);

                                break;

                            case Player.STATE_IDLE:

                                break;

                            case Player.STATE_READY:
                                if (progressBar != null)
                                    progressBar.setVisibility(View.GONE);

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
            setMute(isMute);
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


}
