package com.autoplayvideo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.autoplayvideo.R;
import com.autoplayvideo.adapter.VideoAdapter;
import com.autoplayvideo.model.VideoItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

/**
 * Created by Dev Sharma
 * This is MainActivity of Application and show item in recyclerview
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container_autoPlay)
    Container containerAutoPlay;
    private ArrayList<VideoItem> videoItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prepareVideoData();

    }


    /**
     * Add data to array list
     */
    private void prepareVideoData() {
        videoItemArrayList = new ArrayList<>();
        videoItemArrayList.add(new VideoItem("http://www.betcoingaming.com/webdesigns/animatedslider/images/liveroulette2.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "Video1"));
        videoItemArrayList.add(new VideoItem("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "Video2"));
        videoItemArrayList.add(new VideoItem("", "http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg", "Image1"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1491561340/hello_cuwgcb.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1491561340/hello_cuwgcb.jpg", "Video3"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795675/3_yqeudi.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/3_yqeudi.jpg", "Video4"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795675/1_pyn1fm.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/1_pyn1fm.jpg", "Video5"));
        videoItemArrayList.add(new VideoItem("", "http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg", "Image2"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1491561340/hello_cuwgcb.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1491561340/hello_cuwgcb.jpg", "Video6"));
        videoItemArrayList.add(new VideoItem("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "Video7"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795676/4_nvnzry.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795676/4_nvnzry.jpg", "Video8"));
        videoItemArrayList.add(new VideoItem("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "Video9"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795675/3_yqeudi.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/3_yqeudi.jpg", "Video10"));
        videoItemArrayList.add(new VideoItem("", "http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/2_qwpgis.jpg", "Image3"));
        videoItemArrayList.add(new VideoItem("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795675/1_pyn1fm.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/1_pyn1fm.jpg", "Video11"));
        videoItemArrayList.add(new VideoItem("", "http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/3_lfndfq.jpg", "Image4"));
        videoItemArrayList.add(new VideoItem("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "Video12"));
        videoItemArrayList.add(new VideoItem("", "http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg", "Image5"));
        setAdapter();
    }

    /**
     * method to set adapter in recycler view
     */
    private void setAdapter() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        containerAutoPlay.setLayoutManager(mLinearLayoutManager);
        VideoAdapter videoAdapter = new VideoAdapter(videoItemArrayList, this);
        containerAutoPlay.setAdapter(videoAdapter);
    }

}
