package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils
;

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

    public void multiply(Matrix transform) {
        affineTransform.concatenate(((Java2DMatrix)transform).affineTransform);
    }

    public void mapPoints(float[] pts) {
        affineTransform.transform(pts, 0, pts, 0, pts.length/2);
    }

    public void mapPoints(float[] dst, int dstOffset, float[] src, int srcOffset, int pointCount) {
        affineTransform.transform(src, srcOffset, dst, dstOffset, pointCount);
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

    public void set(
            float pivotX, float pivotY,
            float scaleX, float scaleY,
            float skewX, float skewY,
            float rotation,
            float x, float y) {
        affineTransform.setToIdentity();
        affineTransform.translate(x, y);
        affineTransform.rotate(FloatMathUtils.toRadians(rotation));
        affineTransform.shear(skewX, skewY);
        affineTransform.scale(scaleX, scaleY);
        affineTransform.translate(-pivotX, -pivotY);
    }
}
