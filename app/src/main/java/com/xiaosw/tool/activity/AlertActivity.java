package com.xiaosw.tool.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.library.utils.LogUtil;
import com.xiaosw.library.widget.dialog.BaseAlertDialog;
import com.xiaosw.tool.R;

public class AlertActivity extends BaseAppCompatActivity {
    
    private static final String TAG = "AlertActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);
    }

    public void showDialog(View view) {
        switch (view.getId()) {
            case R.id.bt_origin_alert:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                builder.setTitle("mTitle");
                builder.setMessage("mMessage");
                TextView textView = new TextView(this);
                textView.setText("custom view");
                builder.setView(textView);

                TextView customTitle = new TextView(this);
                textView.setText("customTitle");
                builder.setCustomTitle(customTitle);
                builder.setPositiveButton("PositiveButton", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            break;
            case R.id.bt_custom_all:
                BaseAlertDialog.Builder build = new BaseAlertDialog.Builder(this)
                    .setRadius(20)
                    .setTitle("TITLE")
                    .setMessage("MESSAGE")
                    .setCancelable(false);
                build.setPositiveButton("BUTTON_POSITIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                build.setNegativeButton("BUTTON_NEGATIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                build.setNeutralButton("BUTTON_NEUTRAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });
                build.show();
                break;

            case R.id.bt_title_button:
                BaseAlertDialog baseAlertDialog = new BaseAlertDialog.Builder(this).create();
                baseAlertDialog.setTitle("TITLE");
                baseAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BUTTON_POSITIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                baseAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "BUTTON_NEGATIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                baseAlertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "BUTTON_NEUTRAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });
                baseAlertDialog.show();
                break;

            case R.id.bt_message_button:
                baseAlertDialog = new BaseAlertDialog.Builder(this).create();
                baseAlertDialog.setMessage(mMessage + mMessage);
                baseAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BUTTON_POSITIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                baseAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "BUTTON_NEGATIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                baseAlertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "BUTTON_NEUTRAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });
                baseAlertDialog.show();

            break;

            case R.id.bt_one_button:
                baseAlertDialog = new BaseAlertDialog.Builder(this).create();
                baseAlertDialog.setTitle("TITLE");
                baseAlertDialog.setMessage("MESSAGE");
                baseAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BUTTON_POSITIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });
                baseAlertDialog.show();
                break;

            case R.id.bt_two_button:
                baseAlertDialog = new BaseAlertDialog.Builder(this).create();
                baseAlertDialog.setTitle("TITLE");
                baseAlertDialog.setMessage("MESSAGE");
                baseAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "BUTTON_POSITIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });

                baseAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "BUTTON_NEGATIVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.e("--------------------------> " + which);
                    }
                });
                baseAlertDialog.show();

            default:
                // TODO: 2016/10/18
        }
    }

    private String mMessage = "private final Context mContext;  \n" +
        "private final DialogInterface mDialogInterface;  \n" +
        "private final Window mWindow;  \n" +
        "private CharSequence mTitle;  \n" +
        "private CharSequence mMessage;  \n" +
        "private ListView mListView;  \n" +
        "private View mView;  \n" +
        "private int mViewSpacingLeft;  \n" +
        "private int mViewSpacingTop;  \n" +
        "private int mViewSpacingRight;  \n" +
        "private int mViewSpacingBottom;  \n" +
        "private boolean mViewSpacingSpecified = false; \n" +
        "private Button mButtonPositive;  \n" +
        "private CharSequence mButtonPositiveText;  \n" +
        "private Message mButtonPositiveMessage;  \n" +
        "private Button mButtonNegative;  \n" +
        "private CharSequence mButtonNegativeText;  \n" +
        "private Message mButtonNegativeMessage;  \n" +
        "private Button mButtonNeutral;  \n" +
        "private CharSequence mButtonNeutralText;  \n" +
        "private Message mButtonNeutralMessage;  \n" +
        "private ScrollView mScrollView;  \n" +
        "private int mIconId = -1;  \n" +
        "private Drawable mIcon;  \n" +
        "private ImageView mIconView;  \n" +
        "private TextView mTitleView;  \n" +
        "private TextView mMessageView;  \n" +
        "private View mCustomTitleView;  \n" +
        "private boolean mForceInverseBackground;  \n" +
        "private ListAdapter mAdapter;  \n" +
        "private int mCheckedItem = -1;  \n" +
        "private int mAlertDialogLayout;  \n" +
        "private int mListLayout;  \n" +
        "private int mMultiChoiceItemLayout;  \n" +
        "private int mSingleChoiceItemLayout;  \n" +
        "private int mListItemLayout;  \n" +
        "private Handler mHandler;  \n";
}
