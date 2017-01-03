package com.xiaosw.library.controll;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaosw.library.R;

import java.lang.ref.WeakReference;

/**
 * @ClassName : {@link AlertControll}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-18 16:16:14
 */
public class AlertControll {

    /** @see AlertControll#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-AlertControll";

    /** 圆角大小 */
    private static final int DEFAULT_RADIUS_SIZE_DP = 8;

    private final Context mContext;
    private final DialogInterface mDialogInterface;
    private final Window mWindow;
    private Handler mHandler;

    private CardView mDialogPanel;

    /** 标题 */
    public CharSequence mTitle;
    public TextView mTitltView;

    /** 提示信息 */
    private CharSequence mMessage;
    private TextView mMessageView;
    private ScrollView mScrollView;

    /** 自定义view */
    private View mView;
    private int mViewSpacingLeft;
    private int mViewSpacingTop;
    private int mViewSpacingRight;
    private int mViewSpacingBottom;
    private boolean mViewSpacingSpecified = false;

    /** 控制按钮 */
    private CharSequence mPositiveButtonText;
    private Button mPositiveButton;
    private Message mPositiveButtonMessage;
    private Drawable mPositiviButtonBackgrounDrawable;
    private CharSequence mNegativeButtonText;
    private Button mNegativeButton;
    private Message mNegativeButtonMessage;
    private Drawable mNegativeButtonBackgrounDrawable;
    private CharSequence mNeutralButtonText;
    private Button mNeutralButton;
    private Message mNeutralButtonMessage;
    private Drawable mNeutralButtonBackgrounDrawable;

    private float mRadiusSize = DEFAULT_RADIUS_SIZE_DP;

    View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Message m = null;
            if (v == mPositiveButton && mPositiveButtonMessage != null) {
                m = Message.obtain(mPositiveButtonMessage);
            } else if (v == mNegativeButton && mNegativeButtonMessage != null) {
                m = Message.obtain(mNegativeButtonMessage);
            } else if (v == mNeutralButton && mNeutralButtonMessage != null) {
                m = Message.obtain(mNeutralButtonMessage);
            }
            if (m != null) {
                m.sendToTarget();
            }

            // Post a message so we dismiss after the above handlers are
            // executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG,
                mDialogInterface).sendToTarget();
        }
    };

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(
                        mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    public AlertControll(Context context, DialogInterface dialogInterface, Window window) {
        mContext = context;
        mDialogInterface = dialogInterface;
        mWindow = window;
        mHandler = new ButtonHandler(dialogInterface);
    }

    public void installContent() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
//        if(width > height){
//            isNormal = false;
//        }

        /* We use a custom title so never request a window title */
//        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setContentView(R.layout.dialog_tips);
        setupView();
    }

    private void setupView() {
        setRadius();

        setupTitle();

        setupContent();

        setupCustomView();

        mWindow.findViewById(R.id.line_between_content_and_button).setVisibility(setupButtons() ? View.VISIBLE : View.GONE);
    }

    private void setupCustomView() {
        FrameLayout customPanel = (FrameLayout) mWindow.findViewById(R.id.view_dialog_custom_panel);
        if (mView != null) {
            FrameLayout custom = (FrameLayout) mWindow.findViewById(R.id.view_dialog_custom);
            custom.addView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
            if (mViewSpacingSpecified) {
                custom.setPadding(mViewSpacingLeft, mViewSpacingTop,
                    mViewSpacingRight, mViewSpacingBottom);
            }
        } else {
            customPanel.setVisibility(View.GONE);
        }
    }

    /**
     * 设置dialog圆角大小
     */
    private void setRadius() {
        mDialogPanel = (CardView) mWindow.findViewById(R.id.view_dialog_content_panel);
        mDialogPanel.setRadius(dp2px(mRadiusSize));
    }

    /**
     * 设置标题
     */
    private void setupTitle() {
        mTitltView = (TextView) mWindow.findViewById(R.id.tv_dialog_title);
        if (!TextUtils.isEmpty(mTitle)) {
            mTitltView.setText(mTitle);
            if (TextUtils.isEmpty(mMessage)) {
                mTitltView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        } else {
            mTitltView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置内容
     */
    private void setupContent() {
        mScrollView = (ScrollView) mWindow.findViewById(R.id.scroll_view);
        mMessageView = (TextView) mWindow.findViewById(R.id.tv_tips_content);
        if (!TextUtils.isEmpty(mMessage)) {
            mScrollView.setVisibility(View.VISIBLE);
            mMessageView.setText(mMessage);
        } else {
            mScrollView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置内容
     * @return
     */
    private boolean setupButtons() {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;
        mNegativeButton = (Button) mWindow.findViewById(R.id.button1);
        mNeutralButton = (Button) mWindow.findViewById(R.id.button2);
        mPositiveButton = (Button) mWindow.findViewById(R.id.button3);

        mPositiveButton.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mPositiveButtonText)) {
            mPositiveButton.setVisibility(View.GONE);
        } else {
            mPositiveButton.setText(mPositiveButtonText);
            mPositiveButton.setVisibility(View.VISIBLE);
            setButtonBackgroud(mPositiveButton, mPositiviButtonBackgrounDrawable);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
            if (TextUtils.isEmpty(mNegativeButtonText)
                && TextUtils.isEmpty(mNeutralButtonText)) {
                // 只有positive按钮
            }
        }

        mNegativeButton.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mNegativeButtonText)) {
            mNegativeButton.setVisibility(View.GONE);
        } else {
            mNegativeButton.setText(mNegativeButtonText);
            mNegativeButton.setVisibility(View.VISIBLE);
            setButtonBackgroud(mNegativeButton, mNegativeButtonBackgrounDrawable);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
            if (TextUtils.isEmpty(mPositiveButtonText)
                && TextUtils.isEmpty(mNeutralButtonText)) {
                // 只有negative按钮
            }
        }

        mNeutralButton.setOnClickListener(mButtonHandler);
        if (TextUtils.isEmpty(mNeutralButtonText)) {
            mNeutralButton.setVisibility(View.GONE);
        } else {
            mNeutralButton.setText(mNeutralButtonText);
            mNeutralButton.setVisibility(View.VISIBLE);
            setButtonBackgroud(mNeutralButton, mNeutralButtonBackgrounDrawable);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
            if (TextUtils.isEmpty(mPositiveButtonText)
                && TextUtils.isEmpty(mNegativeButtonText)) {
                // 只有neutral按钮
            }
        }

        /*
         * If we only have 1 button it should be centered on the layout and
         * expand to fill 50% of the available space.
         */
        if (whichButtons == BIT_BUTTON_POSITIVE) {
            centerButton(mPositiveButton);
        } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
            centerButton(mNegativeButton);
        } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
            centerButton(mNeutralButton);
        }
        return whichButtons != 0;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button
            .getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(params);
    }

    /**
     * Sets a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code listener} or {@code msg}.
     *
     * @param whichButton
     *            Which button, can be one of
     *            {@link DialogInterface#BUTTON_POSITIVE},
     *            {@link DialogInterface#BUTTON_NEGATIVE}, or
     *            {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text
     *            The text to display in positive button.
     * @param listener
     *            The {@link DialogInterface.OnClickListener} to use.
     * @param msg
     *            The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                mPositiveButtonText = text;
                mPositiveButtonMessage = msg;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                mNegativeButtonText = text;
                mNegativeButtonMessage = msg;
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                mNeutralButtonText = text;
                mNeutralButtonMessage = msg;
                break;

            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(View view) {
        mView = view;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog along with the spacing around that
     * view
     */
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
                        int viewSpacingRight, int viewSpacingBottom) {
        mView = view;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitltView != null) {
            mTitltView.setText(mTitle);
        }
    }

    /**
     * @param message
     */
    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    /**
     * @param dp 圆角大小
     */
    public void setRadius(float dp) {
        mRadiusSize = Math.max(0, dp);
        if (mDialogPanel != null) {
            mDialogPanel.setRadius(dp2px(dp));
        }
    }

    public void setPositiveButtonBackgroudDrawable(Drawable drawable) {
        this.mPositiviButtonBackgrounDrawable = drawable;
        if (!TextUtils.isEmpty(mPositiveButtonText)
            && mPositiveButton != null) {
            setButtonBackgroud(mPositiveButton, mPositiviButtonBackgrounDrawable);
        }
    }

    public void setPositiveButtonBackgroudDrawable(int drawableId) {
        setPositiveButtonBackgroudDrawable(mContext.getResources().getDrawable(drawableId));
    }

    public void setNegativeButtonBackgroudDrawable(Drawable drawable) {
        this.mNegativeButtonBackgrounDrawable = drawable;
        if (!TextUtils.isEmpty(mNegativeButtonText)
            && mNegativeButton != null) {
            setButtonBackgroud(mNegativeButton, mNegativeButtonBackgrounDrawable);
        }
    }

    public void setNegativeButtonBackgroudDrawable(int drawableId) {
        setNegativeButtonBackgroudDrawable(mContext.getResources().getDrawable(drawableId));
    }

    public void setNeutralButtonBackgroudDrawable(Drawable drawable) {
        this.mNeutralButtonBackgrounDrawable = drawable;
        if (!TextUtils.isEmpty(mNeutralButtonText)
            && mNeutralButton != null) {
            setButtonBackgroud(mNeutralButton, mNeutralButtonBackgrounDrawable);
        }
    }
    public void setNeutralButtonBackgroudDrawable(int drawableId) {
        setNeutralButtonBackgroudDrawable(mContext.getResources().getDrawable(drawableId));
    }


    private void setButtonBackgroud(View targetview, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            targetview.setBackground(drawable);
        } else {
            targetview.setBackgroundDrawable(drawable);
        }
    }

    private float dp2px(float dp) {
        return mContext.getResources().getDisplayMetrics().density * dp;
    }

    public static class AlertParams {

        public final Context mContext;
        public final LayoutInflater mInflater;

        /** 标题 */
        public CharSequence mTitle;
        public TextView mTitltView;

        /** 提示信息 */
        public CharSequence mMessage;
        public TextView mMessageView;

        public View mView;
        public boolean mViewSpacingSpecified = false;

        /** 控制按钮 */
        public CharSequence mPositiveButtonText;
        public Drawable mPositiveButtonBackgrounDrawable;
        public DialogInterface.OnClickListener mPositiviButtonListener;
        public CharSequence mNegativeButtonText;
        public Drawable mNegativeButtonBackgrounDrawable;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNeutralButtonText;
        public Drawable mNeutralButtonBackgrounDrawable;
        public DialogInterface.OnClickListener mNeutralButtonListener;

        public boolean mCancelable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;

        ///////////////////////////////////////////////////////////////////////////
        // 扩展属性
        ///////////////////////////////////////////////////////////////////////////
        public float mRadiusSize = DEFAULT_RADIUS_SIZE_DP;

        public AlertParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Drawable defaultDrawable = mContext.getResources().getDrawable(R.drawable.selector_drawable_button_white);
            mPositiveButtonBackgrounDrawable = defaultDrawable;
            mNegativeButtonBackgrounDrawable = defaultDrawable;
            mNeutralButtonBackgrounDrawable = defaultDrawable;
        }

        public void apply(AlertControll alertControll) {
            if (!TextUtils.isEmpty(mTitle)) {
                alertControll.setTitle(mTitle);
            }

            if (!TextUtils.isEmpty(mMessage)) {
                alertControll.setMessage(mMessage);
            }

            if (!TextUtils.isEmpty(mPositiveButtonText)) {
                alertControll.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveButtonText, mPositiviButtonListener, null);
            }

            if (!TextUtils.isEmpty(mNegativeButtonText)) {
                alertControll.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText, mNegativeButtonListener, null);
            }

            if (!TextUtils.isEmpty(mNeutralButtonText)) {
                alertControll.setButton(DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText, mNeutralButtonListener, null);
            }

            if (null != mView) {
                alertControll.setView(mView);
            }
            alertControll.setPositiveButtonBackgroudDrawable(mPositiveButtonBackgrounDrawable);
            alertControll.setNegativeButtonBackgroudDrawable(mNegativeButtonBackgrounDrawable);
            alertControll.setNeutralButtonBackgroudDrawable(mNeutralButtonBackgrounDrawable);
            alertControll.setRadius(mRadiusSize);
        }
    }
}
