package ru.spacearena.engine.common;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Sprite extends TransformObject {

    Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    @Override
    public void onDraw(DrawContext context) {
        final Matrix matrix = getMatrix();
        if (matrix != null) {
            context.drawImage(image, matrix);
        } else {
            context.drawImage(image, 0, 0);
        }
    }
}
