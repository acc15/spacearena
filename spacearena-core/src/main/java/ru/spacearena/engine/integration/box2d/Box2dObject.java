package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.common.GenericContainer;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dObject extends GenericContainer {

    private final List<Body> bodies = new ArrayList<Body>();

    private Matrix matrix;

    private float pivotX = 0f, pivotY = 0f;
    private float scaleX = 1f, scaleY = 1f;
    private float skewX = 0f, skewY = 0f;

    public void addBody(Body body) {
        this.bodies.add(body);
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
    }

    public void setPivot(float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public float getSkewX() {
        return skewX;
    }

    public void setSkewX(float skewX) {
        this.skewX = skewX;
    }

    public float getSkewY() {
        return skewY;
    }

    public void setSkewY(float skewY) {
        this.skewY = skewY;
    }

    public void setSkew(float skewX, float skewY) {
        this.skewX = skewX;
        this.skewY = skewY;
    }

    public void onCreate(World world) {
    }

    @Override
    public void onInit(Engine engine) {
        matrix = engine.createMatrix();
    }

    @Override
    public void onDraw(DrawContext context) {
        drawBodies(context, bodies);
    }

    protected void drawBodies(DrawContext context, List<Body> bodies) {
        for (final Body body: bodies) {
            drawBody(context, body);
        }
    }

    protected void drawBody(DrawContext context, Body body) {
        final org.jbox2d.common.Transform xf = body.getTransform();
        matrix.set(pivotX, pivotY, scaleX, scaleY, skewX, skewY, xf.q.c, xf.q.s, xf.p.x, xf.p.y);
        context.pushMatrix(matrix);
        try {
            onDrawBody(context, body);
        } finally {
            context.popMatrix();
        }
    }

    protected void onDrawBody(DrawContext context, Body body) {
        drawBodyShapes(context, body);
    }

    protected final void drawBodyShapes(DrawContext context, Body body) {
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            final Shape shape = f.getShape();
            drawShape(context, shape);
        }
    }

    protected final void drawShape(DrawContext context, Shape shape) {
        switch (shape.getType()) {
        case EDGE:
            drawEdge(context, (EdgeShape)shape);
            break;

        case CIRCLE:
            final CircleShape circle = (CircleShape) shape;
            final Vec2 pos = circle.getVertex(0);
            final float radius = circle.getRadius();
            context.drawCircle(pos.x, pos.y, radius);
            context.drawLine(pos.x, pos.y, pos.x + radius, pos.y);
            break;

        case POLYGON:
            final PolygonShape polygon = (PolygonShape) shape;
            final float[] buf = ShapeUtils.POINT_BUF;
            final int pointCount = polygon.getVertexCount();
            for (int i=0; i < pointCount; i++) {
                final Vec2 v = polygon.getVertex(i);
                buf[i*2] = v.x;
                buf[i*2+1] = v.y;
            }
            context.drawPoly(buf, 0, pointCount);
            break;

        case CHAIN:
            final ChainShape chain = (ChainShape) shape;
            final EdgeShape edgeShape = new EdgeShape();
            for (int i=0; i<chain.getChildCount(); i++) {
                chain.getChildEdge(edgeShape, i);
                drawEdge(context, edgeShape);
            }
        }
    }

    private void drawEdge(DrawContext context, EdgeShape edgeShape) {
        context.drawLine(edgeShape.m_vertex1.x, edgeShape.m_vertex1.y, edgeShape.m_vertex2.x, edgeShape.m_vertex2.y);
    }
}
