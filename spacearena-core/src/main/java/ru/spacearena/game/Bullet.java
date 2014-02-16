package ru.spacearena.game;

import ru.spacearena.engine.common.GenericContainer;
import ru.spacearena.engine.common.PhysicsHandler;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.graphics.Color;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends GenericContainer {

    public static final float SPEED = 1000f;

    public Bullet(float x, float y, float angle) {
        final Transform transform = new Transform();
        transform.add(new Rectangle(Color.RED, -5, -20, 5, 20));

        transform.setPosition(x, y);
        transform.setRotation(angle);

        final PhysicsHandler physicsHandler = new PhysicsHandler(transform);
        physicsHandler.setVelocityPolar(angle, SPEED);

        add(transform);
        add(physicsHandler);
    }

    public Transform getTransform() {
        return get(0);
    }

    public PhysicsHandler getPhysics() {
        return get(1);
    }

}
