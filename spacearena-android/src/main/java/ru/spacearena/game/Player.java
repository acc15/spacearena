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

    private static final float MAX_VELOCITY = 1000f;
    private static final Point2F ORIENTATION = Point2F.cartesian(0, -1);

    private Point2F touchPos = null;

    private TextDisplay textDisplay;
    private Viewport viewport;
    private Ship ship;

    public Player(TextDisplay textDisplay, Viewport viewport, Ship ship) {
        this.textDisplay = textDisplay;
        this.viewport = viewport;
        this.ship = ship;
    }

    private void calculateAngleAndSpeed(float time) {
        if (touchPos == null) {
            ship.setSpeed(0);
            return;
        }

        final Point2F vec = touchPos.sub(ship.getPosition());
        final float distance = vec.magnitude();
        if (distance < 20) {
            ship.setSpeed(0);
            return;
        }

        ship.setAngle(vec.angle());
        ship.setSpeed(MAX_VELOCITY * time);
    }

    @Override
    public boolean process(float time) {
        calculateAngleAndSpeed(time);
        textDisplay.printMessage(String.format("FPS: %.2f", 1f/time));
        return true;
    }
/*
    @Override
    public void render(Canvas canvas) {
        canvas.drawCircle();
    }
*/
    @Override
    public void postProcess() {

        //final float viewMagnitude = viewport.getViewSize().min() / 2;
        //final Point2F viewMove = Point2F.polar(ship.getAngle(), viewMagnitude);
        //final Point2F viewPos = ship.getPosition().add(viewMove);
        //final Point2F viewMove = ship.getVelocity().negate().resize(viewMagnitude);
        //final Point2F pt = ship.getPosition().add(viewMove);
        viewport.position(ship.getPosition());

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
