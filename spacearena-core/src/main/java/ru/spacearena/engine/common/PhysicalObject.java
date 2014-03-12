package ru.spacearena.engine.common;

import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.DrawUtils;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-02
 */
public class PhysicalObject extends Transform {

    float frameVelocityX = 0f, frameVelocityY = 0f;

    float frameAngularVelocity = 0f;

    float velocityX = 0f, velocityY = 0f;

    float angularVelocity = 0f;

    public float getFrameVelocityX() {
        return frameVelocityX;
    }

    public float getFrameVelocityY() {
        return frameVelocityY;
    }

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

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        computeVelocities(seconds);
        applyRotation(seconds);
        applyVelocities(seconds);
        return true;
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        if (!engine.getDebug().isDrawVelocities() || FloatMathUtils.isZero(velocityX, velocityY)) {
            return;
        }

        final float textSize = context.getTextSize();
        final float lineWidth = context.getLineWidth();
        try {
            context.setLineWidth(2f);
            context.setTextSize(40f);

            final float tx = x + velocityX, ty = y + velocityY;
            context.setColor(Color.WHITE);

            DrawUtils.drawArrow(context, x, y, tx, ty, DrawUtils.HeadType.ARROW, 50f, DrawUtils.HeadType.ARROW, 50f);
            context.drawText(String.format("%.2f;%.2f", velocityX, velocityY), tx, ty+20);

        } finally {
            context.setTextSize(textSize);
            context.setLineWidth(lineWidth);
        }
    }

    public void applyRotation(float seconds) {
        setRotation(rotation + frameAngularVelocity);
    }

    public void applyVelocities(float seconds) {
        translate(frameVelocityX, frameVelocityY);
    }

    public void accelerateTo(float targetVelocityX, float targetVelocityY, float acceleration) {
        final float velDiffX = targetVelocityX - velocityX;
        final float velDiffY = targetVelocityY - velocityY;
        if (!FloatMathUtils.isZero(velDiffX, velDiffY)) {
            final float length = acceleration/FloatMathUtils.length(velDiffX, velDiffY);
            final float appliedVelocityX = velDiffX * length;
            final float appliedVelocityY = velDiffY * length;
            velocityX = FloatMathUtils.absGt(appliedVelocityX, velDiffX)
                    ? targetVelocityX : velocityX + appliedVelocityX;
            velocityY = FloatMathUtils.absGt(appliedVelocityY, velDiffY)
                    ? targetVelocityY : velocityY + appliedVelocityY;
        }
    }

    public void rotateTo(float targetAngle, float velocity) {
        final float angleDiff = FloatMathUtils.angleDiff(targetAngle, rotation);
        if (!FloatMathUtils.isZero(angleDiff)) {
            setRotation(velocity > FloatMathUtils.abs(angleDiff)
                    ? targetAngle
                    : FloatMathUtils.copySign(velocity, angleDiff));
        }
    }


    public void computeVelocities(float seconds) {


        /*final float velDiffX = targetVelocityX - currentVelocityX;
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
        */

        this.frameVelocityX = velocityX * seconds;
        this.frameVelocityY = velocityY * seconds;
        this.frameAngularVelocity = angularVelocity * seconds;
    }


}
