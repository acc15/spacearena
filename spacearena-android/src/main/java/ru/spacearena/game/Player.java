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

    private Point2F movePt = null;
    private Point2F shootPt = null;
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
        Point2F movePos = movePt;
        if (movePos == null) {
            decelerate(time);
            return;
        }

        Point2F shootPos = shootPt;
        if (shootPos != null) {
            shootPos = viewport.mapPoint(shootPos).sub(ship.getPosition());
            ship.setAngle(shootPos.angle());
            if (!hasShot) {
                for (Point2F gun: ship.getGunPositions()) {
                    final Bullet bullet = new Bullet();
                    bullet.setPosition(gun);
                    bullet.setVelocity(shootPos.resize(1000f));
                    viewport.add(bullet);
                }
                hasShot = true;
            }
        }

        movePos = viewport.mapPoint(movePos).sub(ship.getPosition());
        if (movePos.magnitude() < 20) {
            decelerate(time);
            return;
        }

        if (shootPos == null) {
            ship.setAngle(movePos.angle());
        }

        final float accel = SHIP_ACCEL * time;
        final Point2F acceleration = movePos.resize(accel);
        Point2F velocity = ship.getVelocity().add(acceleration);

        final float speed = velocity.magnitude();
        if (speed > MAX_VELOCITY) {
            velocity = velocity.div(speed).mul(MAX_VELOCITY);
        }
        ship.setVelocity(velocity);
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
        movePt = shootPt = null;
        for (Point2F pt: points) {
            if (movePt == null) {
                movePt = pt;
            } else if (shootPt == null) {
                shootPt = pt;
            }
        }
        if (shootPt == null) {
            hasShot = false;
        }
        return true;
    }
}
