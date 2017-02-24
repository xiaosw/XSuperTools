package com.xiaosw.tool.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.xiaosw.datepicker.DateUtil;
import com.xiaosw.datepicker.view.WheelView;
import com.xiaosw.datepicker.widget.DateAdapter;
import com.xiaosw.datepicker.widget.WheelViewAdapter;
import com.xiaosw.library.activity.BaseAppCompatActivity;
import com.xiaosw.tool.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class DateSelectorActivity extends BaseAppCompatActivity {

    /** @see DateSelectorActivity#getClass().getSimpleName() */
    private static final String TAG = "DateSelectorActivity";

    @BindView(R.id.view_year)
    WheelView view_year;

    @BindView(R.id.view_month)
    WheelView view_month;

    @BindView(R.id.view_day)
    WheelView view_day;

    @BindView(R.id.tv_result)
    TextView tv_result;

    private DateAdapter mYearAdapter;
    private DateAdapter mMonthAdapter;
    private DateAdapter mDayAdapter;

    private List<String> mYearDatas;
    private List<String> mMonthDatas;
    private List<String> mDayDatas;

    private Calendar mCalendar;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selector);
        useCustomActionBar();
        setDisplayHomeAsUpEnabled(true);
        setTitle(TAG);

        mCalendar = Calendar.getInstance();
        mCurrentYear = mCalendar.get(Calendar.YEAR);
        mCurrentMonth = mCalendar.get(Calendar.MONTH);
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        initData();
        updateResult();

        setYearView();

        setMonthView();

        setDayView();
    }

    private void setYearView() {
        Log.e(TAG, "setYearView: " + mCurrentYear);
        mYearAdapter = new DateAdapter(this, mYearDatas, mCurrentYear - DateUtil.MIN_START_YEAR);
        view_year.setVisibleItems(5);
        view_year.setViewAdapter(mYearAdapter);
        view_year.setCurrentItem(mCurrentYear - DateUtil.MIN_START_YEAR);
        view_year.addChangingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String year = mYearAdapter.updateTextStyle(newValue);
                if (!TextUtils.isEmpty(year)) {
                    mCurrentYear = Integer.parseInt(year);
                }
                updateResult();
            }
        });
        view_year.addScrollingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String year = mYearAdapter.updateTextStyle(wheel.getCurrentItem());
                if (!TextUtils.isEmpty(year)) {
                    mCurrentYear = Integer.parseInt(year);
                }
                initDays(DateUtil.getMaxDayByMonthAndMonth(mCurrentYear, mCurrentMonth + 1));
                setDayView();
            }
        });
    }

    private void setMonthView() {
        mMonthAdapter = new DateAdapter(this, mMonthDatas, mCurrentMonth);
        view_month.setVisibleItems(5);
        view_month.setViewAdapter(mMonthAdapter);
        view_month.setCurrentItem(mCurrentMonth);
        view_month.addChangingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String month = mMonthAdapter.updateTextStyle(newValue);
                if (!TextUtils.isEmpty(month)) {
                    mCurrentMonth = Integer.parseInt(month) - 1;
                }
                updateResult();
            }
        });
        view_month.addScrollingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mMonthAdapter.updateTextStyle(wheel.getCurrentItem());
                initDays(DateUtil.getMaxDayByMonthAndMonth(mCurrentYear, mCurrentMonth + 1));
                setDayView();
            }
        });
    }

    private void setDayView() {
        mDayAdapter = new DateAdapter(this, mDayDatas, mCurrentDay - 1);
        view_day.setVisibleItems(5);
        view_day.setViewAdapter(mDayAdapter);
        view_day.setCurrentItem(mCurrentDay - 1);
        view_day.addChangingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String day = mDayAdapter.updateTextStyle(newValue);
                if (!TextUtils.isEmpty(day)) {
                    mCurrentDay = Integer.parseInt(day);
                }
                updateResult();
            }
        });
        view_day.addScrollingListener(new WheelViewAdapter.OnWheelClickedListener.OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mDayAdapter.updateTextStyle(wheel.getCurrentItem());
            }
        });
    }

    private void updateResult() {
        tv_result.setText(mCurrentYear + "-" + (mCurrentMonth + 1) + "-" + mCurrentDay);
    }

    ///////////////////////////////////////////////////////////////////////////
    // generate date
    ///////////////////////////////////////////////////////////////////////////
    private void initData() {
        mYearDatas = new ArrayList<>();
        mMonthDatas = new ArrayList<>();
        for (int i = DateUtil.MIN_START_YEAR; i < 2100; i++) {
            mYearDatas.add(i + "");
        }

        for (int i = 1; i <= 12; i++) {
            mMonthDatas.add(i + "");
        }
        Log.e(TAG, "initData: mCurrentMonth = " + mCurrentMonth);
        initDays(DateUtil.getMaxDayByMonthAndMonth(mCurrentYear, mCurrentMonth + 1));

    }

    private void initDays(int maxDay) {
        Log.e(TAG, "initDays: maxDay = " + maxDay);
        if (null == mDayDatas) {
            mDayDatas = new ArrayList<>();
        } else {
            mDayDatas.clear();
        }
        for (int i = 1; i <= maxDay; i++) {
            mDayDatas.add(i + "");
        }
        mCurrentDay = Math.min(mCurrentDay, maxDay);
    }

}
