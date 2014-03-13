package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Polygon2FP extends AbstractPolyShape2F {

    private float[] points;
    private int start;
    private int count;

    public Polygon2FP(float[] points, int start, int count) {
        this.points = points;
        this.start = start;
        this.count = count;
    }

    @Override
    public void getPoints(float[] points, int start, int pointCount) {
        System.arraycopy(this.points, this.start, points, start, pointCount*2);
    }

    public float getPointX(int i) {
        return points[i*2];
    }

    public float getPointY(int i) {
        return points[i*2+1];
    }

    public int getPointCount() {
        return count;
    }
}
