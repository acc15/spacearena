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

    public static int TRANSFORM_INDEX = 0;
    public static int PHYSICS_INDEX = 1;

    public Ship() {
        final Transform transform = new Transform();
        add(transform);
        add(new PhysicsHandler(transform));
    }

    @Override
    public void onInit(Engine engine) {
        final Image image = engine.loadImage("ship.png");
        getTransform().add(new Sprite(image));
        getTransform().setPivot(image.getWidth() / 2, image.getHeight() / 2);
        super.onInit(engine);
    }

    public Transform getTransform() {
        return get(TRANSFORM_INDEX);
    }

    public PhysicsHandler getPhysics() {
        return get(PHYSICS_INDEX);
    }
}
