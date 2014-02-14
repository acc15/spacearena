package ru.spacearena.engine.common;

import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class PhysicalObject extends TransformObject {

    private float velocityX = 0f, velocityY = 0f;
    private float accelerationX = 0f, accelerationY = 0f;
    private float angularVelocity = 0f;

    // TODO add min/max speed

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setAcceleration(float accelerationX, float accelerationY) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean onUpdate(float seconds) {
        velocityX += accelerationX * seconds;
        velocityY += accelerationY * seconds;
        if (!FloatMathUtils.isZero(velocityX, velocityY)) {
            x += velocityX * seconds;
            y += velocityY * seconds;
            isDirty = true;
        }
        if (!FloatMathUtils.isZero(angularVelocity)) {
            rotation = FloatMathUtils.normalizeDegrees(rotation + angularVelocity * seconds);
            isDirty = true;
        }
        return true;
    }
}
