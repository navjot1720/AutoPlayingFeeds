package com.autoplayvideo;

import android.app.Application;
import android.content.Context;

import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

import im.ene.toro.exoplayer.Config;
import im.ene.toro.exoplayer.MediaSourceBuilder;
import im.ene.toro.exoplayer.ToroExo;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 */

@ReportsCrashes(formUri = "",
        mailTo = "navjot.singh@appinventiv.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.app_name,
        customReportContent = {
                ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL,
                ReportField.BRAND,
                ReportField.STACK_TRACE,
        })
public class FeedsApplication extends Application {

    private static FeedsApplication instance;

    private Config config;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ACRA.init(this);

        // Initializing toro3 lib
        toroInit();

    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }


    public static FeedsApplication getInstance() {
        return instance;
    }


    private void toroInit() {
        long cacheSize = 5 * 1024 * 1024;
        SimpleCache cache = new SimpleCache(new File(getFilesDir().getPath() + "/toro_cache"),
                new LeastRecentlyUsedCacheEvictor(cacheSize));
        config = new Config.Builder()
                .setMediaSourceBuilder(MediaSourceBuilder.LOOPING)
                .setCache(cache)
                .build();
        ToroExo.with(this).getCreator(config);
    }


    public Config getToroConfig() {
        return config;
    }

}
