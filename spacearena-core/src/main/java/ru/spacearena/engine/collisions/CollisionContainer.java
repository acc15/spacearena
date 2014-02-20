package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geom.Bounds;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public class CollisionContainer extends EngineContainer<CollisionContainer.CollisionEntity> {

    public static interface CollisionEntity extends EngineEntity {

        Bounds getAABB();

        void updateVelocities(float seconds);

        float getFrameVelocityX();
        float getFrameVelocityY();

    }

    public static interface CollisionListener {
        void onCollision(CollisionEntity e1, CollisionEntity e2, float timeOfImpact);
        boolean canCollide(CollisionEntity e1, CollisionEntity e2);
    }

    private CollisionListener listener;

    public CollisionContainer(CollisionListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onUpdate(float seconds) {
        // naive implementation
        final int size = children.size();
        for (int i=0; i<size; i++) {
            final CollisionEntity e1 = children.get(i);
            if (i==0) {
                e1.updateVelocities(seconds);
            }

            float timeOfImpact = -1f;
            for (int j=i+1; j<size; j++) {
                final CollisionEntity e2 = children.get(j);
                if (i==0) {
                    e2.updateVelocities(seconds);
                }
                if (!listener.canCollide(e1, e2)) {
                    return false;
                }

                final float xTime = computeTimeOfImpact(
                        e1.getAABB().getMinX(),
                        e1.getAABB().getMaxX(),
                        e2.getAABB().getMinX(),
                        e2.getAABB().getMaxX(),
                        e1.getFrameVelocityX(),
                        e2.getFrameVelocityX());
                if (xTime < 0) {
                    continue;
                }

                final float yTime = computeTimeOfImpact(
                        e1.getAABB().getMinY(),
                        e1.getAABB().getMaxY(),
                        e2.getAABB().getMinY(),
                        e2.getAABB().getMaxY(),
                        e1.getFrameVelocityY(),
                        e2.getFrameVelocityY());
                if (yTime < 0) {
                    continue;
                }
                listener.onCollision(e1, e2, timeOfImpact);
            }
        }
        return true;
    }

    public float computeTimeOfImpact(float min1, float max1, float min2, float max2, float v1, float v2) {
        if (max1 <= min2) {
            return computeTimeOfImpact(min2 - max1, v1 - v2);
        } else if (max2 <= min1) {
            return computeTimeOfImpact(min1 - max2, v2 - v1);
        } else {
            // they're already collide
            return 0f;
        }
    }

    public float computeTimeOfImpact(float distance, float speed) {
        if (speed == 0 || speed < distance) {
            return -1f;
        }
        return distance/speed;
    }

}
