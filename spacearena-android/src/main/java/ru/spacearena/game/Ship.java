package ru.spacearena.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    public static final Point2F ORIENTATION = Point2F.to(0, -1);

    private final Bitmap image;
    private final Point2F pivot;
    private final Matrix rotateMatrix = new Matrix();
    private final Paint paint = new Paint();

    private Point2F position = Point2F.ZERO;
    private Point2F velocity = Point2F.ZERO;

    public Ship(Bitmap image) {
        this.image = image;
        this.pivot = Point2F.to(image.getWidth()/2, (float) image.getHeight() * 2 / 3);
        rotateMatrix.preTranslate(-pivot.getX(), -pivot.getY());
    }

    @Override
    public void init() {
    }

    public boolean process(float time) {
        if (velocity.isZero()) {
            return true;
        }

        position = position.add(velocity);

        final float cosineOfAngle = velocity.cosineOfAngle(ORIENTATION);
        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (velocity.getX() < 0) {
            angle = 360 - angle;
        }

        rotateMatrix.setRotate(angle);
        rotateMatrix.preTranslate(-pivot.getX(), -pivot.getY());
        rotateMatrix.postTranslate(position.getX(), position.getY());
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(image, rotateMatrix, paint);
    }

    public Point2F getPosition() {
        return position;
    }

    public Point2F getVelocity() {
        return velocity;
    }

    public void setPosition(Point2F position) {
        this.position = position;
    }

    public void setVelocity(Point2F velocity) {
        this.velocity = velocity;
    }
}
