package ru.spacearena.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Point2F;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    private final Bitmap image;
    private final Point2F pivot;
    private final Matrix rotateMatrix = new Matrix();
    private final Paint paint = new Paint();

    private Point2F position = Point2F.ZERO;
    private float angle = 0;
    private Point2F velocity = Point2F.ZERO;
    private final float[] gunPositions = new float[] {35, 100, 115, 100};

    public Ship(Bitmap image) {
        this.image = image;
        this.pivot = Point2F.cartesian(image.getWidth() / 2, (float) image.getHeight() * 2 / 3);
    }

    @Override
    public void init() {
    }

    public boolean process(float time) {
        position = position.add(velocity.mul(time));
        updateMatrix();
        return true;
    }

    public void render(Canvas canvas) {
        canvas.drawBitmap(image, rotateMatrix, paint);
    }

    public List<Point2F> getGunPositions() {
        updateMatrix();
        final float[] values = new float[gunPositions.length];
        rotateMatrix.mapPoints(values, gunPositions);
        final List<Point2F> pts = new ArrayList<Point2F>();
        for (int i=0; i<values.length; i+=2) {
            pts.add(Point2F.cartesian(values[i], values[i+1]));
        }
        return pts;
    }

    private void updateMatrix() {
        rotateMatrix.setRotate(angle);
        rotateMatrix.preTranslate(-pivot.getX(), -pivot.getY());
        rotateMatrix.postTranslate(position.getX(), position.getY());
    }

    public Point2F getPosition() {
        return position;
    }

    public void setPosition(Point2F position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Point2F getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2F velocity) {
        this.velocity = velocity;
    }

}
