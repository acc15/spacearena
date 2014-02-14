package ru.spacearena.engine.common;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-02
 */
public abstract class AbstractTransformation extends EngineContainer<EngineObject> {

    private Matrix matrix;
    private boolean isDirty = false;

    protected abstract void applyTransformations(Matrix matrix);

    public void markDirty() {
        this.isDirty = true;
    }

    public Matrix getMatrix() {
        if (!isDirty) {
            return matrix;
        }
        matrix.identity();
        applyTransformations(matrix);
        isDirty = false;
        return matrix;
    }

    public void mapPoints(float[] pts) {
        getMatrix().mapPoints(pts);
    }

    public void onInit(Engine engine) {
        this.matrix = engine.createMatrix();
    }

    @Override
    public void onDraw(DrawContext context) {
        final Matrix thisMatrix = getMatrix();
        if (thisMatrix.isIdentity()) {
            super.onDraw(context);
            return;
        }
        final Matrix oldMatrix = context.getMatrixCopy();
        final Matrix concatMatrix = context.getMatrixCopy();
        concatMatrix.multiply(thisMatrix);
        try {
            context.setMatrix(concatMatrix);
            super.onDraw(context);
        } finally {
            context.setMatrix(oldMatrix);
        }
    }

}
