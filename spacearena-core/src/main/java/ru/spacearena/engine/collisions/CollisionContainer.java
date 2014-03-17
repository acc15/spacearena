package ru.spacearena.engine.collisions;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.util.FloatMathUtils
;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public class CollisionContainer extends EngineContainer<CollisionObject> {

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
        int size = getChildCount();
        for (int i=0; i<size; i++) {
            final CollisionObject e1 = getChild(i);
            if (i == 0) {
                e1.computeBoundingBox(seconds);
                e1.applyRotation(seconds);
            }

            final float e1vx = e1.getVelocityX() * seconds, e1vy = e1.getVelocityY() * seconds;
            contact.setContact(1f, 0f, 0f);

            int firstContactIndex = -1;
            for (int j=i+1; j<size; j++) {
                final CollisionObject e2 = getChild(j);
                if (i == 0) {
                    e2.computeBoundingBox(seconds);
                    e2.applyRotation(seconds);
                }
                if (!e1.canCollide(e2)) {
                    continue;
                }

                /*
                final float e2vx = e2.getVelocityX() * seconds, e2vy = e2.getVelocityY() * seconds;

                final BoundingBox2F a = e1.getAABB();
                final BoundingBox2F b = e2.getAABB();

                final float vx = e1vx - e2vx;
                computeContact(a.getMinX(), a.getMaxX(), b.getMinX(), b.getMaxX(), vx, xContact);
                if (xContact.startTime >= 1f) {
                    continue;
                }

                final float vy = e1vy - e2vy;
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
                */
            }
            if (firstContactIndex < 0) {
                e1.applyVelocity(seconds);
                continue;
            }

            final CollisionObject e2 = getChild(firstContactIndex);
            if (!e1.onCollision(e2, seconds, true, contact)) {
                remove(i);
                --size;
                --i;
            }
            if (!e2.onCollision(e1, seconds, false, contact)) {
                remove(firstContactIndex);
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
