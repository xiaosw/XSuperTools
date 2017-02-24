package com.xiaosw.datepicker;

/**
 * <p><br/>ClassName : {@link DateUtil}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-02-24 18:18:09</p>
 */
public class DateUtil {

    /**
     * @see DateUtil#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-DateUtil";

    public static final int MIN_START_YEAR = 1900;

    /**
     * 根据当前年月获取当前月日数
     * @param year
     * @param month
     * @return
     */
    public static int getMaxDayByMonthAndMonth(int year, int month) {
        int maxDay = 30;
        boolean leayYear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayYear = true;
        } else {
            leayYear = false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxDay = 31;
                break;

            case 2:
                if (leayYear) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxDay = 30;
                break;
            default:
                break;
        }
        return maxDay;
    }


}
