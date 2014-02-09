package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.TextDisplay;

import java.util.Collection;


/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Player extends EngineObject {

    private static final float MAX_VELOCITY = 500f;
    private static final float SHIP_ACCEL = 500f;
    private static final float SHIP_DECCEL = 100f;

    private Point2F flyTo = null;
    private Point2F lookTo = null;
    private boolean hasShot = false;

    private TextDisplay textDisplay;
    private Viewport viewport;
    private Ship ship;

    public Player(TextDisplay textDisplay, Viewport viewport, Ship ship) {
        this.textDisplay = textDisplay;
        this.viewport = viewport;
        this.ship = ship;
    }

    private void decelerate(float time) {
        if (!ship.getVelocity().isZero()) {
            final Point2F velocity = ship.getVelocity();
            ship.setVelocity(velocity.sub(velocity.resize(Math.min(SHIP_DECCEL * time, velocity.magnitude()))));
        }
    }

    private void calculateAngleAndSpeed(float time) {
        if (flyTo == null) {
            decelerate(time);
            return;
        }

        final Point2F lookPos = lookTo != null ? viewport.mapPoint(lookTo).sub(ship.getPosition()) : null;
        if (lookPos != null) {
            ship.setAngle(lookPos.angle());
            if (!hasShot) {
                final Bullet bullet = new Bullet();
                bullet.setPosition(ship.getPosition());
                bullet.setVelocity(lookPos.resize(1000f));
                viewport.add(bullet);
                hasShot = true;
            }
        }

        final Point2F vec = viewport.mapPoint(flyTo).sub(ship.getPosition());
        if (vec.magnitude() < 20) {
            decelerate(time);
            return;
        }

        final float angle = vec.angle();
        if (lookPos == null) {
            ship.setAngle(angle);
        }

        final float accel = SHIP_ACCEL * time;
        final Point2F acceleration = Point2F.polar(angle, accel);
        ship.setVelocity(ship.getVelocity().add(acceleration));

        // TODO consider replace ship velocity to polar coordinates
        final float speed = ship.getVelocity().magnitude();
        if (speed > MAX_VELOCITY) {
            ship.setVelocity(ship.getVelocity().div(speed).mul(MAX_VELOCITY));
        }
    }

    @Override
    public boolean process(float time) {
        calculateAngleAndSpeed(time);
        textDisplay.printMessage(String.format("FPS: %.2f", 1f / time));
        return true;
    }

    @Override
    public void postProcess() {
        viewport.setPosition(ship.getPosition());
        textDisplay.printMessage(String.format("Angle: %.2f", ship.getAngle()));
        textDisplay.printMessage(String.format("Position: (%.2f;%.2f)", ship.getPosition().getX(), ship.getPosition().getY()));
    }

    public boolean touch(Collection<Point2F> points) {
        flyTo = lookTo = null;
        for (Point2F pt: points) {
            if (flyTo == null) {
                flyTo = pt;
            } else if (lookTo == null) {
                lookTo = pt;
            }
        }
        if (lookTo == null) {
            hasShot = false;
        }
        return true;
    }
}
