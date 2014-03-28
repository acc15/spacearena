package ru.spacearena.jogl;

import ru.spacearena.engine.graphics.Color;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-03
*/
public class Triangle {

    public static final float COS_30 = 0.86602540378f, SIN_30 = 0.5f;

    public int color = Color.BLACK;
    public float size;
    public float velocityX, velocityY;
    public float positionX, positionY;

    public void update(int w, int h, float dt) {
        positionX += velocityX * dt;
        positionY += velocityY * dt;
        if (positionX > w) {
            positionX = w;
            velocityX = -velocityX;
        } else if (positionX < 0) {
            positionX = 0;
            velocityX = -velocityX;
        }
        if (positionY > h) {
            positionY = h;
            velocityY = -velocityY;
        } else if (positionY < 0) {
            positionY = 0;
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

    public int getVertexCount() {
        return 3;
    }

}
