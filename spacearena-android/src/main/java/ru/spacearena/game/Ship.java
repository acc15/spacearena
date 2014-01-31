package ru.spacearena.game;

import android.graphics.*;
import ru.spacearena.engine.EngineObject;
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

    private PointF position = new PointF();
    private PointF touchPos = null;

    private Matrix rotateMatrix = new Matrix();

    private Paint paint = new Paint();

    public Ship(Bitmap image) {
        this.image = image;
    }

    @Override
    public void init() {
    }

    public boolean process(float time) {
        if (touchPos == null) {
            return true;
        }

        final PointF velocity = subtract(copy(touchPos), position);
        add(position, PointUtils.resize(velocity, MAX_VELOCITY * time));

        final float cosineOfAngle = cosineOfAngle(velocity, ORIENTATION);
        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (velocity.x < 0) {
            angle = 360 - angle;
        }

        final PointF pivot = new PointF(image.getWidth()/2, (float) image.getHeight() * 2 / 3);
        pivot.negate();

        rotateMatrix.preTranslate(pivot.x, pivot.y);
        rotateMatrix.setRotate(angle);
        rotateMatrix.postTranslate(position.x, position.y);
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
