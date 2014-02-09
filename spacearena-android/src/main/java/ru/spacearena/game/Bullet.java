package ru.spacearena.game;

import android.graphics.Canvas;
import android.graphics.Color;
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

    @Override
    public boolean process(float time) {
        position = position.add(velocity.mul(time));
        return position.getX() > -10000f && position.getX() < 10000f &&
               position.getY() > -10000f && position.getY() < 10000f;
    }

    @Override
    public void render(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(position.getX(), position.getY(), 20f, paint);
    }
}
