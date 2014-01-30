package ru.spacearena.engine.primitives;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Matrix2F {

    private float[] values;

    public Matrix2F() {
    }

    public Matrix2F(float[] values) {
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }

    public Matrix2F scale(Point2F pt) {
        // TODO implement..
        throw new Error("Not implemented ...");
    }

    public Matrix2F rotate(float degrees, Point2F pivot) {
        // TODO implement..
        throw new Error("Not implemented ...");
    }

    public Matrix2F translate(Point2F pt) {
        // TODO implement..
        throw new Error("Not implemented ...");
    }
}
