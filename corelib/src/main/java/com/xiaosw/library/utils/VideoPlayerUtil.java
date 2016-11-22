package com.xiaosw.library.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.Map;

/**
 * 视频播放工具类（用户指南视频）
 * Created by xiaos on 2016/8/13 12:12:25.
 * mail: xiaosw@putao.com
 */
public class VideoPlayerUtil implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{

    public static final String TAG = "VideoPlayerUtil";

    /** 显示 */
    private SurfaceView mSurfaceView;
    /** 播放 */
    private MediaPlayer mMediaPlayer;
    private Activity mActivity;
    /** 记录暂停前是否已播放完毕 */
    private volatile boolean isPlayComplete;
    /** 视频URI */
    private Uri mVideoUri;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;

    public VideoPlayerUtil(Activity activity, SurfaceView surfaceView, String videoPath) {
        this(activity, surfaceView, Uri.parse(videoPath));
    }

    public VideoPlayerUtil(Activity activity, SurfaceView surfaceView, Uri videoUri) {
        this.mActivity = activity;
        this.mSurfaceView = surfaceView;
        this.mVideoUri = videoUri;
        initialize();
    }

    public void onResume() {
        if (null != mMediaPlayer && !isPlayComplete) {
            mMediaPlayer.start();
        }
    }

    public void onPause() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPlayComplete = false;
        }
    }

    private void initialize() {
        showSurfaceViewNeeded();
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openVideo();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void showSurfaceViewNeeded() {
        mSurfaceView.setVisibility(View.VISIBLE);
        mSurfaceView.requestLayout();
        mSurfaceView.invalidate();
        mSurfaceView.bringToFront();
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    public void setVideoURI(Uri uri, Map<String, String> headers) {
        mVideoUri = uri;
        mHeaders = headers;
        showSurfaceViewNeeded();
        openVideo();
    }

    private void openVideo() {
        if (mVideoUri == null || mSurfaceView.getHolder() == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release();

        AudioManager am = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        try {
            mMediaPlayer = new MediaPlayer();
            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mActivity, mVideoUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(mActivity, mVideoUri);
            }
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
        } catch (IOException ex) {
            mSurfaceView.setVisibility(View.GONE);
            LogUtil.e(TAG, "Unable to open content: " + mVideoUri, ex);
            return;
        } catch (IllegalArgumentException ex) {
            mSurfaceView.setVisibility(View.GONE);
            LogUtil.e(TAG, "Unable to open content: " + mVideoUri, ex);
            return;
        } finally {
            // do nothing
        }
    }

    /*
     * release the media player in any state
     */
    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            AudioManager am = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPlayComplete = true;
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        } else {
            mActivity.finish();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mActivity.finish();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPlayComplete = false;
        mMediaPlayer.start();
    }

    public boolean isPlayComplete() {
        return isPlayComplete;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }
}
