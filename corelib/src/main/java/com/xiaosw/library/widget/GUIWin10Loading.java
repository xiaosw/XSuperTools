package com.xiaosw.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.xiaosw.library.R;
import com.xiaosw.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><br/>ClassName : {@link GUIWin10Loading}
 * <br/>Description : 仿Win10加载动画
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2016-12-30 19:19:47</p>
 */
public class GUIWin10Loading extends RelativeLayout {

    /** @see GUIWin10Loading#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-GUIWin10Loading";
    /**
     * 点颜色
     */
    private int mDotColor;

    private int mWidth;
    private int mHeight;

    private float mDotDegree; // 一个圆点所占的角度

    private View[] mDotViews = new View[5];
    private AnimatorSet mAnimSet;

    public GUIWin10Loading(Context context) {
        this(context, null);
    }

    public GUIWin10Loading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GUIWin10Loading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GUIWin10Loading);
        // 点的默认颜色是Win10中的绿色
        mDotColor = ta.getColor(R.styleable.GUIWin10Loading_dotColor, Color.rgb(0x92, 0xcb, 0x29));
        ta.recycle();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (mAnimSet != null) {
            if (visibility == VISIBLE) {
                if (!mAnimSet.isStarted()) {
                    mAnimSet.start();
                }
            } else {
                mAnimSet.cancel();
            }
        }
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mWidth != w || mHeight != h) {
            mWidth = w;
            mHeight = h;
            initAnim();
            if (getVisibility() == VISIBLE) {
                if (mAnimSet != null) {
                    mAnimSet.start();
                }
            }
        }
    }


    private void initAnim() {
        // 计算点半径
        float halfSize = mWidth < mHeight ? (mWidth * 0.5f) : (mHeight * 0.5f); // 大小
        float dotR = halfSize / 14; // 点半径
        int dotD = (int) (2 * dotR); // 点直径

        // 计算圆点对旋转中心所占的角度
        float trackR = halfSize - dotR;
        mDotDegree = (float) Math.toDegrees(2 * Math.asin(dotR / trackR));
        LogUtil.i(TAG, "mDotDegree=" + mDotDegree);

        // 1、 清除所有控件
        removeAllViews();

        // 2、 添加新控件
        for (int i = 0; i < mDotViews.length; i++) {
            mDotViews[i] = new View(getContext());

            View view = mDotViews[i];
            LayoutParams lp = new LayoutParams(dotD, dotD);
            // 添加规则：底部 + 水平居中
            lp.addRule(ALIGN_PARENT_BOTTOM);
            lp.addRule(CENTER_HORIZONTAL);
            // 调整位置
            if (mHeight > mWidth) {
                lp.bottomMargin = (mHeight - mWidth) / 2;
            }
            // 设置旋转中心点
            ViewHelper.setPivotX(view, dotR);
            ViewHelper.setPivotY(view, -(halfSize - dotD));
            // 背景
            view.setBackgroundResource(R.drawable.drawable_dot_shape);
            // 修改点的背景颜色
            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            gradientDrawable.setColor(mDotColor);
            view.setVisibility(INVISIBLE);
            addView(view, lp);
        }

        // 3、初始化动画集合
        initAnimSet();
    }

    private void initAnimSet() {
        mAnimSet = new AnimatorSet();

        List<Animator> animList = new ArrayList<>(5);
        for (int i = 0; i < mDotViews.length; i++) {
            animList.add(createViewAnim(mDotViews[i], i));
        }
        mAnimSet.playTogether(animList);
    }


    /**
     * 创建动画
     *
     * @param view 需执行的控件
     * @param index 该控件执行的顺序
     * @return 该控件的动画
     */
    private Animator createViewAnim(final View view, final int index) {
        long duration = 7500; // 一个周期（2圈）一共运行7500ms，固定值

        // 最小执行单位时间
        final float minRunUnit = duration / 100f;
        // 最小执行单位时间所占总时间的比例
        double minRunPer = minRunUnit / duration;
        // 在插值器中实际值（Y坐标值），共8组
        final double[] trueRunInOne = new double[]{
            0,
            0,
            160 / 720d,
            190 / 720d,
            360 / 720d,
            520 / 720d,
            550 / 720d,
            1
        };
        // 动画开始的时间比偏移量。剩下的时间均摊到每个圆点上
        final float offset = (float) (index * (100 - 86) * minRunPer / (mDotViews.length - 1));
        // 在差值器中理论值（X坐标值），与realRunInOne对应
        final double[] rawRunInOne = new double[]{
            0,
            offset + 0,
            offset + 11 * minRunPer,
            offset + 32 * minRunPer,
            offset + 43 * minRunPer,
            offset + 54 * minRunPer,
            offset + 75 * minRunPer,
            offset + 86 * minRunPer
        };

        // 各贝塞尔曲线控制点的Y坐标
        final float p1_2 = calculateLineY(rawRunInOne[2], trueRunInOne[2], rawRunInOne[3], trueRunInOne[3], rawRunInOne[1]);
        final float p1_4 = calculateLineY(rawRunInOne[2], trueRunInOne[2], rawRunInOne[3], trueRunInOne[3], rawRunInOne[4]);
        final float p1_5 = calculateLineY(rawRunInOne[5], trueRunInOne[5], rawRunInOne[6], trueRunInOne[6], rawRunInOne[4]);
        final float p1_7 = calculateLineY(rawRunInOne[5], trueRunInOne[5], rawRunInOne[6], trueRunInOne[6], rawRunInOne[7]);

        // A 创建属性动画：绕着中心点旋转2圈
        ObjectAnimator objAnim = ObjectAnimator.ofFloat(view, "rotation", -mDotDegree * index, 720 - mDotDegree * index);
        // B 设置一个周期执行的时间
        objAnim.setDuration(duration);
        // C 设置重复执行的次数：无限次重复执行下去
        objAnim.setRepeatCount(ValueAnimator.INFINITE);
        // D 设置差值器
        objAnim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input < rawRunInOne[1]) {
                    // 1 等待开始
                    if (view.getVisibility() != INVISIBLE) {
                        view.setVisibility(INVISIBLE);
                    }
                    return 0;

                } else if (input < rawRunInOne[2]) {
                    if (view.getVisibility() != VISIBLE) {
                        view.setVisibility(VISIBLE);
                    }
                    // 2 底部 → 左上角：贝赛尔曲线1
                    // 先转换成[0, 1]范围
                    input = calculateNewPercent(rawRunInOne[1], rawRunInOne[2], 0, 1, input);
                    return calculateBezierQuadratic(trueRunInOne[1], p1_2, trueRunInOne[2], input);

                } else if (input < rawRunInOne[3]) {
                    // 3 左上角 → 顶部：直线
                    return calculateLineY(rawRunInOne[2], trueRunInOne[2], rawRunInOne[3], trueRunInOne[3], input);

                } else if (input < rawRunInOne[4]) {
                    // 4 顶部 → 底部：贝赛尔曲线2
                    input = calculateNewPercent(rawRunInOne[3], rawRunInOne[4], 0, 1, input);
                    return calculateBezierQuadratic(trueRunInOne[3], p1_4, trueRunInOne[4], input);

                } else if (input < rawRunInOne[5]) {
                    // 5 底部 → 左上角：贝赛尔曲线3
                    input = calculateNewPercent(rawRunInOne[4], rawRunInOne[5], 0, 1, input);
                    return calculateBezierQuadratic(trueRunInOne[4], p1_5, trueRunInOne[5], input);

                } else if (input < rawRunInOne[6]) {
                    // 6 左上角 → 顶部：直线
                    return calculateLineY(rawRunInOne[5], trueRunInOne[5], rawRunInOne[6], trueRunInOne[6], input);

                } else if (input < rawRunInOne[7]) {
                    // 7 顶部 → 底部：贝赛尔曲线4
                    input = calculateNewPercent(rawRunInOne[6], rawRunInOne[7], 0, 1, input);
                    return calculateBezierQuadratic(trueRunInOne[6], p1_7, trueRunInOne[7], input);

                } else {
                    // 8 消失
                    if (view.getVisibility() != INVISIBLE) {
                        view.setVisibility(INVISIBLE);
                    }
                    return 1;
                }
            }
        });
        return objAnim;
    }

    /**
     * 根据旧范围，给定旧值，计算在新范围中的值
     *
     * @param oldStart 旧范围的开始值
     * @param oldEnd   旧范围的结束值
     * @param newStart 新范围的开始值
     * @param newEnd   新范围的结束之
     * @param value    给定旧值
     * @return 新范围的值
     */
    private float calculateNewPercent(double oldStart, double oldEnd, double newStart, double newEnd, double value) {
        if ((value < oldStart && value < oldEnd) || (value > oldStart && value > oldEnd)) {
            throw new IllegalArgumentException(String.format("参数输入错误，value必须在[%f, %f]范围中", oldStart, oldEnd));
        }
        return (float) ((value - oldStart) * (newEnd - newStart) / (oldEnd - oldStart));
    }

    /**
     * 根据两点坐标形成的直线，计算给定X坐标在直线上对应的Y坐标值
     *
     * @param x1 起点X坐标
     * @param y1 起点Y坐标
     * @param x2 终点X坐标
     * @param y2 终点Y坐标
     * @param x  给定的X坐标
     * @return 给定X坐标对应的Y坐标
     */
    private float calculateLineY(double x1, double y1, double x2, double y2, double x) {
        if (x1 == x2) {
            return (float) y1;
        }
        return (float) ((x - x1) * (y2 - y1) / (x2 - x1) + y1);
    }

    /**
     * 计算贝塞尔二阶曲线的X（或Y）坐标值
     * 给定起点、控制点、终点的X（或Y）坐标值，和给定时间t（∈[0, 1]），算出此时贝塞尔曲线的X（或Y）坐标值
     *
     * @param p0 起点值
     * @param p1 控制点值
     * @param p2 终点值
     * @param t  给定的时间
     * @return 曲线的位置值
     */
    private float calculateBezierQuadratic(double p0, double p1, double p2, @FloatRange(from = 0, to = 1) double t) {
        double tmp = 1 - t;
        return (float) (tmp * tmp * p0 + 2 * tmp * t * p1 + t * t * p2);
    }

    /**
     * 三阶贝塞尔曲线
     * @param p0 起点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终点
     * @param t 时间
     * @return 曲线对应坐标值
     */
    private float calculateBezierCubic(double p0, double p1, double p2, double p3, @FloatRange(from = 0, to = 1) double t) {
        double tmp = 1 - t;
        return (float) (tmp * tmp * tmp * p0 + 3 * tmp * tmp * t * p1 + 3 * tmp * t * t * p2 + t * t* t * p3);
    }

    /**
     * Setter and Getter
     */
    public int getDotColor() {
        return mDotColor;
    }

    public synchronized void setDotColor(int dotColor) {
        this.mDotColor = dotColor;
        for (View view : mDotViews) {
            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            gradientDrawable.setColor(dotColor);
        }
    }
}
