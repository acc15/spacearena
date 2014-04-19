package ru.spacearena.engine.integration.box2d;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dBody extends Box2dObject {

    private final Matrix matrix = new Matrix();

    private boolean live = true;
    private Body body;


    private float smoothX, smoothY, smoothAngle;
    private float prevX, prevY, prevAngle;

    private final BodyDef bodyDef = new BodyDef();

    public Transform getTransform() {
        return body.getTransform();
    }

    public float getAngle() {
        return smoothAngle;//body.getAngle();
    }

    public void markDead() {
        this.live = false;
    }

    public Body getBody() {
        return body;
    }

    public void setAngle(float radians) {
        body.setTransform(body.getPosition(), radians);
    }

    public float getPositionX() {
        return smoothX;//body.getPosition().x;
    }

    public float getPositionY() {
        return smoothY;//body.getPosition().y;
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
        smoothX = prevX = body.getPosition().x;
        smoothY = prevY = body.getPosition().y;
        smoothAngle = prevAngle = body.getAngle();
        matrix.setTransform(smoothAngle, smoothX, smoothY);
    }

    protected void onPreCreate(BodyDef bodyDef) {
    }

    protected void onPostCreate(Body body) {
    }

    public Point2F mapPoint(Point2F pt) {
        return matrix.transformPoint(pt);
    }

    public float mapPointX(float x, float y) {
        return matrix.transformPointX(x,y);
    }

    public float mapPointY(float x, float y) {
        return matrix.transformPointY(x,y);
    }

    @Override
    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        if (!live) {
            body.getWorld().destroyBody(body);
            return false;
        }
        return true;
    }

    public void onStep(float dt) {
        prevX = body.m_xf.p.x;
        prevY = body.m_xf.p.y;
        prevAngle = body.m_sweep.a;
    }

    @Override
    public void onSmooth(float ratio, float prevRatio) {
        final float newX = body.m_xf.p.x * ratio + prevX * prevRatio,
                    newY = body.m_xf.p.y * ratio + prevY * prevRatio,
                    newAngle = FloatMathUtils.normalizeRadians(prevAngle +
                            FloatMathUtils.radDiff(body.m_sweep.a, prevAngle) * ratio);
        if (newX != smoothX || newY != smoothY || newAngle != smoothAngle) {
            smoothX = newX;
            smoothY = newY;
            smoothAngle = newAngle;
            matrix.setTransform(smoothAngle, smoothX, smoothY);
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        try {
            context.multiplyMatrix(matrix);
            onDrawTransformed(context);
        } finally {
            context.popMatrix();
        }
        if (getEngine().getDebug().isDrawVelocities()) {
            final float vx = body.getLinearVelocity().x, vy = body.getLinearVelocity().y;
            context.color(Color.LIGHT_GRAY).drawArrow(smoothX, smoothY, smoothX + vx, smoothY + vy, 1);
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
            drawBodyShapes(context, Color.GREEN, false);
        }
    }

    protected final void drawBodyShapes(DrawContext context, Color color, boolean fill) {
        context.color(color);
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
            }
            break;

        case POLYGON:
            final PolygonShape polygon = (PolygonShape) shape;
            final int pc = polygon.getVertexCount();
            final DrawContext.Polygon p = context.polygon();
            for (int i=0; i<pc; i++) {
                final Vec2 v = polygon.getVertex(i);
                p.put(v.x, v.y);
            }
            if (fill) {
                p.fill();
            } else {
                p.draw();
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


    public void onCollision(Box2dBody object, boolean isReference, Contact contact, ContactImpulse impulse) {
    }

    public boolean canCollide(Box2dBody object) {
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
