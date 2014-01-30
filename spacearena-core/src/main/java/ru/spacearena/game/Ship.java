package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Matrix2F;
import ru.spacearena.engine.primitives.Point2F;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    private static final float MAX_VELOCITY = 1000f;

    private final Image bitmap;

    private Point2F position = new Point2F();
    private Point2F touchPos = null;

    private final Matrix2F rotateMatrix = new Matrix2F();

    public Ship(Image image) {
        this.bitmap = image;
    }

    public boolean process(float time) {
        if (touchPos == null) {
            return true;
        }

        final Point2F velocity = touchPos.copy().sub(position);
        position.add(velocity.length(MAX_VELOCITY * time));

        final float cosineOfAngle = velocity.cosineOfAngle(new Point2F(0,-1));
        float angle = (float)Math.toDegrees(Math.acos(cosineOfAngle));
        if (velocity.x < 0) {
            angle = 360 - angle;
        }

        final Point2F pivot = new Point2F(bitmap.getWidth()/2, (float)bitmap.getHeight() * 2 / 3);
        final Point2F bmPos = position.copy().sub(pivot);
        rotateMatrix.rotate(angle, pivot).translate(bmPos);
        return true;
    }

    public boolean touch(MotionType type, List<Point2F> points) {
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

    public void render(RenderContext context) {
        context.drawImage(bitmap, rotateMatrix);
    }
}
