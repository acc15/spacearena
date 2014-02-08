package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.TextDisplay;
import ru.spacearena.engine.input.MotionType;

import java.util.List;


/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Player extends EngineObject {

    private static final float MAX_VELOCITY = 500f;
    private static final float SHIP_ACCEL = 500f;
    private static final float SHIP_DECCEL = 100f;

    private Point2F touchPos = null;

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
            ship.setVelocity(velocity.sub(velocity.resize(Math.min(SHIP_DECCEL*time, velocity.magnitude()))));
        }
    }

    private void calculateAngleAndSpeed(float time) {
        if (touchPos == null) {
            decelerate(time);
            return;
        }

        final Point2F movePos = viewport.mapPoint(touchPos);
        final Point2F vec = movePos.sub(ship.getPosition());
        final float distance = vec.magnitude();
        if (distance < 20) {
            decelerate(time);
            return;
        }

        final float angle = vec.angle();
        ship.setAngle(angle);

        final float accel = SHIP_ACCEL * time;
        final Point2F acceleration = Point2F.polar(angle, accel);
        ship.setVelocity(ship.getVelocity().add(acceleration));

        // TODO consider replacing ship velocity by polar coordinates
        if (ship.getVelocity().magnitude() > MAX_VELOCITY) {
            ship.setVelocity(ship.getVelocity().resize(MAX_VELOCITY));
        }
    }

    @Override
    public boolean process(float time) {
        calculateAngleAndSpeed(time);
        textDisplay.printMessage(String.format("FPS: %.2f", 1f/time));
        return true;
    }

    @Override
    public void postProcess() {
        viewport.setPosition(ship.getPosition());
        textDisplay.printMessage(String.format("Angle: %.2f", ship.getAngle()));
        textDisplay.printMessage(String.format("Position: (%.2f;%.2f)", ship.getPosition().getX(), ship.getPosition().getY()));
    }

    public boolean touch(MotionType type, List<Point2F> points) {
        switch (type) {
        case UP:
            touchPos = null;
            break;

        case DOWN:
        case MOVE:
            touchPos = points.get(0);
            break;
        }
        return true;
    }
}
