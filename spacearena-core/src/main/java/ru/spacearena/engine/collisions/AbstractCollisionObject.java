package ru.spacearena.engine.collisions;

import ru.spacearena.engine.common.AbstractBoundedTransform;
import ru.spacearena.engine.common.PhysicsHandler;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public abstract class AbstractCollisionObject extends AbstractBoundedTransform implements CollisionContainer.CollisionEntity {

    protected AbstractCollisionObject() {
        add(new PhysicsHandler(this));
    }

    public PhysicsHandler getPhysics() {
        return get(0);
    }

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);

        //final Bounds b = getAABB();
        //context.setColor(Color.RED);
        //context.drawRect(b.getMinX(), b.getMinY(), b.getMaxX(), b.getMaxY());
    }

    public Bounds getAABB() {
        return getTransformedBounds();
    }

    public void computeVelocities(float seconds) {
        getPhysics().updateVelocity(seconds);
    }

    public void applyVelocities(float seconds) {
        getPhysics().applyVelocity(seconds);
    }

    public float getFrameVelocityX() {
        return getPhysics().getFrameVelocityX();
    }

    public float getFrameVelocityY() {
        return getPhysics().getFrameVelocityY();
    }
}
