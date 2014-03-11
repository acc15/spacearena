package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.geom.Bounds;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public class CollisionContainer extends EngineContainer<CollisionEntity> {

    public static class AxisContact {
        // time when shapes start overlap [0..1) 1 = shapes won't overlap (in current frame) by given velocity
        float startTime;

        // time when shapes end overlap [0..1) 1 = shapes won't end overlap (in current frame) if startTime < 1
        float endTime;

        // amount of overlap
        float overlap;
    }

    private final AxisContact xContact = new AxisContact();
    private final AxisContact yContact = new AxisContact();
    private final Contact contact = new Contact();

    @Override
    public boolean onUpdate(float seconds) {
        // naive implementation
        int size = children.size();
        for (int i=0; i<size; i++) {
            final CollisionEntity e1 = children.get(i);
            if (i==0) {
                e1.computeVelocities(seconds);
            }

            contact.setContact(1f, 0f, 0f);

            int firstContactIndex = -1;
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
                computeContact(a.getMinX(), a.getMaxX(), b.getMinX(), b.getMaxX(), vx, xContact);
                if (xContact.startTime >= 1f) {
                    continue;
                }

                final float vy = e1.getFrameVelocityY() - e2.getFrameVelocityY();
                computeContact(a.getMinY(), a.getMaxY(), b.getMinY(), b.getMaxY(), vy, yContact);
                if (yContact.startTime >= 1f) {
                    continue;
                }

                if (xContact.startTime > yContact.startTime) {
                    if (!FloatMathUtils.inRange(xContact.startTime, yContact.startTime, yContact.endTime)) {
                        continue;
                    }
                    if (xContact.startTime >= contact.time) {
                        continue;
                    }
                    firstContactIndex = j;
                    contact.time = xContact.startTime;
                } else {
                    if (!FloatMathUtils.inRange(yContact.startTime, xContact.startTime, xContact.endTime)) {
                        continue;
                    }
                    if (yContact.startTime >= contact.time) {
                        continue;
                    }
                    firstContactIndex = j;
                    contact.time = yContact.startTime;
                }

                if (FloatMathUtils.absGt(xContact.overlap, yContact.overlap)) {
                    contact.overlapX = 0f;
                    contact.overlapY = yContact.overlap;
                } else {
                    contact.overlapX = xContact.overlap;
                    contact.overlapY = 0f;
                }
            }
            if (firstContactIndex < 0) {
                e1.applyVelocities(seconds);
                continue;
            }

            final CollisionEntity firstContactEntity = get(firstContactIndex);
            if (!e1.onCollision(firstContactEntity, true, contact)) {
                children.remove(i);
                --size;
                --i;
            }
            if (!firstContactEntity.onCollision(e1, false, contact)) {
                children.remove(firstContactIndex);
                --size;
            }
        }
        return true;
    }

    public void computeContact(float a0, float a1, float b0, float b1, float v, AxisContact contact) {
        final float ac = (a1+a0)/2, bc = (b1+b0)/2;
        if (ac < bc) {
            // shape A is at left / top of B
            final float mind = b0 - a1, maxd = b1 - a0;
            contact.startTime = mind < 0 ? 0 : v > mind ? mind / v : 1;
            contact.endTime = v > maxd ? maxd / v : 1;
            contact.overlap = FloatMathUtils.min(mind, 0);
        } else {
            // shape A is at right / bottom of B
            final float mind = b1 - a0, maxd = b0 - a1;
            contact.startTime = mind > 0 ? 0 : v < mind ? mind / v : 1;
            contact.endTime = v < maxd ? maxd / v : 1;
            contact.overlap = FloatMathUtils.max(mind, 0);
        }
    }

}
