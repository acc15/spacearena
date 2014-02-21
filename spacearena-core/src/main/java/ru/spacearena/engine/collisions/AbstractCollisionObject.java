package ru.spacearena.engine.collisions;

import ru.spacearena.engine.common.PhysicalObject;
import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public abstract class AbstractCollisionObject extends PhysicalObject implements CollisionContainer.CollisionEntity {

    private final AABB transformedBounds = new AABB();
    private final float[] aabbPoints = new float[8];
    private final float[][] shapePoints = new float[getConvexShapeCount()][];

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        if (engine.getDebug().isDrawBounds()) {
            final Bounds b = getAABB();
            context.setColor(Color.RED);
            context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
        }
        if (engine.getDebug().isDrawConvexShapes()) {
            final float lw = context.getLineWidth();
            try {
                context.setLineWidth(3f);
                for (int i=0; i<getConvexShapeCount(); i++) {
                    final float[] points = getTransformedShape(i);
                    context.setColor(Color.BLUE);
                    context.drawPoly(points);
                }
            } finally {
                context.setLineWidth(lw);
            }
        }

    }

    @Override
    protected void calculateViewMatrix(Matrix matrix) {
        super.calculateViewMatrix(matrix);
        final Bounds b = getOriginalBounds();
        ShapeUtils.fillRect(aabbPoints, b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
        matrix.mapPoints(aabbPoints);
        transformedBounds.calculate(aabbPoints);
        for (int i=0; i<getConvexShapeCount(); i++) {
            final float[] points = getConvexShape(i);
            final float[] txPoints = new float[points.length];
            System.arraycopy(points, 0, txPoints, 0, points.length);
            matrix.mapPoints(txPoints);
            shapePoints[i] = txPoints;
        }
    }

    public float[] getTransformedShape(int n) {
        updateMatrices();
        return shapePoints[n];
    }

    public float[] getConvexShape(int n) {
        throw new IndexOutOfBoundsException("no more convex shapes");
    }

    public int getConvexShapeCount() {
        return 0;
    }

    public abstract Bounds getOriginalBounds();

    public Bounds getAABB() {
        return transformedBounds;
    }
}
