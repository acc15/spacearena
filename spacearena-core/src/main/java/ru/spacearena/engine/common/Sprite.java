package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.vmsoftware.math.geometry.shapes.AABB2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Sprite extends EngineObject implements AABB2F {

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

    public float getCenterX() {
        return x + getHalfWidth();
    }

    public float getCenterY() {
        return y + getHalfHeight();
    }

    public float getHalfWidth() {
        return getWidth()/2;
    }

    public float getHalfHeight() {
        return getHeight()/2;
    }

    @Override
    public void onDraw(DrawContext context) {
        context.drawImage(image, x, y);
    }
}
