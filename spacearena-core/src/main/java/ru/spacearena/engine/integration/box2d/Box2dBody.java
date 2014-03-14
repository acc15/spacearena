package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import ru.spacearena.engine.EngineObject;
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

    @Override
    public void onDraw(DrawContext context) {
        final Fixture f = body.getFixtureList();
        while (f != null) {

            final Shape shape = f.getShape();
            switch (shape.getType()) {
            case EDGE:
                final EdgeShape edge = (EdgeShape) shape;
                context.drawLine(edge.m_vertex1.x, edge.m_vertex1.y, edge.m_vertex2.x, edge.m_vertex2.y);
                break;

            case CIRCLE:
                final CircleShape circle = (CircleShape) shape;
                context.drawCircle(circle.m_p.x, circle.m_p.y, circle.m_radius);
                break;

            case POLYGON:
                final PolygonShape polygon = (PolygonShape) shape;

                final float[] buf = ShapeUtils.POINT_BUF;
                final int pointCount = polygon.getVertexCount();
                for (int i=0; i < pointCount; i++) {
                    final Vec2 vertex = polygon.getVertex(i);
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
                    context.drawLine(edgeShape.m_vertex1.x, edgeShape.m_vertex1.y,
                            edgeShape.m_vertex2.x, edgeShape.m_vertex2.y);
                }
            }

        }
    }
}
