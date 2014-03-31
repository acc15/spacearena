package ru.spacearena.jogl;

import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.jogl.matrix.Matrix4F;

import java.nio.FloatBuffer;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-03
*/
public class Triangle {

    public static final float COS_30 = 0.86602540378f, SIN_30 = 0.5f;

    private float r, g, b;
    private float size;
    private float velocityX, velocityY;
    private float positionX, positionY;
    private float rotation;

    public Matrix4F matrix = new Matrix4F();

    public void setSize(float size) {
        this.size = size;
    }

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setVelocity(float x, float y) {
        this.velocityX = x;
        this.velocityY = y;
    }

    public void update(JoglListener l, float dt) {

        setPosition(positionX + velocityX * dt, positionY + velocityY * dt);
        if (positionX > l.getRight()) {
            positionX = l.getRight();
            velocityX = -velocityX;
        } else if (positionX < l.getLeft()) {
            positionX = l.getLeft();
            velocityX = -velocityX;
        }
        if (positionY > l.getBottom()) {
            positionY = l.getBottom();
            velocityY = -velocityY;
        } else if (positionY < l.getTop()) {
            positionY = l.getTop();
            velocityY = -velocityY;
        }
        rotation += FloatMathUtils.TWO_PI * dt;

        matrix.identity();
        matrix.preRotate(rotation);
        matrix.preTranslate(positionX, positionY);

        //matrix.rotate(rotation);
    }

    public float getVertexX(int i) {
        switch (i) {
        case 0: return 0;
        case 1: return -COS_30 * size;
        case 2: return COS_30 * size;
        default: throw new IllegalArgumentException();
        }
    }

    public float getVertexY(int i) {
        switch (i) {
        case 0: return -size;
        case 1: case 2: return SIN_30 * size;
        default: throw new IllegalArgumentException();
        }
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getVertexCount() {
        return 3;
    }

    public void put(FloatBuffer buf) {
        for (int i=0; i<getVertexCount(); i++) {
            buf.put(getVertexX(i)).put(getVertexY(i)).put(r).put(g).put(b);
        }
    }

    public float getRotation() {
        return rotation;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionX() {
        return positionX;
    }
}
