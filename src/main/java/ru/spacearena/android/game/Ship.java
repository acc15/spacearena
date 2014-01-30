package ru.spacearena.android.game;

import android.content.res.Resources;
import android.graphics.*;
import ru.spacearena.android.R;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.Point;
import ru.spacearena.android.engine.events.MotionType;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    private static final float MAX_VELOCITY = 1000f;

    private final Bitmap bitmap;
    private final Paint paint = new Paint();

    private Point position = Point.zero(2);
    private Point touchPos = null;


    private final Matrix matrix = new Matrix();
    private final Matrix moveMatrix = new Matrix();
    private final Matrix rotateMatrix = new Matrix();

    public Ship(Resources resources) {
        this.bitmap = BitmapFactory.decodeResource(resources, R.drawable.ship);
    }

    public boolean process(float time) {
        if (touchPos == null) {
            return true;
        }

        final Point vec = touchPos.sub(position);
        if (vec.closeTo(10)) {
            return true;
        }

        final Point direction = vec.identity();
        position = position.add(direction.mul(MAX_VELOCITY * time));

        final float cosineOfAngle = direction.cosineOfAngle(Point.create(0, -1));
        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (direction.getX() < 0) {
            angle = 360-angle;
        }

        final Point pivot = Point.create(bitmap.getWidth()/2, (float)bitmap.getHeight() * 2 / 3);
        rotateMatrix.setRotate(angle, pivot.getX(), pivot.getY());

        final Point bmPos = position.sub(pivot);
        moveMatrix.setTranslate(bmPos.getX(), bmPos.getY());
        matrix.setConcat(moveMatrix, rotateMatrix);

        return true;
    }

    public boolean touch(MotionType type, List<Point> points) {
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
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
