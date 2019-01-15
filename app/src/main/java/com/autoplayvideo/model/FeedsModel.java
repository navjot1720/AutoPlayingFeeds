package com.autoplayvideo.model;

import java.util.List;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 * This class depict the structure of model to be used for the List of Feeds with multiple posts
 */

public class FeedsModel {

    private String id;
    private String Name;
    private int currentPagePosition;
    private List<PostMedium> postMedia;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCurrentPagePosition() {
        return currentPagePosition;
    }

    public void setCurrentPagePosition(int currentPagePosition) {
        this.currentPagePosition = currentPagePosition;
    }

    public List<PostMedium> getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(List<PostMedium> postMedia) {
        this.postMedia = postMedia;
    }

}
