package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.ShapeUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dBody extends EngineObject {

    private Body body;

    public Box2dBody(Body body) {
        this.body = body;
    }

    public float getPositionX() {
        return body.getPosition().x;
    }

    public float getPositionY() {
        return body.getPosition().y;
    }

    public float getVelocityX() {
        return body.getLinearVelocity().x;
    }

    public float getVelocityY() {
        return body.getLinearVelocity().y;
    }

    public float getRotation() {
        return body.getAngle();
    }

    private void drawEdge(DrawContext context, EdgeShape edgeShape) {
        final Vec2 from = body.getWorldPoint(edgeShape.m_vertex1);
        final Vec2 to = body.getWorldPoint(edgeShape.m_vertex2);
        context.drawLine(from.x, from.y, to.x, to.y);
    }

    @Override
    public void onDraw(DrawContext context) {
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            final Shape shape = f.getShape();

            context.setColor(Color.GREEN);
            switch (shape.getType()) {
            case EDGE:
                drawEdge(context, (EdgeShape)shape);
                break;

            case CIRCLE:
                final CircleShape circle = (CircleShape) shape;
                final Vec2 center = body.getWorldPoint(circle.getVertex(0));
                context.drawCircle(center.x, center.y, circle.getRadius());
                break;

            case POLYGON:
                final PolygonShape polygon = (PolygonShape) shape;
                final float[] buf = ShapeUtils.POINT_BUF;
                final int pointCount = polygon.getVertexCount();
                for (int i=0; i < pointCount; i++) {
                    final Vec2 vertex = body.getWorldPoint(polygon.getVertex(i));
                    buf[i*2] = vertex.x;
                    buf[i*2+1] = vertex.y;
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
