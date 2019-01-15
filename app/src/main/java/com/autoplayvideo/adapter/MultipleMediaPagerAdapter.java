package com.autoplayvideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.autoplayvideo.Constants.FeedsConstant;
import com.autoplayvideo.R;
import com.autoplayvideo.interfaces.VolumeStateChangedListener;
import com.autoplayvideo.model.PostMedium;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 */

public class MultipleMediaPagerAdapter extends PagerAdapter {

    private final List<PostMedium> mediaList;
    private VolumeStateChangedListener listener;
    private Context context;
    private boolean isMute;

    MultipleMediaPagerAdapter(List<PostMedium> mediaList, boolean isMute, VolumeStateChangedListener listener) {
        this.mediaList = mediaList;
        this.listener = listener;
        this.isMute = isMute;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    public PostMedium getItem(int position) {
        return mediaList.get(position);
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull final ViewGroup container, final int position) {
        context = container.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_feed_items, container, false);

        RelativeLayout rlPage = itemView.findViewById(R.id.rl_pager);

        final ImageView ivImage = itemView.findViewById(R.id.iv_post_image);
        final PlayerView playerView = itemView.findViewById(R.id.player);

        final ImageView ivSound = itemView.findViewById(R.id.iv_sound);
        final ProgressBar pbLoader = itemView.findViewById(R.id.pb_loader);
        ImageView ivPlay = itemView.findViewById(R.id.iv_play);
        ImageView ivVideo = itemView.findViewById(R.id.iv_video);

        if (getItem(position).getPostType() == FeedsConstant.FEED_TYPE_IMAGE) {
            // for images
            ivPlay.setVisibility(View.GONE);
            ivSound.setVisibility(View.GONE);
            ivVideo.setVisibility(View.GONE);
            playerView.setVisibility(View.GONE);

            ivImage.setVisibility(View.VISIBLE);
            pbLoader.setVisibility(View.VISIBLE);

            String url = getItem(position).getUrl() != null ? getItem(position).getUrl() : "";
            Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivImage) {

                @Override
                protected void setResource(Bitmap resource) {
                    ivImage.setImageBitmap(resource);
                    pbLoader.setVisibility(View.GONE);
                    getItem(position).setMediaLoaded(true);
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    pbLoader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    pbLoader.setVisibility(View.GONE);
                }
            });

        } else {
            // for videos
            ivImage.setVisibility(View.GONE);
            ivPlay.setVisibility(View.GONE);

            ivVideo.setVisibility(View.VISIBLE);
            ivSound.setVisibility(View.VISIBLE);
            pbLoader.setVisibility(View.VISIBLE);

            ivSound.setImageResource(isMute ? R.drawable.ic_volume_off : R.drawable.ic_volume_on);

            if (playerView.getPlayer() != null && playerView.getPlayer() instanceof SimpleExoPlayer) {
                ((SimpleExoPlayer) playerView.getPlayer()).setVolume(isMute ? 0F : 1F);
            }

            rlPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerView.getPlayer() != null && playerView.getPlayer() instanceof SimpleExoPlayer) {
                        isMute = !isMute;

                        ((SimpleExoPlayer) playerView.getPlayer()).setVolume(isMute ? 0F : 1F);
                        ivSound.setImageResource(isMute ? R.drawable.ic_volume_off : R.drawable.ic_volume_on);

                        if (listener != null)
                            listener.onVolumeStateChanged(isMute);

                    }
                }
            });


           /* if (getItem(position).getThumbUrl() != null && getItem(position).getThumbUrl().trim().length() > 0) {
                Glide.with(context).load(getItem(position).getThumbUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivImage) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        ivImage.setImageBitmap(resource);
                        pbLoader.setVisibility(View.GONE);
                        getItem(position).setMediaLoaded(true);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        pbLoader.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        pbLoader.setVisibility(View.GONE);
                    }
                });
            }*/

        }

        // TODO necessary to set position as tag to itemView for easy access via viewPager
        itemView.setTag(position);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
