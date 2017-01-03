package com.xiaosw.library.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaosw.library.R;
import com.xiaosw.library.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * <p><br/>ClassName : {@link LoadingDialog}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-01-03 14:14:57</p>
 */
public class LoadingDialog extends BaseAlertDialog {

    /**
     * @see LoadingDialog#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-LoadingDialog";
    private static final int REFRESH_DURATION = 500;
    private static final String MSG_MAX_SUFFIX = "...";

    private Handler mHandler;
    private Animation mRotateAnim;
    private View view_loading_root;
    private View null_span;
    /** 加载logo */
    private ImageView iv_loading;
    /** 提示信息 */
    private TextView tv_msg;
    private String mMsg;
    /** 是否启用自动刷新(文字后的小点) */
    private boolean mEnableRefresh = true;

    protected LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    protected LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        view_loading_root = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        iv_loading = ButterKnife.findById(view_loading_root, R.id.iv_loading);
        null_span = ButterKnife.findById(view_loading_root, R.id.null_span);
        tv_msg = ButterKnife.findById(view_loading_root, R.id.tv_msg);
        mMsg = tv_msg.getText().toString();
        mHandler = new Handler(Looper.myLooper());
        mRotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        setView(view_loading_root);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            View rootView = getWindow().findViewById(R.id.view_dialog_content_panel);
            if (null != rootView) {
                Drawable drawable = iv_loading.getDrawable();
                CharSequence text = tv_msg.getText() + MSG_MAX_SUFFIX;
                float drawableIntrinsicWidth = 0;
                float textWidth = 0;
                if (null != drawable) {
                    drawableIntrinsicWidth = drawable.getIntrinsicWidth()
                                                    + iv_loading.getPaddingRight()
                                                    + iv_loading.getPaddingLeft();
                }
                if (!TextUtils.isEmpty(text)) {
                    textWidth = tv_msg.getPaint().measureText(text.toString())
                                                    + iv_loading.getPaddingRight()
                                                    + iv_loading.getPaddingLeft();
                }
                rootView.setMinimumWidth((int) (Math.max(drawableIntrinsicWidth, textWidth)
                    + view_loading_root.getPaddingLeft() + view_loading_root.getPaddingRight()+ 0.5f ));
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "onCreate()", e);
        }
    }

    @Override
    public void show() {
        super.show();
        if (null != iv_loading.getDrawable()) {
            iv_loading.startAnimation(mRotateAnim);
        }
        if (mEnableRefresh && !TextUtils.isEmpty(mMsg)) {
            mHandler.postDelayed(mUpdateText, REFRESH_DURATION);
        }
    }

    @Override
    public void dismiss() {
        mHandler.removeCallbacks(mUpdateText);
        super.dismiss();
    }

    StringBuffer sb = new StringBuffer();
    private Runnable mUpdateText = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mUpdateText);
            if (isShowing()) {
                if (sb.length() >= MSG_MAX_SUFFIX.length()) {
                    sb.delete(0, sb.length());
                } else {
                    sb.append(".");
                }
                tv_msg.setText(mMsg + sb.toString());
                mHandler.postDelayed(mUpdateText, REFRESH_DURATION);
            }
        }
    };

    public void setLoadingIcon(Drawable loadingIcon) {
        iv_loading.setImageDrawable(loadingIcon);
        if (null == loadingIcon) {
            iv_loading.setVisibility(View.GONE);
            null_span.setVisibility(View.GONE);
            return;
        } else if (iv_loading.getVisibility() != View.VISIBLE) {
            iv_loading.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(mMsg)) {
            null_span.setVisibility(View.GONE);
        } else {
            null_span.setVisibility(View.VISIBLE);
        }

    }

    public void setLoadingIcon(int resId) {
        iv_loading.setImageResource(resId);
    }

    public void setLoadingMsg(CharSequence msg) {
        tv_msg.setText(msg);
        mMsg = tv_msg.getText().toString();
        if (TextUtils.isEmpty(mMsg)) {
            tv_msg.setVisibility(View.GONE);
            null_span.setVisibility(View.GONE);
            return;
        } else if (tv_msg.getVisibility() != View.VISIBLE) {
            tv_msg.setVisibility(View.VISIBLE);
        }
        if (iv_loading.getDrawable() == null) {
            null_span.setVisibility(View.GONE);
        } else {
            null_span.setVisibility(View.VISIBLE);
        }
    }

    public void setLoadingMsg(int resId) {
        setLoadingMsg(getContext().getResources().getString(resId));
    }

    public boolean isEnableRefresh() {
        return mEnableRefresh;
    }

    public void setEnableRefresh(boolean enableRefresh) {
        mEnableRefresh = enableRefresh;
    }

    public static class Builder extends BaseAlertDialog.Builder<LoadingDialog> {
        private LoadingDialog mLoadingDialog;

        public Builder(Context context) {
            super(context);
        }

        public Builder(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        LoadingDialog generateDialog(Context context) {
            mLoadingDialog = new LoadingDialog(context);
            return mLoadingDialog;
        }

        public void setLoadingIcon(Drawable loadingIcon) {
            mLoadingDialog.setLoadingIcon(loadingIcon);
        }

        public void setLoadingIcon(int resId) {
            mLoadingDialog.setLoadingIcon(resId);
        }

        public void setLoadingMsg(CharSequence msg) {
            mLoadingDialog.setLoadingMsg(msg);
        }

        public void setLoadingMsg(int resId) {
            mLoadingDialog.setLoadingMsg(resId);
        }

        public void setEnableRefresh(boolean enableRefresh) {
            mLoadingDialog.setEnableRefresh(enableRefresh);
        }
    }

}
