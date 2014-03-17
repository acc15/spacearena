package ru.spacearena.engine.geometry.shapes;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public abstract class AbstractRect2F extends AbstractPolyShape2F implements BoundingBox2F {

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

    @Override
    public boolean obtainAxis(int n, boolean reference, Shape2F shape, Point2F axis) {
        if (shape.getType() == ShapeType.RECTANGLE && !reference) {
            return false;
        }
        switch (n) {
        case 0:
            axis.set(1,0);
            return true;
        case 1:
            axis.set(0,1);
            return true;
        default:
            return false;
        }
    }

    @Override
    public void stroke(DrawContext drawContext) {
        drawContext.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }

    @Override
    public void fill(DrawContext drawContext) {
        drawContext.fillRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }
}