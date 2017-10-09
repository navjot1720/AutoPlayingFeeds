AutoPlay Video

This library works along with RecyclerView to provide the auto play feature. You can use either MediaPlayer or ExoPlayer in your RecyclerView row item.

Getting Started-

1. Add toro-core as gradle project in your application.

    (i) Right click on app.
    (ii) Click on Open Module Settings
    (iii) Click on plus icon on left corner
    (iv) Select gradle project and browse toro-core.
    (v) toro-core is now integrated in your project.

2. Write in you application gradle-

    compile project(path: ':toro-core')

3. Using Container in place of RecyclerView in your xml.

    <im.ene.toro.widget.Container
      android:id="@+id/my_fancy_videos"
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>

4. Implement ToroPlayer in your ViewHolder of your VideoItem.

5. Implement following methods-
      (i)   getPlayerView() - Returns instance of SimpleExoplayerView
      (ii)  getCurrentPlaybackInfo() - Returns playback info of current player.
      (iii) initialize(Container container,PlaybackInfo playbackInfo) - Initialize player info with container.
      (iv)  play() - Play video.
      (v)   pause() - Pause video.
      (vi)  isPlaying()- Returns true if video is playing.
      (vii) release() - Release player.
      (viii) wantsToPlay()- Returns the offset of visible area of playing video.
      (ix)  getPlayerOrder()- order of playing videos in list.
      (x)   onContainerScrollStateChange(Container container, int newState)- calls when scrolling state changed.

  class ViewHolderVideo extends RecyclerView.ViewHolder implements ToroPlayer {

        @BindView(R.id.simpleexoplayer)
        SimpleExoPlayerView simpleexoplayer;

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

5. You can use any of the following helper according to the requirements-

        (i) LoopingExoPlayerViewHelper- This is used for looping video with ExoPlayer.
        (ii) ExoPlayerHelper- This is used for playing videos for one time with ExoPlayer.
        (iii) ToroPlayerHelper - This is used for playing videos with MediaPlayer.
