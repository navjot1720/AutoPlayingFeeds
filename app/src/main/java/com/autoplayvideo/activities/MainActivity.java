package com.autoplayvideo.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.autoplayvideo.Constants.FeedsConstant;
import com.autoplayvideo.R;
import com.autoplayvideo.adapter.AutoFeedsAdapter;
import com.autoplayvideo.interfaces.FeedsClickListener;
import com.autoplayvideo.model.FeedsModel;
import com.autoplayvideo.model.PostMedium;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.widget.Container;

/**
 * Created by Navjot Singh
 * on 11/1/19.
 */

public class MainActivity extends AppCompatActivity implements FeedsClickListener {

    @BindView(R.id.rv_feeds)
    Container rvFeeds;

    private List<FeedsModel> mPostList;
    private AutoFeedsAdapter mFeedsAdapter;

    private ExoPlayerViewHelper helper;

    private boolean isLoading;
    private int pageNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVariables();
        setUpAdapter();

//        loadDataFromAPI(pageNo);
        createDummyFeedsList();

    }


    /**
     * method to initialize variables
     */
    private void initVariables() {
        mPostList = new ArrayList<>();
        pageNo = 1;
    }


    /**
     * method to setup Feeds Adapter
     */
    private void setUpAdapter() {
        mFeedsAdapter = new AutoFeedsAdapter(mPostList, this);

        rvFeeds.setLayoutManager(new LinearLayoutManager(this));
        rvFeeds.setAdapter(mFeedsAdapter);

        rvFeeds.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >= 0 && recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

//                    int visibleItems = layoutManager.getChildCount();
                    int totalItems = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    if (!isLoading && (lastVisibleItemPosition + 1) >= totalItems) {
                        pageNo++;
                        loadDataFromAPI(pageNo);
                    }

                }
            }
        });

    }


    /**
     * method to hit API to get data
     */
    private void loadDataFromAPI(int pageNo) {
        // TODO hit API to get data
    }


    @Override
    public void onNameClicked(int position) {
        // TODO handle click on name
        Toast.makeText(this, mPostList.get(position).getName() + " clicked!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setUpHelper(ExoPlayerViewHelper exoPlayerViewHelper, int position) {
        helper = exoPlayerViewHelper;
    }


    /**
     * method to play currently focused video
     */
    public void playCurrentlyVisibleVideo() {
        if (!isFinishing() && helper != null) {
            helper.play();
        }
    }


    /**
     * method to pause currently focused video
     */
    public void pauseCurrentlyVisibleVideo() {
        if (!isFinishing() && helper != null) {
            helper.pause();
        }
    }


    /**
     * method to create dummy Feeds List
     */
    private void createDummyFeedsList() {
        mPostList.clear();

        List<PostMedium> list;
        FeedsModel feedsModel;
        PostMedium postModel;

        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZjVLtJDgMTOExfMHsTZuT4G5cAmaRT0N0vnoVbblrTTKkwSOb");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZjVLtJDgMTOExfMHsTZuT4G5cAmaRT0N0vnoVbblrTTKkwSOb");
        list.add(postModel);

        feedsModel.setName("Aaa");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543125854.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543132164.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Bbb");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1545532616.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1545506170.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Ccc");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543132164.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Ddd");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1545506170.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNgzD5A4T08-ZM8ekUs0TNaduybB07IJQj04bVwoqoj7tw4-Nt1A");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNgzD5A4T08-ZM8ekUs0TNaduybB07IJQj04bVwoqoj7tw4-Nt1A");
        list.add(postModel);

        feedsModel.setName("Eee");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZjVLtJDgMTOExfMHsTZuT4G5cAmaRT0N0vnoVbblrTTKkwSOb");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZjVLtJDgMTOExfMHsTZuT4G5cAmaRT0N0vnoVbblrTTKkwSOb");
        list.add(postModel);

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543125854.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Fff");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        list.add(postModel);

        feedsModel.setName("Ggg");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz_FDTFF8pXh3a8ihVsqq51tUMyMQtY0MQdOwIlOnCDQ3VaWjp");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz_FDTFF8pXh3a8ihVsqq51tUMyMQtY0MQdOwIlOnCDQ3VaWjp");
        list.add(postModel);

        feedsModel.setName("Hhh");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543132164.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Iii");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1545506170.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Jjj");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1543125854.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Kkk");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNgzD5A4T08-ZM8ekUs0TNaduybB07IJQj04bVwoqoj7tw4-Nt1A");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNgzD5A4T08-ZM8ekUs0TNaduybB07IJQj04bVwoqoj7tw4-Nt1A");
        list.add(postModel);

        feedsModel.setName("Lll");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_VIDEO);
        postModel.setUrl("https://goodlifeprod.s3-accelerate.amazonaws.com/iOS/1545532616.mp4");
        postModel.setThumbUrl("");
        list.add(postModel);

        feedsModel.setName("Mmm");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        list.add(postModel);

        feedsModel.setName("Nnn");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);


        feedsModel = new FeedsModel();
        list = new ArrayList<>();

        postModel = new PostMedium();
        postModel.setPostType(FeedsConstant.FEED_TYPE_IMAGE);
        postModel.setUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        postModel.setThumbUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJr0K32JP0vdhzEuWLNrPY8KOMpy6sGphog-SbvQskl3STDmh4");
        list.add(postModel);

        feedsModel.setName("Rrr");
        feedsModel.setPostMedia(list);
        mPostList.add(feedsModel);

        mFeedsAdapter.notifyDataSetChanged();

    }

}
