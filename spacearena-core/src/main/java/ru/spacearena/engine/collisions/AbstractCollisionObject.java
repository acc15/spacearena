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

    private final AABB transform = new AABB();
    private final float[] aabbPoints = new float[8];

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        final Bounds b = getAABB();
        context.setColor(Color.RED);
        context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }

    @Override
    protected void calculateViewMatrix(Matrix matrix) {
        super.calculateViewMatrix(matrix);

        final Bounds b = getOriginalBounds();
        ShapeUtils.fillRect(aabbPoints, b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
        matrix.mapPoints(aabbPoints);
        transform.calculate(aabbPoints);
    }

    public abstract Bounds getOriginalBounds();

    public Bounds getAABB() {
        return transform;
    }
}
