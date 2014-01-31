package ru.spacearena.swing;

import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.primitives.Point2F;

import java.awt.geom.AffineTransform;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class Java2DMatrix implements Matrix {
    private AffineTransform transform;

    public Java2DMatrix() {
        this.transform = new AffineTransform();
    }

    public Java2DMatrix(AffineTransform transform) {
        this.transform = transform;
    }

    public <T> T getNativeMatrix(Class<T> clazz) {
        if (AffineTransform.class.isAssignableFrom(clazz)) {
            return (T) transform;
        }
        throw new IllegalArgumentException();
    }

    public Matrix translate(Point2F pt) {
        transform.translate(pt.x, pt.y);
        return this;
    }

    public Matrix rotate(float degrees) {
        transform.rotate(Math.toRadians(degrees));
        return this;
    }

    public Matrix scale(Point2F pt) {
        transform.scale(pt.x, pt.y);
        return this;
    }
}
