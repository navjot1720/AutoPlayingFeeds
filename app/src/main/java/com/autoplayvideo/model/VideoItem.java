package com.autoplayvideo.model;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Dev Sharma on 2/10/17.
 * Model class for video items
 */

public class VideoItem {

    private String videoUrl;
    private String imageUrl;
    private String name;


    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public VideoItem(String videoUrl, String imageUrl, String name)
    {
        this.videoUrl=videoUrl;
        this.imageUrl=imageUrl;
        this.name=name;
    }
}
