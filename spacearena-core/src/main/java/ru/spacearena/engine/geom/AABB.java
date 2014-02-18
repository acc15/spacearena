package ru.spacearena.engine.geom;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class AABB implements Bounds {

    public float minX, maxX, minY, maxY;

    public AABB() {
    }

    public AABB(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void setX(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }

    public void setY(float minY, float maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    public void set(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getCenterX() {
        return minX + getWidth()/2;
    }

    public float getCenterY() {
        return minY + getHeight()/2;
    }

    public float getWidth() {
        return maxX - minX;
    }

    public float getHeight() {
        return maxY - minY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void calculate(float[] points) {
        minX = points[0];
        maxX = points[0];
        minY = points[1];
        maxY = points[1];
        for (int i=2;i<points.length;i+=2) {
            final float x = points[i], y = points[i+1];
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
    }

    public void scale(float scaleX, float scaleY) {
        final float cx = getWidth(), cy = getHeight();
        inflate((cx*scaleX-cx)/2, (cy*scaleY-cy)/2);
    }

    public void inflate(float dx, float dy) {
        this.minX -= dx;
        this.maxX += dx;
        this.minY -= dy;
        this.maxY += dy;
    }

}
