
package com.autoplayvideo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 * This class depict the structure of model to be used for the List of Posts
 */

public class PostMedium implements Parcelable {

    private int postMediaId;

    private int width;

    private int height;

    private int postType;

    private String url;

    private String thumbUrl;

    private String createdDate;

    public PostMedium() {
    }

    protected PostMedium(Parcel in) {
        postMediaId = in.readInt();
        width = in.readInt();
        height = in.readInt();
        postType = in.readInt();
        url = in.readString();
        thumbUrl = in.readString();
        createdDate = in.readString();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isMediaLoaded;

    private boolean isSelected = true;

    public boolean isMediaLoaded() {
        return isMediaLoaded;
    }

    public void setMediaLoaded(boolean mediaLoaded) {
        isMediaLoaded = mediaLoaded;
    }


    public static final Creator<PostMedium> CREATOR = new Creator<PostMedium>() {
        @Override
        public PostMedium createFromParcel(Parcel in) {
            return new PostMedium(in);
        }

        @Override
        public PostMedium[] newArray(int size) {
            return new PostMedium[size];
        }
    };

    public int getPostMediaId() {
        return postMediaId;
    }

    public void setPostMediaId(int postMediaId) {
        this.postMediaId = postMediaId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postMediaId);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(postType);
        dest.writeString(url);
        dest.writeString(thumbUrl);
        dest.writeString(createdDate);
    }

}
