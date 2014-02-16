package ru.spacearena.java2d.engine;

import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.util.FloatMathUtils;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DMatrix implements Matrix {

    final AffineTransform affineTransform;

    public Java2DMatrix() {
        affineTransform = new AffineTransform();
    }

    public Java2DMatrix(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    public void identity() {
        affineTransform.setToIdentity();
    }

    public void multiply(Matrix transform) {
        affineTransform.concatenate(((Java2DMatrix)transform).affineTransform);
    }

    public void translate(float x, float y) {
        affineTransform.translate(x, y);
    }

    public void rotate(float degrees) {
        affineTransform.rotate(FloatMathUtils.toRadians(degrees));
    }

    public void scale(float x, float y) {
        affineTransform.scale(x, y);
    }

    public void skew(float x, float y) {
        affineTransform.shear(x, y);
    }

    public void mapPoints(float[] pts) {
        affineTransform.transform(pts, 0, pts, 0, pts.length/2);
    }

    public boolean inverse(Matrix matrix) {
        final AffineTransform anotherTransform = ((Java2DMatrix)matrix).affineTransform;
        if (anotherTransform != affineTransform) {
            affineTransform.setTransform(anotherTransform);
        }
        try {
            affineTransform.invert();
        } catch (NoninvertibleTransformException e) {
            return false;
        }
        return true;
    }

    public boolean isIdentity() {
        return affineTransform.isIdentity();
    }
}
