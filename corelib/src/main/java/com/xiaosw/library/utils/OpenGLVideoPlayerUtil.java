package com.xiaosw.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import com.xiaosw.library.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 视频播放工具类（用户指南视频）
 * Created by xiaos on 2016/8/13 12:12:25.
 * mail: xiaosw@putao.com
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class OpenGLVideoPlayerUtil implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, GLSurfaceView.Renderer
        , SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnVideoSizeChangedListener {

    public static final String TAG = "OpenGLVideoPlayerUtil";

    private GLSurfaceView mGLSurfaceView;
    /** 显示 */
    private SurfaceTexture mSurfaceTexture;
    /** 播放 */
    private MediaPlayer mMediaPlayer;
    private Activity mActivity;
    /** 记录暂停前是否已播放完毕 */
    private volatile boolean isPlayComplete;
    /** 视频URI */
    private Uri mVideoUri;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;

    /** @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(GL10, int, int)  */
    private int mGLSurfaceViewWidth;
    /** @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(GL10, int, int)  */
    private int mGLSurfaceViewHeight;
    /** 是否正在绘制窗口 */
    private volatile boolean mFrameAvailable;
    int mTextureParamHandle;
    int mTextureCoordinateHandle;
    int mPositionHandle;
    int mTextureTranformHandle;
    private static float squareSize = 1.0f;
    private static float sSquareCoords[] = {
        -squareSize, squareSize,   // top left
        -squareSize, -squareSize,   // bottom left
        squareSize, -squareSize,    // bottom right
        squareSize, squareSize}; // top right
    private static short mDrawOrder[] = {0, 1, 2, 0, 2, 3};

    // Texture to be shown in backgrund
    private FloatBuffer mTextureBuffer;
    private float mTextureCoords[] = {
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f};
    private int[] mTextures = new int[1];

    private int shaderProgram;
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;
    private float[] videoTextureTransform = new float[16];

    public OpenGLVideoPlayerUtil(Activity activity, GLSurfaceView glSurfaceView) {
        this(activity, glSurfaceView, "");
    }

    public OpenGLVideoPlayerUtil(Activity activity, GLSurfaceView glSurfaceView, String videoPath) {
        this(activity, glSurfaceView, Uri.parse(videoPath));
    }

    public OpenGLVideoPlayerUtil(Activity activity, GLSurfaceView glSurfaceView, Uri videoUri) {
        this.mActivity = activity;
        this.mGLSurfaceView = glSurfaceView;
        this.mVideoUri = videoUri;
//        initialize();
        initGLSurfaceView();
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
    }

    private void showSurfaceViewNeeded() {
        mGLSurfaceView.setVisibility(View.VISIBLE);
        mGLSurfaceView.requestLayout();
        mGLSurfaceView.invalidate();
        mGLSurfaceView.bringToFront();
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
        LogUtil.i(TAG, "openVideo = " + mVideoUri);
        if (mVideoUri == null || mSurfaceTexture == null) {
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
            Surface surface = new Surface(mSurfaceTexture);
            mMediaPlayer.setSurface(surface);
            surface.release();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mActivity, mVideoUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(mActivity, mVideoUri);
            }
            mMediaPlayer.prepareAsync();
        } catch (IOException ex) {
            hideView();
            LogUtil.e(TAG, "Unable to open content: " + mVideoUri, ex);
            return;
        } catch (IllegalArgumentException ex) {
            hideView();
            LogUtil.e(TAG, "Unable to open content: " + mVideoUri, ex);
            return;
        } finally {
            // do nothing
        }
    }

    private void hideView() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGLSurfaceView.setVisibility(View.GONE);
            }
        });
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

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    public boolean isPlayComplete() {
        return isPlayComplete;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    ///////////////////////////////////////////////////////////////////////////
    // GLSurfaceView相关
    ///////////////////////////////////////////////////////////////////////////

    private void initGLSurfaceView() {
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.i(TAG, "------------------------------> onSurfaceCreated()");
        setupGraphics();
        setupVertexBuffer();
        setupTexture();
        if (null != mVideoUri && !TextUtils.isEmpty(mVideoUri.getPath())) {
            openVideo();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mGLSurfaceViewWidth = width;
        mGLSurfaceViewHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (mFrameAvailable) {
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(videoTextureTransform);
                mFrameAvailable = false;
            }
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0, 0, mGLSurfaceViewWidth, mGLSurfaceViewHeight);
        drawTexture();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            mFrameAvailable = true;
        }
    }

    private void setupGraphics() {
        String vertexShader = AppContextUtil.readTextFileFromRawResource(R.raw.vetext_sharder);
        String fragmentShader = AppContextUtil.readTextFileFromRawResource(R.raw.fragment_sharder);

        int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        shaderProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
            new String[]{"texture", "vPosition", "vTexCoordinate", "textureTransform"});

        GLES20.glUseProgram(shaderProgram);
        mTextureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        mTextureTranformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
    }

    private void setupVertexBuffer() {
        // Draw list buffer
        ByteBuffer dlb = ByteBuffer.allocateDirect(mDrawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(mDrawOrder);
        mDrawListBuffer.position(0);

        // Initialize the texture holder
        ByteBuffer bb = ByteBuffer.allocateDirect(sSquareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(sSquareCoords);
        mVertexBuffer.position(0);
    }

    private void setupTexture() {
        ByteBuffer texturebb = ByteBuffer.allocateDirect(mTextureCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());

        mTextureBuffer = texturebb.asFloatBuffer();
        mTextureBuffer.put(mTextureCoords);
        mTextureBuffer.position(0);

        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, mTextures, 0);
        checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextures[0]);
        checkGlError("Texture bind");

        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
    }

    private void drawTexture() {
        // Draw texture

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextures[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mTextureParamHandle, 0);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, mTextureBuffer);

        GLES20.glUniformMatrix4fv(mTextureTranformHandle, 1, false, videoTextureTransform, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, mDrawOrder.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }
}
