package com.xiaosw.library.widget.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xiaosw.library.R;
import com.xiaosw.library.controll.AlertControll;
import com.xiaosw.library.controll.AlertWindowControll;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.utils.ScreenUtil;

/**
 * @ClassName : {@link BaseAlertDialog}
 * @Description : 弹框基类
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-23 10:10:52
 */
public class BaseAlertDialog extends AlertDialog {

    private static final String TAG = "BaseAlertDialog";

    private AlertControll mAlertControll;

    protected BaseAlertDialog(Context context) {
        this(context, R.style.alert_dialog);
    }

    protected BaseAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        initialize();
    }

    protected BaseAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlertControll.installContent();
        LogUtil.e(TAG, TAG + " onCreate()");
    }

    void initialize() {
        mAlertControll = new AlertControll(getContext(), this, getWindow());
    }

    WindowManager.LayoutParams initWindowParams() {
        return initWindowParams((ScreenUtil.getScreenWH(getContext())[0] * 3 / 4), -1);
    }

    WindowManager.LayoutParams initWindowParams(int width, int height) {
        // 设置dialog样式
        Window window = getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.CENTER;
        if (width > 0) {
            windowParams.width = width;
        }
        if (height > 0) {
            windowParams.height = height;
        }
        return windowParams;
    }

    @Override
    public void create() {
        super.create();
        LogUtil.e(TAG, TAG + " create()");
    }

    @Override
    public void show() {
        super.show();
        AlertWindowControll.INSTANCE.show(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AlertWindowControll.INSTANCE.dissmiss(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        mAlertControll.setTitle(title);
    }

    @Override
    public void setMessage(CharSequence message) {
        mAlertControll.setMessage(message);
    }

    @Override
    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mAlertControll.setButton(whichButton, text, listener, null);
    }

    @Override
    public void setButton(int whichButton, CharSequence text, Message msg) {
        mAlertControll.setButton(whichButton, text, null, msg);
    }

    @Override
    public void setButton(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_POSITIVE, text, listener);
    }

    @Override
    public void setButton(CharSequence text, Message msg) {
        setButton(BUTTON_POSITIVE, text, msg);
    }

    @Override
    public void setButton2(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_POSITIVE, text, listener);
    }

    @Override
    public void setButton2(CharSequence text, Message msg) {
        setButton(BUTTON_POSITIVE, text, msg);
    }

    @Override
    public void setButton3(CharSequence text, OnClickListener listener) {
        setButton(BUTTON_POSITIVE, text, listener);
    }

    @Override
    public void setButton3(CharSequence text, Message msg) {
        setButton(BUTTON_POSITIVE, text, msg);
    }

    /**
     * Dialog圆角大小
     * @param dp
     */
    public void setRadius(float dp) {
        mAlertControll.setRadius(dp);
    }

    public void setPositiveButtonBackgroudDrawable(Drawable drawable) {
        mAlertControll.setPositiveButtonBackgroudDrawable(drawable);
    }

    public void setPositiveButtonBackgroudDrawable(int drawableId) {
        mAlertControll.setPositiveButtonBackgroudDrawable(drawableId);
    }

    public void setNegativeButtonBackgroudDrawable(Drawable drawable) {
        mAlertControll.setNegativeButtonBackgroudDrawable(drawable);
    }

    public void setNegativeButtonBackgroudDrawable(int drawableId) {
        mAlertControll.setPositiveButtonBackgroudDrawable(drawableId);
    }

    public void setNeutralButtonBackgroudDrawable(Drawable drawable) {
        mAlertControll.setNeutralButtonBackgroudDrawable(drawable);
    }

    public void setNeutralButtonBackgroudDrawable(int drawableId) {
        mAlertControll.setPositiveButtonBackgroudDrawable(drawableId);
    }

    public static class Builder extends AlertDialog.Builder {

        private final AlertControll.AlertParams P;
        private BaseAlertDialog mBaseAlertDialog;

        public Builder(Context context) {
            super(context);
            P = new AlertControll.AlertParams(context);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public Builder(Context context, int themeResId) {
            super(context, themeResId);
            P = new AlertControll.AlertParams(context);
        }

        @Override
        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        @Override
        public Builder setTitle(int titleId) {
            return setTitle(P.mContext.getString(titleId));
        }

        @Override
        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        @Override
        public Builder setMessage(int messageId) {
            return setMessage(P.mContext.getString(messageId));
        }

        @Override
        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiviButtonListener = listener;
            return this;
        }

        @Override
        public Builder setPositiveButton(int textId, OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getString(textId);
            P.mPositiviButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNegativeButton(int textId, OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNeutralButton(int textId, OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        @Override
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        @Override
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        @Override
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * 设置Dialog圆角大小
         * @param dp
         * @return
         */
        public Builder setRadius(float dp) {
            P.mRadiusSize = Math.max(0, dp);
            return this;
        }

        public Builder setPositiveButtonBackgroudDrawable(Drawable drawable) {
            P.mPositiveButtonBackgrounDrawable = drawable;
            return this;
        }

        public Builder setPositiveButtonBackgroudDrawable(int drawableId) {
            return setPositiveButtonBackgroudDrawable(P.mContext.getResources().getDrawable(drawableId));
        }

        public Builder setNegativeButtonBackgroudDrawable(Drawable drawable) {
            P.mNegativeButtonBackgrounDrawable = drawable;
            return this;
        }

        public Builder setNegativeButtonBackgroudDrawable(int drawableId) {
            return setNegativeButtonBackgroudDrawable(P.mContext.getResources().getDrawable(drawableId));
        }

        public Builder setNeutralButtonBackgroudDrawable(Drawable drawable) {
            P.mNeutralButtonBackgrounDrawable = drawable;
            return this;
        }

        public Builder setNeutralButtonBackgroudDrawable(int drawableId) {
            return setNegativeButtonBackgroudDrawable(P.mContext.getResources().getDrawable(drawableId));
        }

        @Override
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        @Override
        public BaseAlertDialog create() {
            mBaseAlertDialog = new BaseAlertDialog(P.mContext);
            P.apply(mBaseAlertDialog.mAlertControll);
            mBaseAlertDialog.setCancelable(P.mCancelable);
            mBaseAlertDialog.setOnCancelListener(P.mOnCancelListener);
            mBaseAlertDialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                mBaseAlertDialog.setOnKeyListener(P.mOnKeyListener);
            }
            return mBaseAlertDialog;
        }

        @Override
        public BaseAlertDialog show() {
            create();
            mBaseAlertDialog.show();
            return mBaseAlertDialog;
        }

    }
}