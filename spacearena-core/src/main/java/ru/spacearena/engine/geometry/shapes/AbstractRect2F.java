package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public abstract class AbstractRect2F extends AbstractPolyShape2F implements Rect2F {

    public float getPointX(int i) {
        return i / 2 % 2 == 0 ? getMinX() : getMaxX();
    }

    public float getPointY(int i) {
        return (i+1) / 2 % 2 == 0 ? getMinY() : getMaxY();
    }

    public int getPointCount() {
        return 4;
    }

    public ShapeType getType() {
        return ShapeType.RECTANGLE;
    }

    public void computeBoundingBox(PolyShape2F shape) {
        final int pc = shape.getPointCount();
        if (pc < 1) {
            throw new IllegalArgumentException("Shape must have at least one point");
        }

        float minX = shape.getPointX(0), minY = shape.getPointY(0), maxX = minX, maxY = minY;
        for (int i=1; i<pc; i++) {
            final float x = shape.getPointX(i);
            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }

            final float y = shape.getPointY(i);
            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
        }
        setBounds(minX, minY, maxX, maxY);
    }

//    @Override
//    public boolean obtainAxis(int n, boolean reference, Shape2F shape, Point2F axis) {
//        if (shape.getType() == ShapeType.RECTANGLE && !reference) {
//            return false;
//        }
//        switch (n) {
//        case 0:
//            axis.set(1,0);
//            return true;
//        case 1:
//            axis.set(0,1);
//            return true;
//        default:
//            return false;
//        }
//    }

}
