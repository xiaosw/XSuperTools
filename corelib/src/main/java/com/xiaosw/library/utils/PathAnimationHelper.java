package com.xiaosw.library.utils;

import java.util.ArrayList;

/**
 * <p><br/>ClassName : {@link PathAnimationHelper}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-07-28 17:17:03</p>
 */

public class PathAnimationHelper {

    ArrayList<PathPoint> mPathPoints;

    public PathAnimationHelper() {
        mPathPoints = new ArrayList<>();
    }

    public void moveTo(float x, float y) {
        mPathPoints.add(PathPoint.moveTo(x, y));
    }

    public void lineTo(float x, float y) {
        mPathPoints.add(PathPoint.lineTo(x, y));
    }

    public void quadTo(float x, float y, int x1, int y1, float x2, float y2) {
        mPathPoints.add(PathPoint.quadTo(x, y, x1, y1, x2, y2));
    }

    public ArrayList<PathPoint> getPathPoints() {
        return mPathPoints;
    }

    public void reset() {
        mPathPoints.clear();
    }

}
