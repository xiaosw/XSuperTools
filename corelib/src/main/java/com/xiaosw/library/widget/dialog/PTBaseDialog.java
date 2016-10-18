package com.xiaosw.library.widget.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.library.R;
import com.xiaosw.library.controll.PTAlertController;

/**
 * @ClassName : {@link PTBaseDialog}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-18 10:10:27
 */
public class PTBaseDialog extends AlertDialog {

    /**
     * {@link PTAlertController}
     */
    private PTAlertController mAlertController;

    public PTBaseDialog(Context context) {
        this(context, R.style.alert_dialog);
    }

    public PTBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public PTBaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        mAlertController = new PTAlertController(this, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlertController.installContent();
    }

    public static class Builder extends AlertDialog.Builder {

        private final PTAlertController.AlertParams P;
        /** custom view */
        private ViewGroup mViewGroup;

        public Builder(Context context) {
            super(context);
            P = new PTAlertController.AlertParams(context);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public Builder(Context context, int themeResId) {
            super(context, themeResId);
            P = new PTAlertController.AlertParams(context);
        }

        @Override
        public AlertDialog.Builder setView(int layoutResId) {
            return super.setView(layoutResId);
        }

        @Override
        public AlertDialog.Builder setView(View view) {
            return super.setView(view);
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
            P.mPositiveButtonListener = listener;
            return this;
        }

        @Override
        public Builder setPositiveButton(int textId, OnClickListener listener) {
            return setPositiveButton(P.mContext.getString(textId), listener);
        }

        @Override
        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNegativeButton(int textId, OnClickListener listener) {
            return setNegativeButton(P.mContext.getString(textId), listener);
        }

        @Override
        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        @Override
        public Builder setNeutralButton(int textId, OnClickListener listener) {
            return setNeutralButton(P.mContext.getString(textId), listener);
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

        @Override
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        @Override
        public Builder setCustomTitle(View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return this;
        }

        @Override
        public PTBaseDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final PTBaseDialog dialog = new PTBaseDialog(P.mContext);
            P.apply(dialog.mAlertController);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }
    }

}
