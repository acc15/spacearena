package ru.spacearena.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.spacearena.engine.NewEngineObject;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.common.PhysicalHandler;
import ru.spacearena.engine.common.TransformHandler;
import ru.spacearena.engine.handlers.DrawHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-02
 */
public class Bullet extends NewEngineObject {

    private final Paint paint = new Paint();

    public Point2F getVelocity() {
        return physicalHandler.getVelocity();
    }

    public void setVelocity(Point2F velocity) {
        physicalHandler.setVelocity(velocity);
        transformHandler.setRotation(velocity.angle());
    }

    public Point2F getPosition() {
        return transformHandler.getTranslate();
    }

    public void setPosition(Point2F position) {
        transformHandler.setTranslate(position);
    }

    private TransformHandler transformHandler = new TransformHandler();
    private PhysicalHandler physicalHandler = new PhysicalHandler(transformHandler);

    public Bullet() {
        addDrawHandler(transformHandler);
        addUpdateHandler(physicalHandler);
        addDrawHandler(new DrawHandler() {
            public void onDraw(Canvas canvas) {
                paint.setColor(Color.RED);
                canvas.drawRect(-5, -20, 5, 20, paint);
            }

            public void onPreDraw(Canvas canvas) {
            }

            public void onPostDraw(Canvas canvas) {
            }
        });

    }

    public boolean process(float time) {
        return true; // TODO
        //return position.getX() > -10000f && position.getX() < 10000f &&
        //       position.getY() > -10000f && position.getY() < 10000f;
    }

}
