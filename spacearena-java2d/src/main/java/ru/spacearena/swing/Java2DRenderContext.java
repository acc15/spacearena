package ru.spacearena.swing;

import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Matrix2F;

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

    public Matrix2F getMatrix() {
        final double[] matrix = new double[6];
        final AffineTransform transform = graphics2D.getTransform();
        transform.getMatrix(matrix);
        return new Matrix2F(new float[] {
                (float)matrix[0], (float)matrix[1], 0,
                (float)matrix[2], (float)matrix[3], 0,
                (float)matrix[4], (float)matrix[5], 1});
    }

    public void setMatrix(Matrix2F matrix) {
        // todo
    }

    public void drawImage(Image image, Matrix2F matrix) {
        // TODO implement..

    }

    public void drawCircle(float x, float y, float size) {
        graphics2D.fillOval((int)x, (int)y, (int)size, (int)size);
    }
}
