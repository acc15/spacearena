package ru.spacearena.engine.common;

import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class Transform extends AbstractTransformation<Transform> {

    public void applyTransformations(Matrix matrix) {
        matrix.translate(x, y);
        matrix.rotate(rotation);
        matrix.skew(skewX, skewY);
        matrix.scale(scaleX, scaleY);
        matrix.translate(-pivotX, -pivotY);
    }

}
