package ru.spacearena.engine.integration.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.common.GenericContainer;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dObject extends GenericContainer {

    private boolean live = true;
    private Matrix matrix;
    private Body body;

    private final BodyDef bodyDef = new BodyDef();

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public Body getBody() {
        return body;
    }

    public Transform getTransform() {
        return body.getTransform();
    }

    public float getAngle() {
        return body.getAngle();
    }

    public void markDead() {
        this.live = false;
    }

    public void setAngle(float radians) {
        body.setTransform(body.getPosition(), radians);
    }

    public float getPositionX() {
        return body.getPosition().x;
    }

    public float getPositionY() {
        return body.getPosition().y;
    }

    public float getVelocityX() { return body.getLinearVelocity().x; }

    public float getVelocityY() { return body.getLinearVelocity().y; }

    public void setPositionX(float x) {
        setPosition(x, getPositionY());
    }

    public void setPositionY(float y) {
        setPosition(getPositionX(), y);
    }

    public void setPosition(float x, float y) {
        body.setTransform(Box2dUtils.tempVec(x, y), body.getAngle());
    }

    public void onCreate(Box2dWorld world) {
        onPreCreate(bodyDef);
        bodyDef.userData = this;
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

    public Point2F mapPoint(Point2F pt, Point2F out) {
        final Vec2 v = Box2dUtils.tempVec(pt.x, pt.y);
        getBody().getWorldPointToOut(v, v);
        return out.set(v.x, v.y);
    }

    public Point2F mapPoint(Point2F pt) {
        return mapPoint(pt, pt);
    }

    @Override
    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        if (live) {
            return true;
        }
        body.getWorld().destroyBody(body);
        return false;
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

    public void accelerateTo(float targetVelocityX, float targetVelocityY, float acceleration) {
        final float velocityX = body.getLinearVelocity().x, velocityY = body.getLinearVelocity().y;
        final float velDiffX = targetVelocityX - velocityX;
        final float velDiffY = targetVelocityY - velocityY;
        if (FloatMathUtils.isZero(velDiffX, velDiffY)) {
            return;
        }

        final float l2 = FloatMathUtils.lengthSquare(velDiffX, velDiffY);
        if (acceleration * acceleration >= l2) {
            body.setLinearVelocity(Box2dUtils.tempVec(targetVelocityX, targetVelocityY));
            return;
        }

        final float l = FloatMathUtils.sqrt(l2);
        body.setLinearVelocity(Box2dUtils.tempVec(velocityX + velDiffX * acceleration / l, velocityY + velDiffY * acceleration / l));
    }

    public void rotateTo(float targetAngle, float velocity) {
        final float angle = body.getAngle();
        final float angleDiff = FloatMathUtils.radDiff(targetAngle, angle);

        body.setAngularVelocity(0);
        if (FloatMathUtils.isZero(angleDiff)) {
            return;
        }

        if (FloatMathUtils.abs(velocity) >= FloatMathUtils.abs(angleDiff)) {
            setAngle(targetAngle);
            return;
        }
        setAngle(angle + FloatMathUtils.copySign(velocity, angleDiff));
    }

    protected void onDrawTransformed(DrawContext context) {
        super.onDraw(context);
        if (getEngine().getDebug().isDrawConvexShapes()) {
            context.setColor(Color.GREEN);
            drawBodyShapes(context, false);
        }
    }

    protected final void drawBodyShapes(DrawContext context, boolean fill) {
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            final Shape shape = f.getShape();
            drawShape(context, fill, shape);
        }
    }

    protected final void drawShape(DrawContext context, boolean fill, Shape shape) {
        switch (shape.getType()) {
        case EDGE:
            drawEdge(context, (EdgeShape)shape);
            break;

        case CIRCLE:
            final CircleShape circle = (CircleShape) shape;
            final Vec2 pos = circle.getVertex(0);
            final float radius = circle.getRadius();
            if (fill) {
                context.fillCircle(pos.x, pos.y, radius);
            } else {
                context.drawCircle(pos.x, pos.y, radius);
                context.drawLine(pos.x, pos.y, pos.x + radius, pos.y);
            }
            break;

        case POLYGON:
            final PolygonShape polygon = (PolygonShape) shape;
            final float[] buf = TempUtils.POINT_BUF;
            final int pointCount = polygon.getVertexCount();
            for (int i=0; i < pointCount; i++) {
                final Vec2 v = polygon.getVertex(i);
                buf[i*2] = v.x;
                buf[i*2+1] = v.y;
            }
            if (fill) {
                context.fillPoly(buf, 0, pointCount);
            } else {
                context.drawPoly(buf, 0, pointCount);
            }
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

    public void onCollision(Box2dObject object) {
    }

    public boolean canCollide(Box2dObject object) {
        return true;
    }

    public void setInitialPosition(float x, float y) {
        bodyDef.position.set(x, y);
    }

    public void setInitialAngle(float angle) {
        bodyDef.angle = angle;
    }

    public void setInitialVelocity(float x, float y) {
        bodyDef.linearVelocity.set(x, y);
    }

}
