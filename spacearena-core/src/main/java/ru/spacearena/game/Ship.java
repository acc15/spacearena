package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.input.MotionType;
import ru.spacearena.engine.primitives.Matrix2F;
import ru.spacearena.engine.primitives.Point2F;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship extends EngineObject {

    private static final float MAX_VELOCITY = 1000f;

    private Image image;

    private Point2F position = new Point2F();
    private Point2F touchPos = null;

    private Matrix rotateMatrix;

    @Override
    public void init() {
        this.image = getPlatformManager().loadImage("ship");
        this.rotateMatrix = getPlatformManager().createMatrix();
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

        final Point2F pivot = new Point2F(image.getWidth()/2, (float) image.getHeight() * 2 / 3);
        rotateMatrix.translate(pivot.negate()).rotate(angle).translate(position);
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
        context.drawImage(image, rotateMatrix);
    }
}
