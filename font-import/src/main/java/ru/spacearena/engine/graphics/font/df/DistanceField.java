package ru.spacearena.engine.graphics.font.df;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class DistanceField {

    public static final int DEFAULT_BORDER = -1;
    public static final float DEFAULT_DISTANCE = Float.POSITIVE_INFINITY;

    private final float[] distance;
    private final int[] borderX;
    private final int[] borderY;
    private final int w, h;

    public DistanceField(int w, int h) {
        this.w = w;
        this.h = h;

        final int l = w * h;
        this.distance = new float[l];
        this.borderX = new int[l];
        this.borderY = new int[l];
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public boolean inRange(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    public int getIndex(int x, int y) {
        return y * w + x;
    }

    public float getDistance(int x, int y) {
        return inRange(x,y) ? distance[getIndex(x,y)] : DEFAULT_DISTANCE;
    }

    public int getBorderX(int x, int y) {
        return inRange(x,y) ? borderX[getIndex(x,y)] : DEFAULT_BORDER;
    }

    public int getBorderY(int x, int y) {
        return inRange(x,y) ? borderY[getIndex(x,y)] : DEFAULT_BORDER;
    }

    public void set(int x, int y, int borderX, int borderY, float distance) {
        final int i = getIndex(x,y);
        this.distance[i] = distance;
        this.borderX[i] = borderX;
        this.borderY[i] = borderY;
    }

    public void setDistance(int x, int y, float distance) {
        this.distance[getIndex(x,y)] = distance;
    }

    public void setBorder(int x, int y, int borderX, int borderY) {
        final int i = getIndex(x,y);
        this.borderX[i] = borderX;
        this.borderY[i] = borderY;
    }

    public void negateDistance(int x, int y) {
        final int i = getIndex(x,y);
        this.distance[i] = -this.distance[i];
    }

}
