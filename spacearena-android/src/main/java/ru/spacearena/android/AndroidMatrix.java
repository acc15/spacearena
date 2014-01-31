package ru.spacearena.android;

import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.primitives.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class AndroidMatrix implements Matrix {

    private android.graphics.Matrix matrix;

    public AndroidMatrix() {
        this.matrix = new android.graphics.Matrix();
    }

    public AndroidMatrix(android.graphics.Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix translate(Point2F pt) {
        matrix.setTranslate(pt.x, pt.y);
        return this;
    }

    public Matrix rotate(float degrees) {
        matrix.setRotate(degrees);
        return this;
    }

    public Matrix scale(Point2F pt) {
        matrix.setScale(pt.x, pt.y);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getNativeMatrix(Class<T> clazz) {
        if (android.graphics.Matrix.class.isAssignableFrom(clazz)) {
            return (T)matrix;
        }
        throw new IllegalArgumentException();
    }
}
