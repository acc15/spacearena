package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Sprite extends EngineObject implements Bounds {

    Image image;
    float x, y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getWidth() {
        return image.getWidth();
    }

    public float getHeight() {
        return image.getHeight();
    }

    public float getMinX() {
        return x;
    }

    public float getMaxX() {
        return x + image.getWidth();
    }

    public float getMinY() {
        return y;
    }

    public float getMaxY() {
        return y + image.getHeight();
    }

    @Override
    public void onDraw(DrawContext context) {
        context.drawImage(image, x, y);
    }
}
