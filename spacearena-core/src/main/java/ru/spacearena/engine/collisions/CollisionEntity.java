package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geom.Bounds;

/**
* @author Vyacheslav Mayorov
* @since 2014-03-03
*/
public interface CollisionEntity extends EngineEntity {

    Bounds getAABB();

    void computeVelocities(float seconds);
    void applyVelocities(float seconds);

    float getFrameVelocityX();
    float getFrameVelocityY();

    boolean onCollision(CollisionEntity entity, boolean reference, Contact contact);
    boolean canCollide(CollisionEntity entity);

}
