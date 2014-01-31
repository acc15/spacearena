package ru.spacearena.game;

import android.graphics.PointF;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.TextDisplay;
import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.util.PointUtils;

import java.util.List;

import static ru.spacearena.engine.util.PointUtils.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-02
 */
public class Player extends EngineObject {

    private static final float MAX_VELOCITY = 1000f;

    private PointF touchPos = null;

    private float move(Ship ship) {
        if (touchPos == null) {
            return -1f;
        }

        final PointF vec = ship.velocity();
        vec.set(touchPos.x, touchPos.y);

        subtract(vec, ship.position());
        final float distance = vec.length();
        if (distance < 20) {
            return -1f;
        }

        PointUtils.resize(vec, MAX_VELOCITY);
        return distance;
    }

    @Override
    public boolean process(float time) {
        final Ship ship = getEngine().get(GameFactory.PLAYER_SHIP);
        final float distance = move(ship);
        if (distance < 0) {
            ship.velocity().set(0,0);
        }

        final TextDisplay textDisplay = getEngine().get(GameFactory.TEXT_DISPLAY);
        textDisplay.printMessage(String.format("FPS: %.2f", 1f/time));
        textDisplay.printMessage(String.format("Position: (%.2f;%.2f)", ship.position().x, ship.position().y));
        return true;
    }


    public boolean touch(MotionType type, List<PointF> points) {
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
