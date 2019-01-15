package com.autoplayvideo.interfaces;

import android.support.annotation.IntDef;

import com.autoplayvideo.Constants.FeedsConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Navjot Singh
 * on 14/1/19.
 */


@Retention(RetentionPolicy.SOURCE)
@IntDef({FeedsConstant.FEED_TYPE_IMAGE, FeedsConstant.FEED_TYPE_VIDEO})
public @interface MediaType {
}
