package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class PhysicsHandler extends EngineObject {

    private float velocityX = 0f, velocityY = 0f;
    private float angularVelocity = 0f;

    // TODO add min/max speed


    private Transform transform;

    public PhysicsHandler(Transform transform) {
        this.transform = transform;
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

    public boolean onUpdate(float seconds) {
        if (!FloatMathUtils.isZero(velocityX, velocityY)) {
            transform.x += velocityX * seconds;
            transform.y += velocityY * seconds;
            transform.markDirty();
        }
        if (!FloatMathUtils.isZero(angularVelocity)) {
            transform.rotation = FloatMathUtils.normalizeDegrees(transform.rotation + angularVelocity * seconds);
            transform.markDirty();
        }
        return true;
    }
}
