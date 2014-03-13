package ru.spacearena.engine.collisions;

import ru.spacearena.engine.common.PhysicalObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.geometry.primitives.ProjectionF;
import ru.spacearena.engine.util.ShapeUtils;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class CollisionObject extends PhysicalObject {

    private final Rect2FPP localSpaceRect = new Rect2FPP();
    private final Rect2FPP worldSpaceRect = new Rect2FPP();
    private final Shape2F[] worldSpaceShapes = new Shape2F[getConvexShapeCount()];

    public Shape2F getConvexShape(int i) {
        return worldSpaceShapes[i];
    }

    public int getConvexShapeCount() {
        return worldSpaceShapes.length;
    }

    public CollisionObject() {
        //preComputeAABB(localSpaceRect, shapes);
    }

    private void preComputeAABB(Rect2FPP box, Shape2F[] shapes) {
        final Point2F axisX = new Point2F(1,0), axisY = new Point2F(0,1);
        final ProjectionF projectionX = new ProjectionF(), projectionY = new ProjectionF();
        for (final Shape2F shape : shapes) {
            shape.calculateProjection(axisX, projectionX);
            shape.calculateProjection(axisY, projectionY);
        }
        box.set(projectionX.min, projectionY.min, projectionX.max, projectionY.max);
    }

    @Override
    protected void onMatrixUpdate() {


    }

    public void computeBoundingBox(float seconds) {
        ShapeUtils.computeBoundingBox(localSpaceRect, worldSpaceRect, getWorldSpace());
        worldSpaceRect.extend(getVelocityX() * seconds, getVelocityY() * seconds);
    }

    public BoundingBox2F getAABB() {
        updateMatricesIfNeeded();
        return worldSpaceRect;
    }

    public boolean onCollision(CollisionObject object, float seconds, boolean reference, Contact contact) {
        applyVelocity(seconds);
        return true;
    }

    public boolean canCollide(CollisionObject object) {
        return false;
    }

    @Override
    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        computeBoundingBox(seconds);
        return true;
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        drawAABB(context);
        drawShapes(context);
    }

    private void drawAABB(DrawContext context) {
        if (!engine.getDebug().isDrawBounds()) {
            return;
        }
        final BoundingBox2F b = getAABB();
        context.setColor(Color.RED);
        context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }

    private void drawShapes(DrawContext context) {
        if (!engine.getDebug().isDrawConvexShapes()) {
            return;
        }
        final float lw = context.getLineWidth();
        try {
            context.setLineWidth(3f);
            context.setColor(Color.BLUE);
            for (Shape2F shape: worldSpaceShapes) {
                shape.stroke(context);
            }
        } finally {
            context.setLineWidth(lw);
        }
    }


}
