package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geom.Bounds;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public class CollisionContainer extends EngineContainer<CollisionContainer.CollisionEntity> {

    private static class AxisCollision {
        float timeOfImpact;
        float penetration;
    }

    public static interface CollisionEntity extends EngineEntity {

        Bounds getAABB();

        void computeVelocities(float seconds);
        void applyVelocities(float seconds);

        float getFrameVelocityX();
        float getFrameVelocityY();

        boolean onCollision(CollisionEntity entity, float timeOfImpact, float penetrationX, float penetrationY);
        boolean canCollide(CollisionEntity entity);

    }

    private AxisCollision xCollision = new AxisCollision();
    private AxisCollision yCollision = new AxisCollision();

    @Override
    public boolean onUpdate(float seconds) {
        // naive implementation
        int size = children.size();
        int i = 0;
        while (i < size) {
            final CollisionEntity e1 = children.get(i);
            if (i==0) {
                e1.computeVelocities(seconds);
            }

            float timeOfImpact = -1f, penetrationX = 0f, penetrationY = 0f;
            int entityIndex = -1;
            for (int j=i+1; j<size; j++) {
                final CollisionEntity e2 = children.get(j);
                if (i==0) {
                    e2.computeVelocities(seconds);
                }
                if (!e1.canCollide(e2)) {
                    continue;
                }

                if (!computeCollision(
                        e1.getAABB().getMinX(),
                        e1.getAABB().getMaxX(),
                        e2.getAABB().getMinX(),
                        e2.getAABB().getMaxX(),
                        e1.getFrameVelocityX(),
                        e2.getFrameVelocityX(),
                        xCollision)) {
                    continue;
                }
                if (!computeCollision(
                        e1.getAABB().getMinY(),
                        e1.getAABB().getMaxY(),
                        e2.getAABB().getMinY(),
                        e2.getAABB().getMaxY(),
                        e1.getFrameVelocityY(),
                        e2.getFrameVelocityY(),
                        yCollision)) {
                    continue;
                }

                final float xyTime = Math.max(xCollision.timeOfImpact, yCollision.timeOfImpact);
                if (xCollision.timeOfImpact > yCollision.timeOfImpact && xCollision.timeOfImpact < timeOfImpact) {
                    timeOfImpact = xCollision.timeOfImpact;
                    penetrationX = xCollision.penetration;
                    penetrationY = 0;
                    entityIndex = j;
                } else if (yCollision.timeOfImpact > xCollision.timeOfImpact && yCollision.timeOfImpact < timeOfImpact) {
                    timeOfImpact = yCollision.timeOfImpact;
                    penetrationX = 0f;
                    penetrationY = yCollision.penetration;
                    entityIndex = j;
                }
            }
            if (entityIndex < 0) {
                e1.applyVelocities(seconds);
                ++i;
                continue;
            }

            final CollisionEntity firstContactEntity = get(entityIndex);
            if (!e1.onCollision(firstContactEntity, timeOfImpact, penetrationX, penetrationY)) {
                children.remove(i);
                --size;
            } else {
                ++i;
            }
            if (!firstContactEntity.onCollision(e1, timeOfImpact, penetrationX, penetrationY)) {
                children.remove(entityIndex);
                --size;
            }
        }
        return true;
    }

    public boolean computeCollision(float min1, float max1, float min2, float max2, float v1, float v2, AxisCollision axisCollision) {
        final float v = v1 - v2;
        if (min1 <= max2 && v >= 0) {
            return computePenetration(min2 - max1, v, axisCollision);
        }
        if (min2 <= max1 && v <= 0 && computePenetration(min1 - max2, -v, axisCollision)) {
            axisCollision.penetration = -axisCollision.penetration;
            return true;
        }
        return false;
    }

    public boolean computePenetration(float distance, float velocity, AxisCollision a) {
        if (distance > velocity) {
            return false;
        }
        a.timeOfImpact = velocity > 0 ? distance/velocity : 0;
        a.penetration = distance;
        return true;
    }

}
