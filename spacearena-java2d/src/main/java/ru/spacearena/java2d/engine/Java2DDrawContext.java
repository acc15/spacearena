package ru.spacearena.java2d.engine;

import ru.spacearena.android.engine.graphics.DrawContext;
import ru.spacearena.android.engine.graphics.Matrix;

import java.awt.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DDrawContext implements DrawContext {

    private Graphics2D graphics2D;
    private int width, height;
    private int fontOffset;

    public DrawContext wrap(Graphics2D graphics2D, int width, int height) {
        this.graphics2D = graphics2D;
        this.width = width;
        this.height = height;
        this.fontOffset = graphics2D.getFontMetrics().getAscent();
        return this;
    }

    public void setColor(int color) {
        graphics2D.setColor(new Color(color, true));
    }

    public void fillRect(float left, float top, float right, float bottom) {
        graphics2D.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    }

    public void fill() {
        graphics2D.fillRect(0, 0, width, height);
    }

    public void drawText(String text, float x, float y) {
        graphics2D.drawString(text, x, y + fontOffset);
    }

    public void setMatrix(Matrix matrix) {
        graphics2D.setTransform(((Java2DMatrix)matrix).affineTransform);
    }

    public Matrix getMatrix() {
        return new Java2DMatrix(graphics2D.getTransform());
    }
}
