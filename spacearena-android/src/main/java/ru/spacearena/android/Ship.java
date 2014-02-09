package ru.spacearena.android;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-10-02
 */
public class Ship extends Entity {

    private PhysicsHandler physicsHandler = new PhysicsHandler(this);

    public Ship(Sprite image) {
        final float pivotX = image.getWidth()/2;
        final float pivotY = image.getHeight()/2;
        image.setPosition(-pivotX, -pivotY);
        image.setRotationCenter(pivotX, pivotY);
        registerUpdateHandler(physicsHandler);
        attachChild(image);
    }

    public PhysicsHandler getPhysicsHandler() {
        return physicsHandler;
    }

}
