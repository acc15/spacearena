package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class Polygon2F extends AbstractPolyShape2F {

    private float[] points;
    private int start;
    private int count;

    private int computeOffset(int i) {
        return start + i * 2;
    }

    public Polygon2F(float[] points, int start, int count) {
        this.points = points;
        this.start = start;
        this.count = count;
    }

    public void setPoint(int i, float x, float y) {
        if (i >= count) {
            throw new IndexOutOfBoundsException();
        }
        final int off = computeOffset(i);
        this.points[off] = x;
        this.points[off+1] = y;
    }

    public ShapeType getType() {
        return ShapeType.POLYGON;
    }

    @Override
    public void getPoints(float[] points, int start, int pointCount) {
        System.arraycopy(this.points, this.start, points, start, pointCount*2);
    }

    public float getPointX(int i) {
        return points[i*2+start];
    }

    public float getPointY(int i) {
        return points[i*2+start+1];
    }

    public int getPointCount() {
        return count;
    }
}
