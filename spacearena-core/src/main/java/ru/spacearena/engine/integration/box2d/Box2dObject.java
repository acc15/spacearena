package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dObject extends EngineObject {

    private final List<Body> bodyList = new ArrayList<Body>();
    private int color = Color.WHITE;

    private Matrix matrix;

    private void drawEdge(DrawContext context, EdgeShape edgeShape) {
        context.drawLine(edgeShape.m_vertex1.x, edgeShape.m_vertex1.y, edgeShape.m_vertex2.x, edgeShape.m_vertex2.y);
    }

    @Override
    public void onInit(Engine engine) {
        matrix = engine.createMatrix();
    }

    @Override
    public void onDraw(DrawContext context) {
        context.setColor(color);
        for (Body body: bodyList) {
            final org.jbox2d.common.Transform xf = body.getTransform();
            matrix.set(0f, 0f, 1f, 1f, 0f, 0f, xf.q.c, xf.q.s, xf.p.x, xf.p.y);
            context.pushMatrix(matrix);
            try {
                drawBody(context, body);
            } finally {
                context.popMatrix();
            }
        }
    }

    public void addBody(Body body) {
        if (body.getUserData() != null) {
            throw new IllegalArgumentException("Body user data must be null");
        }
        body.setUserData(this);
        bodyList.add(body);
    }

    public void removeBody(Body body) {
        if (bodyList.remove(body)) {
            body.setUserData(null);
        }
    }

    protected void drawBody(DrawContext context, Body body) {
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            final Shape shape = f.getShape();
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
    }

}
