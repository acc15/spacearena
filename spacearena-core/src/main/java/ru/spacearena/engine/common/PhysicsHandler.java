package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class PhysicsHandler extends EngineObject {

    // current move vector
    float currentVelocityX = 0f, currentVelocityY = 0f;

    // target move vector
    float targetVelocityX = 0f, targetVelocityY = 0f;

    // an angle to which object should rotate with {@link angularSpeed} speed
    float targetAngle;

    // how quickly an object can rotate
    float angularSpeed = 0f;

    // how quickly an object can move
    float speed = 1f;

    // how quickly speed can change
    float acceleration;

    private Transform transform;

    public PhysicsHandler(Transform transform) {
        this.transform = transform;
    }

    public float getCurrentVelocityX() {
        return currentVelocityX;
    }

    public void setCurrentVelocityX(float currentVelocityX) {
        this.currentVelocityX = currentVelocityX * speed;
    }

    public float getCurrentVelocityY() {
        return currentVelocityY;
    }

    public void setCurrentVelocityY(float currentVelocityY) {
        this.currentVelocityY = currentVelocityY * speed;
    }

    public void setCurrentVelocity(float velocityX, float velocityY) {
        this.currentVelocityX = velocityX * speed;
        this.currentVelocityY = velocityY * speed;
    }

    public void setVelocityX(float velocityX) {
        this.currentVelocityX = velocityX * speed;
        this.targetVelocityX = velocityX * speed;
    }

    public void setVelocityY(float velocityY) {
        this.currentVelocityY = velocityY * speed;
        this.targetVelocityY = velocityY * speed;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.currentVelocityX = this.targetVelocityX = velocityX * speed;
        this.currentVelocityY = this.targetVelocityY = velocityY * speed;
    }

    public void setCurrentVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadians(degrees-90);
        setCurrentVelocity(FloatMathUtils.cos(radians), FloatMathUtils.sin(radians));
    }

    public void setTargetVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadians(degrees-90);
        setTargetVelocity(FloatMathUtils.cos(radians), FloatMathUtils.sin(radians));
    }

    public void setVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadians(degrees-90);
        setVelocity(FloatMathUtils.cos(radians), FloatMathUtils.sin(radians));
    }

    public float getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(float angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public void setAngle(float angle) {
        transform.setAngle(angle);
        targetAngle = angle;
    }

    public void setTargetVelocityX(float targetVelocityX) {
        this.targetVelocityX = targetVelocityX * speed;
    }

    public void setTargetVelocityY(float targetVelocityY) {
        this.targetVelocityY = targetVelocityY * speed;
    }

    public void setTargetVelocity(float velocityX, float velocityY) {
        this.targetVelocityX = velocityX * speed;
        this.targetVelocityY = velocityY * speed;
    }

    public float getTargetVelocityX() {
        return targetVelocityX;
    }

    public float getTargetVelocityY() {
        return targetVelocityY;
    }

    public float getTargetAngle() {
        return targetAngle;
    }

    public void setTargetAngle(float targetAngle) {
        this.targetAngle = targetAngle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void onDraw(DrawContext context) {
    }

    public boolean onUpdate(float seconds) {

        // calculate current velocity;

        final float velDiffX = targetVelocityX - currentVelocityX;
        final float velDiffY = targetVelocityY - currentVelocityY;
        if (!FloatMathUtils.isZero(velDiffX, velDiffY)) {

            final float frameAcceleration = acceleration * seconds;
            final float length = frameAcceleration/FloatMathUtils.length(velDiffX, velDiffY);
            final float appliedVelocityX = velDiffX * length;
            final float appliedVelocityY = velDiffY * length;
            if (FloatMathUtils.abs(appliedVelocityX) > FloatMathUtils.abs(velDiffX)) {
                currentVelocityX += velDiffX;
            } else {
                currentVelocityX += appliedVelocityX;
            }
            if (FloatMathUtils.abs(appliedVelocityY) > FloatMathUtils.abs(velDiffY)) {
                currentVelocityY += velDiffY;
            } else {
                currentVelocityY += appliedVelocityY;
            }
        }

        // apply velocity
        if (!FloatMathUtils.isZero(currentVelocityX, currentVelocityY)) {
            transform.x += currentVelocityX * seconds;
            transform.y += currentVelocityY * seconds;
            transform.markDirty();
        }

        // apply rotation
        final float angleDiff = FloatMathUtils.angleDiff(targetAngle, transform.angle);
        if (!FloatMathUtils.isZero(angleDiff)) {
            final float frameAngularSpeed = angularSpeed * seconds;
            transform.setAngle(frameAngularSpeed > FloatMathUtils.abs(angleDiff)
                    ? transform.angle + angleDiff
                    : transform.angle + FloatMathUtils.copySign(frameAngularSpeed, angleDiff));
        }
        return true;
    }
}
