package ru.spacearena.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-02
 */
public class Bullet extends EngineObject {

    private Point2F position = Point2F.ZERO;
    private Point2F velocity = Point2F.ZERO;

    private final Paint paint = new Paint();
    private final Matrix matrix = new Matrix();

    public Point2F getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2F velocity) {
        this.velocity = velocity;
    }

    public Point2F getPosition() {
        return position;
    }

    public void setPosition(Point2F position) {
        this.position = position;
    }

    private void updateMatrix() {
        matrix.setRotate(velocity.angle());
        matrix.postTranslate(position.getX(), position.getY());

    }

    @Override
    public boolean process(float time) {
        position = position.add(velocity.mul(time));
        return position.getX() > -10000f && position.getX() < 10000f &&
               position.getY() > -10000f && position.getY() < 10000f;
    }

    @Override
    public void render(Canvas canvas) {

        final Matrix oldMx = canvas.getMatrix();
        try {

            matrix.setTranslate(position.getX(), position.getY());
            matrix.preRotate(velocity.angle());
            matrix.postConcat(oldMx);

            paint.setColor(Color.RED);
            canvas.setMatrix(matrix);
            canvas.drawRect(-5, -20, 5, 20, paint);
        } finally {
            canvas.setMatrix(oldMx);
        }
    }
}
