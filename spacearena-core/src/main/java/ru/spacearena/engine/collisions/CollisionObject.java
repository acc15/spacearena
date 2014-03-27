package ru.spacearena.engine.collisions;

import ru.spacearena.engine.common.PhysicalObject;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.primitives.ProjectionF;
import ru.spacearena.engine.geometry.shapes.*;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-03
 */
public class CollisionObject extends PhysicalObject {

    private final Rect2FPP localSpaceRect = new Rect2FPP();
    private final Rect2FPP worldSpaceRect = new Rect2FPP();
    private final List<Shape2F> shapes = new ArrayList<Shape2F>();
    private final List<Shape2F> worldShapes = new ArrayList<Shape2F>();

    public void addShape(Shape2F shape) {
        this.shapes.add(shape);
    }

    public void setShape(int i, Shape2F shape) {
        this.shapes.set(i, shape);
    }

    public void removeShape(int i) {
        this.shapes.remove(i);
        if (i < this.worldShapes.size()) {
            this.worldShapes.remove(i);
        }
    }

    public void removeAllShapes() {
        this.shapes.clear();
        this.worldShapes.clear();
    }

    public Shape2F getShape(int i) { return shapes.get(i); }

    public Shape2F getWorldShape(int i) {
        if (i >= shapes.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (i >= worldShapes.size()) {
            for (int j=worldShapes.size(); j<i; j++) {
                worldShapes.add(null);
            }
            final Shape2F worldShape = computeWorldShape(shapes.get(i), null);
            worldShapes.add(worldShape);
            return worldShape;
        }

        final Shape2F shape = worldShapes.get(i);
        if (shape == null) {
            final Shape2F worldShape = computeWorldShape(shapes.get(i), null);
            worldShapes.set(i, worldShape);
            return worldShape;
        }
        return shape;
    }

    public int getShapeCount() {
        return shapes.size();
    }

    public Shape2F computeWorldShape(Shape2F localSpaceShape, Shape2F worldSpaceShape) {
        final ShapeType type = localSpaceShape.getType();
        switch (type) {
        case RECTANGLE:
        case POLYGON:

            final PolyShape2F polyShape = (PolyShape2F) localSpaceShape;
            final int pointCount = polyShape.getPointCount();




        default:
            throw new UnsupportedOperationException("Unsupported shape type: " + type);
        }
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

        // invalidating all computed shapes
        for (int i=0; i<worldShapes.size(); i++) {
            final Shape2F worldShape = worldShapes.get(i);
            if (worldShape != null) {
                worldShapes.set(i, computeWorldShape(shapes.get(i), worldShape));
            }
        }
    }

    public void computeBoundingBox(float seconds) {
        ShapeUtils.computeBoundingBox(localSpaceRect, worldSpaceRect, getWorldSpace());
        worldSpaceRect.extend(getVelocityX() * seconds, getVelocityY() * seconds);
    }

    public BoundingBox2F getAABB() {
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
        if (!getEngine().getDebug().isDrawBounds()) {
            return;
        }
        final BoundingBox2F b = getAABB();
        context.setColor(Color.RED);
        context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }

    private void drawShapes(DrawContext context) {
        if (!getEngine().getDebug().isDrawConvexShapes()) {
            return;
        }
        final float lw = context.getLineWidth();
        try {
            context.setLineWidth(3f);
            context.setColor(Color.BLUE);
            for (int i=0; i<getShapeCount(); i++) {
                getWorldShape(i).stroke(context);
            }
        } finally {
            context.setLineWidth(lw);
        }
    }


}
