package ru.spacearena.engine.common;

import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-02
 */
public class PhysicalObject extends Transform {


    float frameVelocityX = 0f, frameVelocityY = 0f;

    float frameAngularVelocity = 0f;

    // current move vector
    float currentVelocityX = 0f, currentVelocityY = 0f;

    // target move vector
    float targetVelocityX = 0f, targetVelocityY = 0f;

    // an rotation to which object should rotate with {@link angularVelocity} speed
    float targetRotation = 0f;

    // how quickly an object can rotate
    float angularVelocity = 1f;

    // how quickly an object can move
    float maxSpeed = 1f;

    // how quickly speed can change
    float acceleration;

    public float getCurrentVelocityX() {
        return currentVelocityX;
    }

    public void setCurrentVelocityX(float currentVelocityX) {
        this.currentVelocityX = currentVelocityX;
    }

    public float getCurrentVelocityY() {
        return currentVelocityY;
    }

    public void setCurrentVelocityY(float currentVelocityY) {
        this.currentVelocityY = currentVelocityY;
    }

    public void setCurrentVelocity(float velocityX, float velocityY) {
        this.currentVelocityX = velocityX;
        this.currentVelocityY = velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.currentVelocityX = velocityX;
        this.targetVelocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.currentVelocityY = velocityY;
        this.targetVelocityY = velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.currentVelocityX = this.targetVelocityX = velocityX;
        this.currentVelocityY = this.targetVelocityY = velocityY;
    }

    public void setTargetVelocityX(float targetVelocityX) {
        this.targetVelocityX = targetVelocityX;
    }

    public void setTargetVelocityY(float targetVelocityY) {
        this.targetVelocityY = targetVelocityY;
    }

    public void setTargetVelocity(float velocityX, float velocityY) {
        this.targetVelocityX = velocityX;
        this.targetVelocityY = velocityY;
    }

    public float getTargetVelocityX() {
        return targetVelocityX;
    }

    public float getTargetVelocityY() {
        return targetVelocityY;
    }

    public void setCurrentVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadiansTop(degrees);
        setCurrentVelocity(FloatMathUtils.cos(radians) * maxSpeed, FloatMathUtils.sin(radians) * maxSpeed);
    }

    public void setTargetVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadiansTop(degrees);
        setTargetVelocity(FloatMathUtils.cos(radians) * maxSpeed, FloatMathUtils.sin(radians) * maxSpeed);
    }

    public void setVelocityByAngle(float degrees) {
        final float radians = FloatMathUtils.toRadiansTop(degrees);
        setVelocity(FloatMathUtils.cos(radians) * maxSpeed, FloatMathUtils.sin(radians) * maxSpeed);
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setRotation(float angle) {
        targetRotation = angle;
        super.setRotation(angle);
    }

    public float getTargetRotation() {
        return targetRotation;
    }

    public void setTargetRotation(float targetRotation) {
        this.targetRotation = targetRotation;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getFrameVelocityX() {
        return frameVelocityX;
    }

    public float getFrameVelocityY() {
        return frameVelocityY;
    }

    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        computeVelocities(seconds);
        applyVelocities(seconds);
        return true;
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        if (engine.getDebug().isDrawVelocities() && !FloatMathUtils.isZero(currentVelocityX, currentVelocityY)) {

            final float textSize = context.getTextSize();
            final float lineWidth = context.getLineWidth();
            try {
                context.setLineWidth(2f);
                context.setTextSize(40f);

                final float tx = x + currentVelocityX, ty = y + currentVelocityY;
                context.setColor(Color.WHITE);
                context.drawLine(x, y, tx, ty);

                final float angle = FloatMathUtils.angle(-currentVelocityX, -currentVelocityY);
                final float r1 = FloatMathUtils.toRadiansTop(angle + 30);
                final float arrow = 20f;
                context.drawLine(tx, ty, tx + FloatMathUtils.cos(r1) * arrow, ty + FloatMathUtils.sin(r1) * arrow);
                final float r2 = FloatMathUtils.toRadiansTop(angle - 30);
                context.drawLine(tx, ty, tx + FloatMathUtils.cos(r2) * arrow, ty + FloatMathUtils.sin(r2) * arrow);
                context.drawText(String.format("%.2f;%.2f", currentVelocityX, currentVelocityY), tx, ty+20);

            } finally {
                context.setTextSize(textSize);
                context.setLineWidth(lineWidth);
            }
        }
    }

    public void applyVelocities(float seconds) {
        translate(frameVelocityX, frameVelocityY);
        super.setRotation(rotation + frameAngularVelocity);
    }

    public void computeVelocities(float seconds) {
        final float velDiffX = targetVelocityX - currentVelocityX;
        final float velDiffY = targetVelocityY - currentVelocityY;
        if (!FloatMathUtils.isZero(velDiffX, velDiffY)) {
            final float frameAcceleration = acceleration * seconds;
            final float length = frameAcceleration/FloatMathUtils.length(velDiffX, velDiffY);
            final float appliedVelocityX = velDiffX * length;
            final float appliedVelocityY = velDiffY * length;
            currentVelocityX = FloatMathUtils.absGt(appliedVelocityX, velDiffX)
                    ? targetVelocityX : currentVelocityX + appliedVelocityX;
            currentVelocityY = FloatMathUtils.absGt(appliedVelocityY, velDiffY)
                    ? targetVelocityY : currentVelocityY + appliedVelocityY;
        }

        // apply rotation
        final float angleDiff = FloatMathUtils.angleDiff(targetRotation, rotation);
        if (!FloatMathUtils.isZero(angleDiff)) {
            final float frameAngularSpeed = angularVelocity * seconds;
            frameAngularVelocity = frameAngularSpeed > FloatMathUtils.abs(angleDiff)
                    ? targetRotation - rotation
                    : FloatMathUtils.copySign(frameAngularSpeed, angleDiff);
        }

        frameVelocityX = currentVelocityX * seconds;
        frameVelocityY = currentVelocityY * seconds;
    }


}
