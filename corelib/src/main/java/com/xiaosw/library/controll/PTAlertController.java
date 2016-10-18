package com.xiaosw.library.controll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaosw.library.R;

/**
 * @ClassName : {@link PTAlertController}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-10-18 10:10:11
 */
public class PTAlertController {

    protected final Context mContext;
    private final Dialog mDialog;
    private final DialogInterface mDialogInterface;
    private final Window mWindow;

    /** 标题内容 */
    private CharSequence mTitle;
    /** 标题视图 */
    private TextView mTitleView;
    /** 自定义标题视图 */
    private View mCustomTitleView;
    /** icon */
    private int mIconId = 0;
    /** icon */
    private Drawable mIcon;
    /** 自定义content view */
    private View mCustomContentView;
    private RecyclerView mRecyclerView;

    private CharSequence mMessage;

    private Button mButtonPositive;
    private CharSequence mButtonPositiveText;
    private Button mButtonNegative;
    private CharSequence mButtonNegativeText;
    private Button mButtonNeutral;
    private CharSequence mButtonNeutralText;
    private ScrollView mScrollView;
    private TextView mMessageView;

    View.OnClickListener mButtonClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    };

    public PTAlertController(Dialog dialog, DialogInterface dialogInterface) {
        mDialog = dialog;
        mWindow = dialog.getWindow();
        mDialogInterface = dialogInterface;
        mContext = dialog.getContext();
    }

    public void installContent() {
        /* We use a custom title so never request a window title */
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setContentView(R.layout.dialog_alert);
        setupView();
    }

    private void setupView() {
        LinearLayout contentPanel = (LinearLayout) mWindow.findViewById(R.id.view_dialog_content_panel);
        setupContent(contentPanel);
        boolean hasButtons = setupButtons();

        LinearLayout topPanel = (LinearLayout) mWindow
            .findViewById(R.id.view_dialog_title_panel);
        // TypedArray a = mContext.obtainStyledAttributes(
        // null, null, android.R.attr.alertDialogStyle, 0);
        TypedArray a = null;
        boolean hasTitle = setupTitle(topPanel);

        View buttonPanel = mWindow.findViewById(R.id.view_dialog_button_pannel);
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }

        FrameLayout customPanel = (FrameLayout) mWindow.findViewById(R.id.view_dialog_custom_panel);
        if (mCustomContentView != null) {

            FrameLayout custom = (FrameLayout) mWindow
                .findViewById(R.id.view_dialog_custom);
            custom.addView(mCustomContentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (mRecyclerView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
            }
        } else {
            customPanel.setVisibility(View.GONE);
        }

        /*
         * Only display the divider if we have a title and a custom view or a
         * message.
         */
        if (hasTitle && ((mMessage != null) || (mCustomContentView != null))) {
            // View divider = mWindow.findViewById(R.id.titleDivider);
            // divider.setVisibility(View.VISIBLE);
        }

        // a.recycle();
    }

    private void setupContent(LinearLayout contentPanel) {
        mScrollView = (ScrollView) mWindow.findViewById(R.id.view_dialog_scroll_view);
        mScrollView.setFocusable(false);

        // Special case for users that only want to display a String
        mMessageView = (TextView) mWindow.findViewById(R.id.tv_dialog_message);
        if (mMessageView == null) {
            return;
        }

        if (mMessage != null) {
            mMessageView.setText(mMessage);
        } else {
            mMessageView.setVisibility(View.GONE);
            mScrollView.removeView(mMessageView);
            contentPanel.setVisibility(View.GONE);
        }
    }

    private boolean setupButtons() {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;
        mButtonPositive = (Button) mWindow.findViewById(R.id.btton1);
        mButtonNegative = (Button) mWindow.findViewById(R.id.btton2);
        mButtonNeutral = (Button) mWindow.findViewById(R.id.btton3);

        mButtonPositive.setOnClickListener(mButtonClick);

        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
        }

        mButtonNegative.setOnClickListener(mButtonClick);

        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
        }

        mButtonNeutral.setOnClickListener(mButtonClick);

        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);
        }

        /*
         * If we only have 1 button it should be centered on the layout and
         * expand to fill 50% of the available space.
         */
        if (whichButtons == BIT_BUTTON_POSITIVE) {
            centerButton(mButtonPositive);
        } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
            centerButton(mButtonNeutral);
        } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
            centerButton(mButtonNeutral);
        }

        return whichButtons != 0;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button
            .getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
        button.setLayoutParams(params);
    }

    private boolean setupTitle(LinearLayout topPanel) {
        boolean hasTitle = true;
        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
            topPanel.addView(mCustomTitleView, lp);
            // Hide the title template
            mWindow.findViewById(R.id.tv_dialog_title).setVisibility(View.GONE);
        } else {
            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);
            if (hasTextTitle) {
                /* Display the title if a title is supplied, else hide it */
                mTitleView = (TextView) mWindow.findViewById(R.id.tv_dialog_title);

                mTitleView.setText(mTitle);
                // mIconView.setImageResource(R.drawable.ic_dialog_menu_generic);

                /*
                 * Do this last so that if the user has supplied any icons we
                 * use them instead of the default ones. If the user has
                 * specified 0 then make it disappear.
                 */
                if (mIconId > 0) {
                    mTitleView.setCompoundDrawablesWithIntrinsicBounds(mIconId, 0, 0, 0);
                } else if (mIcon != null) {
                    mTitleView.setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
                }
            } else {

                // Hide the title template
                View titleTemplate = mWindow.findViewById(R.id.view_dialog_title_panel);
                titleTemplate.setVisibility(View.GONE);
                hasTitle = false;
            }
        }
        return hasTitle;
    }

    /**
     * @see AlertDialog.Builder#setCustomTitle(View)
     */
    public void setCustomTitle(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    /**
     * Set resId to 0 if you don't want an icon.
     *
     * @param resId
     *            the resourceId of the drawable to use as the icon or 0 if you
     *            don't want an icon.
     */
    public void setIcon(int resId) {
        mIconId = resId;
        if (mTitleView != null) {
            mTitleView.setCompoundDrawablesWithIntrinsicBounds(mIconId, 0, 0, 0);
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        mTitleView.setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
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
     */
    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener) {

        switch (whichButton) {

            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                mButtonNeutralText = text;
                break;

            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    public static class AlertParams {
        public final Context mContext;
        private final LayoutInflater mLayoutInflater;

        public CharSequence mTitle;
        public int mIconId = -1;
        public Drawable mIcon;
        public View mCustomTitleView;
        public CharSequence mMessage;
        public CharSequence mPositiveButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public boolean mCancelable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;


        public AlertParams(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void apply(PTAlertController dialog) {
            if (mCustomTitleView != null) {
                dialog.setCustomTitle(mCustomTitleView);
            } else {
                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                if (mIcon != null) {
                    dialog.setIcon(mIcon);
                }
                if (mIconId >= 0) {
                    dialog.setIcon(mIconId);
                }
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mPositiveButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    mPositiveButtonText, mPositiveButtonListener);
            }
            if (mNegativeButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    mNegativeButtonText, mNegativeButtonListener);
            }
            if (mNeutralButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                    mNeutralButtonText, mNeutralButtonListener);
            }

            /*
             * dialog.setCancelable(mCancelable);
             * dialog.setOnCancelListener(mOnCancelListener); if (mOnKeyListener
             * != null) { dialog.setOnKeyListener(mOnKeyListener); }
             */
        }

    }

}
