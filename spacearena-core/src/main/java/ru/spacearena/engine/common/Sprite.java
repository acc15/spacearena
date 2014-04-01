package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineEntity;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Sprite extends Transform<EngineEntity> /*implements BoundingBox2F*/ {



    /*
    private final Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public float getWidth() {
        return image.getWidth() * getScaleX();
    }

    public float getHeight() {
        return image.getHeight() * getScaleY();
    }

    public float getMinX() {
        return getPositionX() - getPivotX() * getScaleX();
    }

    public float getMaxX() {
        return getMinX() + getWidth();
    }

    public float getMinY() {
        return getPositionY() - getPivotY() * getScaleY();
    }

    public float getMaxY() {
        return getMinY() + getHeight();
    }

    public float getCenterX() {
        return getPositionX();
    }

    public float getCenterY() { return getPositionY(); }

    public float getHalfWidth() {
        return getWidth()/2;
    }

    public float getHalfHeight() {
        return getHeight()/2;
    }

    @Override
    protected void onDrawTransformed(DrawContext context) {
        super.onDrawTransformed(context);
        context.drawImage(image, 0, 0);
    }
    */

}
