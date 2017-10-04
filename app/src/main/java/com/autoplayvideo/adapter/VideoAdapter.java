package com.autoplayvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autoplayvideo.R;
import com.autoplayvideo.model.VideoItem;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.helper.LoopingExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

/**
 * Created by Dev Sharma on 2/10/17.
 * Adapter of video player container
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoItem> videoItemArrayList;
    private Context context;

    public VideoAdapter(ArrayList<VideoItem> videoItemArrayList, Context mainActivity) {
        this.videoItemArrayList = videoItemArrayList;
        this.context = mainActivity;

    }

    /**
     * resturns view type
     *
     * @param position position of item
     * @return 0 for video and 1 for image
     */
    @Override
    public int getItemViewType(int position) {
        if (videoItemArrayList.get(position).getVideoUrl().length() == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new ViewHolderVideo(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false));
                break;
            case 1:
                holder = new ViewHolderImage(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
                Glide.with(context).load(videoItemArrayList.get(position).getImageUrl()).into(viewHolderVideo.ivThumbnail);
                viewHolderVideo.tvName.setText(videoItemArrayList.get(position).getName());
                break;
            case 1:
                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                Glide.with(context).load(videoItemArrayList.get(position).getImageUrl()).into(viewHolderImage.ivThumbnail);
                viewHolderImage.tvName.setText(videoItemArrayList.get(position).getName());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return videoItemArrayList.size();
    }


    /**
     * ViewHolder for image item
     */
    class ViewHolderImage extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;

        public ViewHolderImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * ViewHolder for video items
     */
    class ViewHolderVideo extends RecyclerView.ViewHolder implements ToroPlayer {

        @BindView(R.id.simpleexoplayer)
        SimpleExoPlayerView simpleexoplayer;
        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.tv_name)
        TextView tvName;
        private LoopingExoPlayerViewHelper helper;

        ViewHolderVideo(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return simpleexoplayer;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        /**
         * initialize your url with help.  use LoopingExoPlayerViewHelper for looping videos else use ExoplayerHelper
         *
         * @param container    the RecyclerView contains this Player.
         * @param playbackInfo initialize info for the preparation.
         */
        @Override
        public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
            if (helper == null) {
                if (getAdapterPosition() > -1) {
                    helper = new LoopingExoPlayerViewHelper(container, this, Uri.parse(videoItemArrayList.get(getAdapterPosition()).getVideoUrl()));
                }
            }
            if (playbackInfo != null && helper != null) {
                helper.initialize(playbackInfo);
            }

        }

        @Override
        public void play() {
            if (helper != null) {
                helper.play();
                progressBar.setVisibility(View.GONE);
                ivThumbnail.setVisibility(View.GONE);
            }
        }

        @Override
        public void pause() {
            if (helper != null) {
                helper.pause();
            }
        }

        @Override
        public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override
        public void release() {
            if (helper != null) {
                ivThumbnail.setVisibility(View.VISIBLE);
                try {
                    helper.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                helper = null;
            }
        }

        /**
         * the ratio value in range of 0.0 ~ 1.0 of the visible area.
         *
         * @return visible area of item to play video
         */
        @Override
        public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.50;

        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }

        @Override
        public void onContainerScrollStateChange(Container container, int newState) {

        }
    }
}
