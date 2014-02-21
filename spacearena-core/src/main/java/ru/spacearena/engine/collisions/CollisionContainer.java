package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public class CollisionContainer extends EngineContainer<CollisionContainer.CollisionEntity> {

    public static interface CollisionEntity extends EngineEntity {

        Bounds getAABB();

        void computeVelocities(float seconds);
        void applyVelocities(float seconds);

        float getFrameVelocityX();
        float getFrameVelocityY();

        boolean onCollision(CollisionEntity entity, boolean b, float penetrationX, float penetrationY);
        boolean canCollide(CollisionEntity entity);

    }

    @Override
    public boolean onUpdate(float seconds) {
        // naive implementation
        int size = children.size();
        for (int i=0; i<size; i++) {
            final CollisionEntity e1 = children.get(i);
            if (i==0) {
                e1.computeVelocities(seconds);
            }

            float distanceX = 0f, distanceY = 0f;
            int entityIndex = -1;
            for (int j=i+1; j<size; j++) {
                final CollisionEntity e2 = children.get(j);
                if (i==0) {
                    e2.computeVelocities(seconds);
                }
                if (!e1.canCollide(e2)) {
                    continue;
                }

                final Bounds a = e1.getAABB();
                final Bounds b = e2.getAABB();

                final float vx = e1.getFrameVelocityX() - e2.getFrameVelocityX();
                float xOffset = computeCollision(a.getMinX(), a.getMaxX(), b.getMinX(), b.getMaxX(), vx);
                if (FloatMathUtils.isZero(xOffset)) {
                    continue;
                }

                final float vy = e1.getFrameVelocityY() - e2.getFrameVelocityY();
                float yOffset = computeCollision(a.getMinY(), a.getMaxY(), b.getMinY(), b.getMaxY(), vy);
                if (FloatMathUtils.isZero(yOffset)) {
                    continue;
                }

                if (FloatMathUtils.abs(xOffset) < FloatMathUtils.abs(yOffset)) {
                    yOffset = FloatMathUtils.isZero(vx) ? 0 : vy*xOffset/vx;
                } else {
                    xOffset = FloatMathUtils.isZero(vy) ? 0 : vx*yOffset/vy;
                }

                if (entityIndex < 0 ||
                        FloatMathUtils.abs(xOffset) > FloatMathUtils.abs(distanceX) ||
                        FloatMathUtils.abs(yOffset) > FloatMathUtils.abs(distanceY)) {
                    distanceX = xOffset;
                    distanceY = yOffset;
                    entityIndex = j;
                }

            }
            if (entityIndex < 0) {
                e1.applyVelocities(seconds);
                continue;
            }

            final CollisionEntity firstContactEntity = get(entityIndex);
            if (!e1.onCollision(firstContactEntity, true, distanceX, distanceY)) {
                children.remove(i);
                --size;
                --i;
            }
            if (!firstContactEntity.onCollision(e1, false, distanceX, distanceY)) {
                children.remove(entityIndex);
                --size;
            }
        }
        return true;
    }

    public float computeCollision(float a0, float a1, float b0, float b1, float v) {
        final float ac = (a1+a0)/2, bc = (b1+b0)/2;
        if (ac <= bc) {
            final float dist = b0-a1;
            return v > dist ? v-dist : 0f;
        } else {
            final float dist = b1-a0;
            return v < dist ? v-dist : 0f;
        }
    }


}
