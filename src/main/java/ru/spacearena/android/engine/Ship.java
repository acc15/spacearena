package ru.spacearena.android.engine;

import android.content.res.Resources;
import android.graphics.*;
import android.view.MotionEvent;
import ru.spacearena.android.R;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship implements EngineObject {

    private float MAX_VELOCITY = 1000f;

    private Bitmap bitmap;
    private final Paint paint = new Paint();

    private Point position = Point.zero(2);
    private Point touchPos = null;
    private Matrix matrix = new Matrix();
    private Matrix moveMatrix = new Matrix();
    private Matrix rotateMatrix = new Matrix();

    public Ship(Resources resources) {
        this.bitmap = BitmapFactory.decodeResource(resources, R.drawable.ship);
    }

    public boolean process(float timeDelta) {
        if (touchPos == null) {
            return true;
        }

        final Point vec = touchPos.sub(position);
        if (vec.closeTo(5)) {
            return true;
        }

        final Point direction = vec.identity();
        position = position.add(direction.mul(MAX_VELOCITY * timeDelta));

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
