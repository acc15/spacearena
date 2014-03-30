package ru.spacearena.jogl;

import ru.spacearena.engine.util.FloatMathUtils;

import java.nio.FloatBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-03
 */
public class Particle {

    private float x, y;
    private float velocityX, velocityY;
    private float accelerationX, accelerationY;
    private float targetX, targetY;
    private float restitution;
    private float r, g, b;

    public void setTarget(float x, float y) {
        this.targetX = x;
        this.targetY = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(float vx, float vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void setAcceleration(float ax, float ay) {
        this.accelerationX = ax;
        this.accelerationY = ay;
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void update(JoglListener lst, float dt) {

        final float dx = this.targetX - this.x, dy = this.targetY - this.y;

        final float l = FloatMathUtils.length(dx, dy);
        final float d = FloatMathUtils.isZero(l) ? 0f : 500f/l;
        //final float dm = d/100f;
        //final float invDm
        this.accelerationX = dx*d;
        this.accelerationY = dy*d;

        this.velocityX = (this.velocityX * 0.9f + this.accelerationX * dt);
        this.velocityY = (this.velocityY * 0.9f + this.accelerationY * dt);

        this.x += velocityX * dt;
        this.y += velocityY * dt;
        if (x > lst.getRight()) {
            x = lst.getRight();
            velocityX = -velocityX * restitution;
        } else if (x < lst.getLeft()) {
            x = lst.getLeft();
            velocityX = -velocityX * restitution;
        }
        if (y > lst.getBottom()) {
            y = lst.getBottom();
            velocityY = -velocityY * restitution;
        } else if (y < lst.getTop()) {
            y = lst.getTop();
            velocityY = -velocityY * restitution;
        }
    }

    public void put(FloatBuffer buf) {
        buf.put(x).put(y).put(r).put(g).put(b);
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetX() {
        return targetX;
    }
}
