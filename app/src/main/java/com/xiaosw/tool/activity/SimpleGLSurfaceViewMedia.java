package com.xiaosw.tool.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;
import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.utils.OpenGLVideoPlayerUtil;
import com.xiaosw.library.utils.ScreenUtil;
import com.xiaosw.tool.R;
import com.xiaosw.tool.activity.fragment.SlideFragment;
import com.xiaosw.tool.adapter.SlideFragmentPageAdapter;
import com.xiaosw.tool.bean.MediaItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SimpleGLSurfaceViewMedia extends BaseAppCompatActivity implements ViewPager.PageTransformer,
    ViewPager.OnPageChangeListener, View.OnTouchListener {
    /** 自动播放间隔时间 */
    private static final int DEFAULT_AUTO_SWITCH_GAP = 3000;
    /** 切换过场动画时间 */
    private static final int DEFAULT_AUTO_SWITCH_DURATION = 1500;

    private static final String TAG = "SimpleGLSurfaceViewMedia";

    /** 根据时间删选幻灯片播放组 */
    public static final String KEY_DATA = "slide_date";

    private Handler mHandler;
    private ArrayList<MediaItem> mDatas;
    private SlideFragment mLastFragment;
    private OpenGLVideoPlayerUtil mOpenGLVideoPlayerUtil;
    private ViewPager mViewPager;
    private SlideFragmentPageAdapter mSlideFragmentPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ScreenUtil.setFullScreen(this);
        setContentView(R.layout.activity_glsurface_view_media);
        mDatas = new ArrayList<>();
        setupView();
        mHandler = new Handler();
        mHandler.postDelayed(mUpdateTask, DEFAULT_AUTO_SWITCH_GAP);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (null != mOpenGLVideoPlayerUtil) {
            mOpenGLVideoPlayerUtil.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mOpenGLVideoPlayerUtil) {
            mOpenGLVideoPlayerUtil.onPause();
        }
    }


    @Override
    public void onPageSelected(int position) {
        SlideFragment fragment = mSlideFragmentPageAdapter.getItem(position);
        MediaItem infoSub = fragment.getMediaInfoSub();
        if (infoSub.getMimeType().startsWith("video")) {
            play(infoSub.getData(), fragment.getGLSurfaceView());
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (null != mLastFragment
                && mLastFragment.getGLSurfaceView() != null) {
                mLastFragment.getGLSurfaceView().setVisibility(View.GONE);
                mLastFragment = null;
            }
            mLastFragment = mSlideFragmentPageAdapter.getItem(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        float MIN_SCALE = 0.75f;
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            // view.setAlpha(0);
            ViewHelper.setAlpha(view, 0);
        } else if (position <= 0) {// a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            // [-1,0]
            // Use the default slide transition when moving to the left page
            // view.setAlpha(1);
            ViewHelper.setAlpha(view, 1);
            // view.setTranslationX(0);
            ViewHelper.setTranslationX(view, 0);
            // view.setScaleX(1);
            ViewHelper.setScaleX(view, 1);
            // view.setScaleY(1);
            ViewHelper.setScaleY(view, 1);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            // view.setAlpha(1 - position);
            ViewHelper.setAlpha(view, 1 - position);
            // Counteract the default slide transition
            // view.setTranslationX(pageWidth * -position);
            ViewHelper.setTranslationX(view, pageWidth * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - position);
            // view.setScaleX(scaleFactor);
            ViewHelper.setScaleX(view, scaleFactor);
            // view.setScaleY(1);
            ViewHelper.setScaleY(view, scaleFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            // view.setAlpha(0);
            ViewHelper.setAlpha(view, 1);
        }
    }

    private void setupView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
//        mViewPager.setAdapter(new PlaySlidePagerAdapter(mDatas, this));
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        }, null, null, "datetaken desc, _id desc");
        if (null != cursor) {
            while (cursor.moveToNext()) {
                mDatas.add(parseMediaItemByCursor(cursor));
            }
            cursor.close();
        }

        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        }, null, null, "datetaken desc, _id desc");
        if (null != cursor) {
            while (cursor.moveToNext()) {
                mDatas.add(parseMediaItemByCursor(cursor));
            }
            cursor.close();
        }
        Collections.sort(mDatas, new Comparator<MediaItem>() {
            @Override
            public int compare(MediaItem lhs, MediaItem rhs) {
                return lhs.getDatetaken() > rhs.getDatetaken() ? -1 : (lhs == rhs ? 0 : 1);
            }
        });
        mSlideFragmentPageAdapter = new SlideFragmentPageAdapter(getSupportFragmentManager(), initFragments());
        mViewPager.setAdapter(mSlideFragmentPageAdapter);
//        mViewPager.setPageTransformer(true, this);
        mViewPager.addOnPageChangeListener(this);
        ViewPagerScroll viewPagerScroll = new ViewPagerScroll(this);
        viewPagerScroll.initViewPagerScroll(mViewPager);
        mViewPager.setOnTouchListener(this);
    }

    private MediaItem parseMediaItemByCursor(Cursor cursor) {
        MediaItem mediaItem = new MediaItem();
        mediaItem.setData(cursor.getString(1));
        mediaItem.setMimeType(cursor.getString(2));
        mediaItem.setWidth(cursor.getInt(3));
        mediaItem.setHeight(cursor.getInt(4));
        mediaItem.setDatetaken(cursor.getLong(5));
        LogUtil.i(TAG, "path = " + mediaItem.getData());
        return mediaItem;
    }

    private ArrayList<SlideFragment> initFragments() {
        ArrayList<SlideFragment> fragments = new ArrayList<>();
        int i = 0;
        for (MediaItem infoSub : mDatas) {
            fragments.add(SlideFragment.newInstance(infoSub, i));
            i++;
        }
        return fragments;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        finish();
        return true;
    }

    public void play(String vidoePath, final GLSurfaceView glSurfaceView) {
        LogUtil.i(TAG, "play glSurfaceView = " + glSurfaceView);
        glSurfaceView.setOnTouchListener(this);
        mHandler.removeCallbacks(mUpdateTask);
        mOpenGLVideoPlayerUtil = new OpenGLVideoPlayerUtil(this, glSurfaceView);
        mOpenGLVideoPlayerUtil.setVideoPath(vidoePath);
        mOpenGLVideoPlayerUtil.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                glSurfaceView.setVisibility(View.GONE);
                mHandler.post(mUpdateTask);
            }
        });
    }

    /**
     * 自动播放task
     */
    private Runnable mUpdateTask = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(this);
            if (null != mOpenGLVideoPlayerUtil
                && !mOpenGLVideoPlayerUtil.isPlayComplete()) {
                return;
            }
            if (mViewPager.getCurrentItem() < mDatas.size() - 1) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            } else {
                finish();
            }
            mHandler.postDelayed(mUpdateTask, DEFAULT_AUTO_SWITCH_GAP);
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mUpdateTask);
        ScreenUtil.cancelFullScreen(this);
        super.onDestroy();
    }

    /**
     * 控制ViewPager切换速度
     */
    @TargetApi(11)
    private class ViewPagerScroll extends Scroller {
        private int mDuration = DEFAULT_AUTO_SWITCH_DURATION;

        public void setDuration(int duration) {
            mDuration = duration;
        }

        public ViewPagerScroll(Context context) {
            this(context, null);
        }

        public ViewPagerScroll(Context context, Interpolator interpolator) {
            this(context, interpolator,
                context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB);
        }

        public ViewPagerScroll(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
