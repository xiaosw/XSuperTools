package com.xiaosw.library.utils;

/**
 * <p><br/>ClassName : {@link PathPoint}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-07-28 17:17:05</p>
 */

public class PathPoint {

    private float x, x1, x2;
    private float y, y1, y2;
    private Operation mOperation;

    public PathPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private PathPoint(float x, float y, Operation operation) {
        this.x = x;
        this.y = y;
        this.mOperation = operation;
    }

    private PathPoint(float x, float x1, float x2, float y, float y1, float y2,
                      Operation operation) {
        this.x = x;
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
        this.y1 = y1;
        this.y2 = y2;
        this.mOperation = operation;
    }

    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(x, y, Operation.MOVE);
    }

    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(x, y, Operation.LINE);
    }

    public static PathPoint quadTo(float x, float y, int x1, int y1, float x2, float y2) {
        return new PathPoint(x, x1, x2, y, y1, y2, Operation.QUAD);
    }

    public float getX() {
        return x;
    }

    public float getX1() {
        return x1;
    }

    public float getX2() {
        return x2;
    }

    public float getY() {
        return y;
    }

    public float getY1() {
        return y1;
    }

    public float getY2() {
        return y2;
    }

    public Operation getOperation() {
        return mOperation;
    }

    public enum Operation {
        MOVE,
        LINE,
        QUAD
    }

}
