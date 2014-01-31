package ru.spacearena.game;

import android.graphics.*;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.TextDisplay;
import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.util.PointUtils;

import java.util.List;

import static ru.spacearena.engine.util.PointUtils.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    private static final float MAX_VELOCITY = 1000f;
    public static final PointF ORIENTATION = new PointF(0, -1);

    private Bitmap image;

    private PointF position = new PointF(0,0);
    private float angle = 0;
    private PointF touchPos = null;

    private Matrix rotateMatrix = new Matrix();

    private Paint paint = new Paint();

    public Ship(Bitmap image) {
        this.image = image;
    }

    @Override
    public void init() {
    }

    private float move(float time) {
        if (touchPos == null) {
            return 0;
        }

        final PointF velocity = subtract(copy(touchPos), position);
        final float distance = velocity.length();
        if (distance < 20) {
            return distance;
        }

        add(position, PointUtils.resize(velocity, MAX_VELOCITY * time));

        final float cosineOfAngle = cosineOfAngle(velocity, ORIENTATION);

        angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (velocity.x < 0) {
            angle = 360 - angle;
        }

        final PointF pivot = new PointF(image.getWidth()/2, (float) image.getHeight() * 2 / 3);
        rotateMatrix.setRotate(angle);
        rotateMatrix.preTranslate(-pivot.x, -pivot.y);
        rotateMatrix.postTranslate(position.x, position.y);
        return distance;
    }

    public boolean process(float time) {
        final TextDisplay textDisplay = getEngine().get(GameFactory.TEXT_DISPLAY);
        textDisplay.printMessage("FPS: " + 1f / time);

        final float distance = move(time);
        textDisplay.printMessage(String.format("Position: (%.2f;%.2f); Angle: %.2f", position.x, position.y, angle));
        if (distance != 0) {
            textDisplay.printMessage(String.format("Distance to finger: %.2f", distance));
        }
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

    public void render(Canvas canvas) {
        canvas.drawBitmap(image, rotateMatrix, paint);
    }
}
