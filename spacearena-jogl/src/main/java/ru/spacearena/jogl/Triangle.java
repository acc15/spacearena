package ru.spacearena.jogl;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-03
*/
public class Triangle {

    public static final float COS_30 = 0.86602540378f, SIN_30 = 0.5f;

    public float r, g, b;
    public float size;
    public float velocityX, velocityY;
    public float positionX, positionY;

    public void update(JoglListener l, float dt) {
        positionX += velocityX * dt;
        positionY += velocityY * dt;
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
    }

    public float getVertexX(int i) {
        switch (i) {
        case 0: return positionX;
        case 1: return positionX - COS_30 * size;
        case 2: return positionX + COS_30 * size;
        default: throw new IllegalArgumentException();
        }
    }

    public float getVertexY(int i) {
        switch (i) {
        case 0: return positionY - size;
        case 1: case 2: return positionY + SIN_30 * size;
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

}
