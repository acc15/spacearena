package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.common.GenericContainer;
import ru.spacearena.engine.common.PhysicsHandler;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends GenericContainer {

    public Ship() {
        final Transform transform = new Transform();
        add(transform);
        add(new PhysicsHandler(transform));
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {35, 100, 110, 100};
        getTransform().mapPoints(positions);
        return positions;
    }

    @Override
    public void onInit(Engine engine) {
        final Image image = engine.loadImage("ship.png");
        final Transform transform = getTransform();
        transform.add(new Sprite(image));
        transform.setPivot(image.getWidth() / 2, image.getHeight() / 2);
        super.onInit(engine);
    }

    public Transform getTransform() {
        return get(0);
    }

    public PhysicsHandler getPhysics() {
        return get(1);
    }
}
