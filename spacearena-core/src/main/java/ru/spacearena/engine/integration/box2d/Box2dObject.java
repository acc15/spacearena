package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.common.GenericContainer;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dObject extends GenericContainer {

    private Matrix matrix;
    private Body body;

    private final BodyDef bodyDef = new BodyDef();

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public Body getBody() {
        return body;
    }

    public float getAngle() {
        return body.getAngle();
    }

    public float getPositionX() {
        return body.getPosition().x;
    }

    public float getPositionY() {
        return body.getPosition().y;
    }

    public void onCreate(Box2dWorld world) {
        onPreCreate(bodyDef);
        this.body = world.getWorld().createBody(bodyDef);
        onPostCreate(this.body);
    }

    protected void onPreCreate(BodyDef bodyDef) {
    }

    protected void onPostCreate(Body body) {
    }

    @Override
    public void onAttach(Engine engine) {
        super.onAttach(engine);
        matrix = engine.createMatrix();
    }

    @Override
    public void onDraw(DrawContext context) {
        org.jbox2d.common.Transform tx = body.getTransform();
        matrix.set(tx.q.c, tx.q.s, tx.p.x, tx.p.y);
        try {
            context.pushMatrix(matrix);
            onDrawTransformed(context);
        } finally {
            context.popMatrix();
        }
    }

    protected void onDrawTransformed(DrawContext context) {
        super.onDraw(context);
        if (getEngine().getDebug().isDrawConvexShapes()) {
            context.setColor(Color.GREEN);
            drawBodyShapes(context, body);
        }
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

    public void setInitialPosition(float x, float y) {
        bodyDef.position.set(x, y);
    }
}
