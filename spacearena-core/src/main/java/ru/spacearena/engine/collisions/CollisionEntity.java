package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineEntity;
import ru.vmsoftware.math.geometry.shapes.AABB2F;

/**
* @author Vyacheslav Mayorov
* @since 2014-03-03
*/
public interface CollisionEntity extends EngineEntity {

    float getVelocityX();
    float getVelocityY();

    boolean onCollision(CollisionEntity entity, boolean reference, Contact contact);
    boolean canCollide(CollisionEntity entity);

    void applyRotation(float seconds);
    void applyVelocity(float seconds);

    AABB2F getAABB();
}
