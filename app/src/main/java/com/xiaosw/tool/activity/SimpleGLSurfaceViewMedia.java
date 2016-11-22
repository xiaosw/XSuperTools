package com.xiaosw.tool.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.OpenGLVideoPlayerUtil;

public class SimpleGLSurfaceViewMedia extends BaseAppCompatActivity {

    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-SimpleGLSurfaceViewMedia";

    private GLSurfaceView mGLSurfaceView;
    private OpenGLVideoPlayerUtil mOpenGLVideoPlayerUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle("SimpleGLSurfaceViewMedia");
        setupView();
        mOpenGLVideoPlayerUtil = new OpenGLVideoPlayerUtil(this, mGLSurfaceView);
        String videoPath = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/VID_20161122_142017665.mp4";
        mOpenGLVideoPlayerUtil.setVideoPath(videoPath);
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

    private void setupView() {
        mGLSurfaceView = new GLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }

}
