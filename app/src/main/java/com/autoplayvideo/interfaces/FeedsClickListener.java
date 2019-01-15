package com.autoplayvideo.interfaces;

import im.ene.toro.exoplayer.ExoPlayerViewHelper;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 */

public interface FeedsClickListener {

    void setUpHelper(ExoPlayerViewHelper helper, int position);

    void onNameClicked(int position);

}
