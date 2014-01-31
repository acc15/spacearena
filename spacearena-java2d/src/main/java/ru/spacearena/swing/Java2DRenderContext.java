package ru.spacearena.swing;

import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.graphics.RenderContext;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Java2DRenderContext implements RenderContext {

    private Graphics2D graphics2D;
    private Rectangle bounds;

    public Java2DRenderContext(Graphics2D graphics2D, Rectangle bounds) {
        this.graphics2D = graphics2D;
        this.bounds = bounds;
    }

    public void setColor(Color color) {
        graphics2D.setColor(new java.awt.Color(color.getValue(), true));
    }

    public void setTextSize(float textSize) {
        final Font derivedFont = graphics2D.getFont().deriveFont(textSize);
        graphics2D.setFont(derivedFont);
    }

    public void fillScreen() {
        graphics2D.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void drawText(String text, int x, int y) {
        graphics2D.drawString(text, x, y);
    }

    public Matrix getMatrix() {
        return new Java2DMatrix(graphics2D.getTransform());
    }

    public void setMatrix(Matrix matrix) {
        graphics2D.setTransform(matrix.getNativeMatrix(AffineTransform.class));
    }

    public void drawImage(Image image, Matrix matrix) {
        graphics2D.drawImage(
                image.getNativeImage(java.awt.Image.class),
                matrix.getNativeMatrix(AffineTransform.class),
                Java2DImage.LOAD_OBSERVER);
    }

    public void drawCircle(float x, float y, float size) {
        graphics2D.fillOval((int)x, (int)y, (int)size, (int)size);
    }
}
