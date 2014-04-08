package ru.spacearena.engine.graphics.font.df;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class Grid {

    public static final int P = -1;
    public static final float D = Float.POSITIVE_INFINITY;

    private Point[] values;
    private int w, h;
    private float minDistance, maxDistance;

    public Grid(int w, int h) {
        this.w = w;
        this.h = h;

        final int l = w * h;
        this.values = new Point[l];
        for (int i=0; i<l; i++) {
            this.values[i] = new Point(D, P,P);
        }
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public Point get(int x, int y) {
        return this.values[y*w+x];
    }

    public boolean inRange(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    public float getDistance(int x, int y) {
        return inRange(x,y) ? get(x,y).d : D;
    }

    public int getX(int x, int y) {
        return inRange(x,y) ? get(x,y).x : P;
    }

    public int getY(int x, int y) {
        return inRange(x,y) ? get(x,y).y : P;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
}
