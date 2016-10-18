package com.xiaosw.library.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xiaosw.library.R;
import com.xiaosw.library.controll.AlertWindowControll;
import com.xiaosw.library.utils.ScreenUtil;

/**
 * @ClassName : {@link BaseDialog}
 * @Description : 弹框基类
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-23 10:10:52
 */
public class BaseDialog extends AlertDialog implements View.OnClickListener {

    TextView tv_dialog_title; // 标题

    TextView tv_tips_content; // 提示内容

    Button bt_ok; // 确定

    Button bt_cancel; // 取消

    private View mContentView;
    private OnClickOperationListener mOnClickOperationListener;

    public BaseDialog(Context context) {
        this(context, R.style.alert_dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        mContentView = View.inflate(getContext(), R.layout.dialog_tips, null);
        setContentView(mContentView);
        tv_dialog_title = getViewById(R.id.tv_dialog_title);
        tv_tips_content = getViewById(R.id.tv_tips_content);
        bt_ok = getViewById(R.id.bt_ok);
        bt_cancel = getViewById(R.id.bt_cencel);
        bt_ok.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        initialize();
    }

    void initialize() {
    }

    public void setOnClickOperationListener(OnClickOperationListener onClickOperationListener) {
        this.mOnClickOperationListener = onClickOperationListener;
    }

    public void setTitle(CharSequence title) {
        tv_dialog_title.setText(title);
    }

    public void setTitle(int resId) {
        tv_dialog_title.setText(resId);
    }

    public void setContentStr(CharSequence title) {
        tv_tips_content.setText(title);
    }

    public void setContentStr(int resId) {
        tv_tips_content.setText(resId);
    }

    public void setOkStr(CharSequence title) {
        bt_ok.setText(title);
    }

    public void setOkStr(int resId) {
        bt_ok.setText(resId);
    }

    public void setCancelStr(CharSequence title) {
        bt_cancel.setText(title);
    }

    public void setCancelStr(int resId) {
        bt_cancel.setText(resId);
    }

    public TextView getTitleView() {
        return tv_dialog_title;
    }

    public TextView getContentView() {
        return tv_tips_content;
    }

    public Button getOkView() {
        return bt_ok;
    }

    public Button getCancelView() {
        return bt_cancel;
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
    public void onClick(View view) {
        dismiss();
        if (null == mOnClickOperationListener) {
            return;
        }
        int id = view.getId();
        if (R.id.bt_ok == id) {
            mOnClickOperationListener.onOk(view);
        } else if (R.id.bt_cencel == id) {
            mOnClickOperationListener.onCancel(view);
        }
    }

    <V extends View> V getViewById(int id) {
        return findViewById(mContentView, id);
    }

    <V extends View> V findViewById(View parentView, int id) {
        return (V) parentView.findViewById(id);
    }

    /**
     * 按钮点击回掉
     */
    public interface OnClickOperationListener {
        /**
         * 确定
         * @param view
         */
        public void onOk(View view);

        /**
         * 取消
         * @param view
         */
        public void onCancel(View view);
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
}