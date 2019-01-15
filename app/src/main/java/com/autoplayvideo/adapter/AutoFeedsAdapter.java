package com.autoplayvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autoplayvideo.Constants.FeedsConstant;
import com.autoplayvideo.R;
import com.autoplayvideo.activities.MainActivity;
import com.autoplayvideo.helper.AutoPlayViewHolder;
import com.autoplayvideo.interfaces.FeedsClickListener;
import com.autoplayvideo.interfaces.VolumeStateChangedListener;
import com.autoplayvideo.model.FeedsModel;
import com.autoplayvideo.views.WrapContentViewPager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;


/**
 * Created by Navjot Singh
 * on 11/1/19.
 * Adapter class for RecyclerView in {@link MainActivity}
 * <p>
 * this class depicts the handling of the operations
 * and managing the states by overriding the necessary methods
 * from {@link AutoPlayViewHolder} by making your viewHolder extending it
 */

public class AutoFeedsAdapter extends RecyclerView.Adapter<AutoFeedsAdapter.AutoFeedViewHolder> implements VolumeStateChangedListener {

    private Context context;
    private List<FeedsModel> mPostList;
    private FeedsClickListener listener;

    private boolean isMute;


    public AutoFeedsAdapter(List<FeedsModel> postList, FeedsClickListener listener) {
        this.mPostList = postList;
        this.listener = listener;
        isMute = true;
    }


    @NonNull
    @Override
    public AutoFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AutoFeedViewHolder(LayoutInflater.from(context).inflate(R.layout.row_feed, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull AutoFeedViewHolder holder, int position) {

        FeedsModel feedsEntity = mPostList.get(position);

        if (feedsEntity != null) {

            holder.tvName.setText(feedsEntity.getName());

            //setting up view pager for images & videos
            MultipleMediaPagerAdapter mediaPagerAdapter = new MultipleMediaPagerAdapter(feedsEntity.getPostMedia(), isMute, AutoFeedsAdapter.this);
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


    class AutoFeedViewHolder extends AutoPlayViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_image_no)
        TextView tvImageNumber;
        @BindView(R.id.vp_media_slider)
        WrapContentViewPager viewPager;
        @BindView(R.id.player)
        PlayerView playerView;

        AutoFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // call this method to set up views in parent class
            initView();

        }


        @OnClick({R.id.tv_name})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.tv_name:
                    if (listener != null) {
                        listener.onNameClicked(getAdapterPosition());
                    }
                    break;
            }
        }


        @Override
        public WrapContentViewPager setViewPager() {
            return viewPager;
        }


        @NonNull
        @Override
        public PlayerView getPlayerView() {
            PlayerView s;
            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null)
                s = view.findViewById(R.id.player);
            else
                s = playerView;

            return s;
        }


        @Override
        public int getPostType(int pagePosition) {
            int type = FeedsConstant.FEED_TYPE_IMAGE;
            if (mPostList.get(getAdapterPosition()).getPostMedia() != null && mPostList.get(getAdapterPosition()).getPostMedia().size() > pagePosition)
                type = mPostList.get(getAdapterPosition()).getPostMedia().get(pagePosition).getPostType();

            return type;
        }


        @Override
        public int getPostType(int position, int pagePosition) {
            int type = FeedsConstant.FEED_TYPE_IMAGE;
            if (mPostList.size() > position && mPostList.get(position).getPostMedia().size() > pagePosition)
                type = mPostList.get(position).getPostMedia().get(pagePosition).getPostType();

            return type;
        }


        /**
         * initialize {@link ExoPlayerViewHelper} by calling {@link #initHelper(Container, PlaybackInfo, Uri)}
         */
        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            initHelper(container, playbackInfo, Uri.parse(mPostList.get(getAdapterPosition()).getPostMedia().get(viewPager.getCurrentItem()).getUrl()));
        }


        @Override
        public void play() {
            super.play();
            // Update UI accordingly
            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null) {
                ProgressBar progressBar = view.findViewById(R.id.pb_loader);
                progressBar.setVisibility(View.GONE);
            }
        }


        @Override
        public void pause() {
            super.pause();
            // Update UI accordingly
            View view = viewPager.findViewWithTag(viewPager.getCurrentItem());
            if (view != null) {
                ProgressBar progressBar = view.findViewById(R.id.pb_loader);
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void release() {
            super.release();
        }


        @Override
        public void pausePreviouslyPlayingVideoOnPageSelected(int oldPagePosition) {
            // pause the previously playing video in viewPager
            if (mPostList.get(getAdapterPosition()) != null) {
                FeedsModel feedsEntity = mPostList.get(getAdapterPosition());
                if (feedsEntity.getPostMedia().size() > oldPagePosition) {
                    if (getPostType(oldPagePosition) == FeedsConstant.FEED_TYPE_VIDEO) {
                        View view = viewPager.findViewWithTag(oldPagePosition);
                        if (view != null) {
                            PlayerView playerView = view.findViewById(R.id.player);
                            if (playerView.getPlayer() != null) {
                                playerView.getPlayer().setPlayWhenReady(false);
                                // playerView.getPlayer().stop(true);
                            }
                        }
                    }
                }
            }
        }


        @Override
        public String setVideoUrlOnPageSelected(int currentPagePosition) {
            return mPostList.get(getAdapterPosition()).getPostMedia().get(currentPagePosition).getUrl();
        }


        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            setMute(isMute);

            // Update UI accordingly
            if (mPostList.get(getAdapterPosition()) != null) {
                FeedsModel feedsEntity = mPostList.get(getAdapterPosition());

                // setting currently visible page position to manage state of visible page in viewPager while scrolling
                feedsEntity.setCurrentPagePosition(position);

                // updating post position per feed count
                String str = (position + 1) + "/" + feedsEntity.getPostMedia().size();
                tvImageNumber.setText(str);
            }
        }


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            setMute(isMute);

            // Update UI accordingly
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

                                break;

                            default:
                                break;

                        }
                    }
                }
            }
        }

    }

}
