package com.xiaosw.library.utils;

import android.animation.TypeEvaluator;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * <p><br/>ClassName : {@link PointEvaluate}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-07-28 17:17:20</p>
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class PointEvaluate implements TypeEvaluator<PathPoint> {
    
    /** @see PointEvaluate#getClass().getSimpleName() */
    private static final String TAG = "xiaosw-PointEvaluate";
    
    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
        float x = 0;
        float y = 0;
        switch (endValue.getOperation()) {
            case MOVE:
                x = startValue.getX() + endValue.getX();
                y = startValue.getY() + endValue.getY();
                break;

            case LINE:
                x = startValue.getX() + (endValue.getX() - startValue.getX()) * fraction;
                y = startValue.getY() +(endValue.getY() - startValue.getY()) * fraction;
                break;

            case QUAD:
                x = (float) (Math.pow((1 - fraction), 2) * endValue.getX()
                    + 2 * fraction * (1 - fraction) * endValue.getX1()
                    + Math.pow(fraction, 2) * endValue.getX2());

                y = (float) (Math.pow((1 - fraction), 2) * endValue.getY()
                    + 2 * fraction * (1 - fraction) * endValue.getY1()
                    + Math.pow(fraction, 2) * endValue.getY2());
                break;

            default:
                // TODO: 2017/7/28
        }
        return new PathPoint(x, y);
    }
}
