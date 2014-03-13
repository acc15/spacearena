package ru.spacearena.engine.collisions;

import ru.spacearena.engine.common.PhysicalObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public abstract class AbstractCollisionObject extends PhysicalObject implements CollisionEntity {

    private final float[][] shapePoints = new float[getConvexShapeCount()][];

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        /*
        if (engine.getDebug().isDrawBounds()) {
            final Bounds b = getAABB();
            context.setColor(Color.RED);
            context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
        }*/
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
    protected void onMatrixUpdate() {
        for (int i=0; i<getConvexShapeCount(); i++) {
            final float[] points = getConvexShape(i);
            final float[] txPoints = new float[points.length];
            System.arraycopy(points, 0, txPoints, 0, points.length);
            getWorldSpace().mapPoints(txPoints);
            shapePoints[i] = txPoints;
        }
    }

    public float[] getTransformedShape(int n) {
        updateMatricesIfNeeded();
        return shapePoints[n];
    }

    public float[] getConvexShape(int n) {
        throw new IndexOutOfBoundsException("no more convex shapes");
    }

    public int getConvexShapeCount() {
        return 0;
    }
}
