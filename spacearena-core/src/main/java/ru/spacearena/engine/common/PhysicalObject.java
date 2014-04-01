package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.graphics.ColorU;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.DrawUtils;
import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-02
 */
public class PhysicalObject extends Transform<EngineEntity> {

    private float velocityX = 0f, velocityY = 0f;
    private float accelerationX = 0f, accelerationY = 0f;
    private float angularVelocity = 0f;

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

    public void setVelocityByAngle(float angle, float speed) {
        final float rads = FloatMathUtils.toRadians(angle);
        setVelocity(FloatMathUtils.cos(rads) * speed, FloatMathUtils.sin(rads) * speed);
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
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

    public void applyRotation(float seconds) {
        rotate(angularVelocity * seconds);
    }

    public void applyVelocity(float seconds) {
        velocityX += accelerationX * seconds;
        velocityY += accelerationY * seconds;
        translate(velocityX * seconds, velocityY * seconds);
    }

    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        applyRotation(seconds);
        applyVelocity(seconds);
        return true;
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        drawVelocities(context);
    }

    private void drawVelocities(DrawContext context) {
        if (!getEngine().getDebug().isDrawVelocities() || FloatMathUtils.isZero(velocityX, velocityY)) {
            return;
        }

        final float textSize = context.getTextSize();
        final float lineWidth = context.getLineWidth();
        try {
            context.setLineWidth(2f);
            context.setTextSize(40f);

            final float tx = getPositionX() + velocityX, ty = getPositionY() + velocityY;
            context.setColor(ColorU.WHITE);

            DrawUtils.drawArrow(context, getPositionX(), getPositionY(), tx, ty, DrawUtils.HeadType.NONE, 0f, DrawUtils.HeadType.ARROW, 50f);
            context.drawText(String.format("%.2f;%.2f", velocityX, velocityY), tx, ty+20);

        } finally {
            context.setTextSize(textSize);
            context.setLineWidth(lineWidth);
        }
    }

    public void accelerateTo(float targetVelocityX, float targetVelocityY, float acceleration, float time) {
        final float velDiffX = targetVelocityX - velocityX;
        final float velDiffY = targetVelocityY - velocityY;
        if (FloatMathUtils.isZero(velDiffX, velDiffY)) {
            accelerationX = 0f;
            accelerationY = 0f;
            return;
        }

        final float l2 = FloatMathUtils.lengthSquare(velDiffX, velDiffY);

        final float at = acceleration * time;
        if (at*at >= l2) {
            accelerationX = velDiffX / time;
            accelerationY = velDiffY / time;
            return;
        }

        final float l = FloatMathUtils.sqrt(l2);
        accelerationX = velDiffX * acceleration / l;
        accelerationY = velDiffY * acceleration / l;
    }

    public void rotateTo(float targetAngle, float velocity, float time) {
        final float angleDiff = FloatMathUtils.radDiff(targetAngle, getAngle());
        if (FloatMathUtils.isZero(angleDiff)) {
            angularVelocity = 0f;
            return;
        }

        final float vt = velocity * time;
        if (FloatMathUtils.abs(vt) >= FloatMathUtils.abs(angleDiff)) {
            angularVelocity = angleDiff / time;
            return;
        }

        angularVelocity = FloatMathUtils.copySign(velocity, angleDiff);
    }

}
