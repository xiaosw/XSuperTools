package com.xiaosw.tool.activity.fragment;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaosw.tool.R;
import com.xiaosw.tool.activity.SimpleGLSurfaceViewMedia;
import com.xiaosw.tool.bean.MediaItem;

/**
 * <p><br/>ClassName : {@link SlideFragment}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-04 19:19:23</p>
 */
public class SlideFragment extends Fragment {

    private MediaItem mMediaInfoSub;

    private GLSurfaceView mSurfaceView;
    private boolean isResumePlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slide, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv_slide = (ImageView) view.findViewById(R.id.iv_slide);
        if (mMediaInfoSub.getMimeType().startsWith("video")) {
            mSurfaceView = (GLSurfaceView) view.findViewById(R.id.gl_surface_view);

        } else {
            Glide.with(getContext()).load(mMediaInfoSub.getData()).into(iv_slide);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumePlay) {
            ((SimpleGLSurfaceViewMedia) getActivity()).play(mMediaInfoSub.getData(), getGLSurfaceView());
        }
    }

    /**
     * 获取Fragment
     * @param infoSub 页面数据
     * @param position 页面位置
     * @return
     */
    public static SlideFragment newInstance(MediaItem infoSub, int position) {
        SlideFragment fragment = new SlideFragment();
        fragment.setMediaInfoSub(infoSub);
        if (position == 0 && infoSub.getMimeType().startsWith("video")) {
            fragment.isResumePlay = true;
        }
        return fragment;
    }

    public MediaItem getMediaInfoSub() {
        return mMediaInfoSub;
    }

    public void setMediaInfoSub(MediaItem mediaInfoSub) {
        mMediaInfoSub = mediaInfoSub;
    }

    public GLSurfaceView getGLSurfaceView() {
        return mSurfaceView;
    }

}
