package ru.spacearena.android.game;

import android.content.res.Resources;
import android.graphics.*;
import android.view.MotionEvent;
import ru.spacearena.android.R;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.Frame;
import ru.spacearena.android.engine.Point;

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

    public boolean process(Frame frame) {
        if (touchPos == null) {
            return true;
        }

        final Point vec = touchPos.sub(position);
        if (vec.closeTo(10)) {
            return true;
        }

        final Point direction = vec.identity();
        position = position.add(direction.mul(MAX_VELOCITY * frame.getTimeDelta()));

        final float cosineOfAngle = direction.cosineOfAngle(Point.create(0, -1));
        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (direction.getX() < 0) {
            angle = 360-angle;
        }

        rotateMatrix.setRotate(angle, bitmap.getWidth()/2, bitmap.getHeight()/2);
        moveMatrix.setTranslate(
                position.getX() - bitmap.getWidth() / 2,
                position.getY() - bitmap.getHeight() / 2);
        matrix.setConcat(moveMatrix, rotateMatrix);

        return true;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
        case MotionEvent.ACTION_UP:
            touchPos = null;
            break;

        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
            touchPos = Point.create(motionEvent.getX(), motionEvent.getY());
            break;
        }

        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}