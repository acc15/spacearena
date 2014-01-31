package ru.spacearena.game;

import android.graphics.*;
import ru.spacearena.engine.EngineObject;

import static ru.spacearena.engine.util.PointUtils.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    public static final PointF ORIENTATION = new PointF(0, -1);

    private final Bitmap image;
    private final PointF pivot;
    private final Matrix rotateMatrix = new Matrix();
    private final Paint paint = new Paint();

    private PointF position = new PointF(0,0);
    private PointF velocity = new PointF(0,0);




    public Ship(Bitmap image) {
        this.image = image;
        this.pivot = new PointF(image.getWidth()/2, (float) image.getHeight() * 2 / 3);
        rotateMatrix.preTranslate(-pivot.x, -pivot.y);
    }

    @Override
    public void init() {
    }

    public boolean process(float time) {
        if (velocity.x == 0 && velocity.y == 0) {
            return true;
        }

        final PointF vel = copy(velocity);
        add(position, mul(vel, time));

        final float cosineOfAngle = cosineOfAngle(velocity, ORIENTATION);

        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (velocity.x < 0) {
            angle = 360 - angle;
        }

        rotateMatrix.setRotate(angle);
        rotateMatrix.preTranslate(-pivot.x, -pivot.y);
        rotateMatrix.postTranslate(position.x, position.y);
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(image, rotateMatrix, paint);
    }

    public PointF position() {
        return position;
    }

    public PointF velocity() {
        return velocity;
    }

}
