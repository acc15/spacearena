package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.*;
import ru.spacearena.engine.graphics.Image;

import java.awt.*;
import java.awt.Color;
import java.awt.geom.*;
import java.util.Stack;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DDrawContext implements DrawContext {

    private Graphics2D graphics2D;

    private final Line2D.Float floatLine = new Line2D.Float();
    private final Ellipse2D.Float floatEllipse = new Ellipse2D.Float();
    private final Rectangle2D.Float floatRect = new Rectangle2D.Float();
    private final Java2DPath path = new Java2DPath();

    private final Stack<AffineTransform> matrixStack = new Stack<AffineTransform>();
    private final AffineTransform imageTransform = new AffineTransform();

    public DrawContext wrap(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        return this;
    }

    public float getTextHeight() {
        return graphics2D.getFontMetrics().getHeight();
    }

    public void setColor(int color) {
        graphics2D.setColor(new Color(color, ru.spacearena.engine.graphics.Color.hasAlpha(color)));
    }

    public void pushMatrix(Matrix matrix) {
        final AffineTransform stackCopy = graphics2D.getTransform();
        matrixStack.push(stackCopy);
        final AffineTransform concatCopy = graphics2D.getTransform();
        concatCopy.concatenate(((Java2DMatrix) matrix).affineTransform);
        graphics2D.setTransform(concatCopy);
    }

    public void popMatrix() {
        graphics2D.setTransform(matrixStack.pop());
    }

    public float getTextSize() {
        return graphics2D.getFont().getSize2D();
    }

    public void setTextSize(float size) {
        graphics2D.setFont(graphics2D.getFont().deriveFont(size));
    }

    public void drawText(String text, float x, float y) {
        graphics2D.drawString(text, x, y + graphics2D.getFontMetrics().getAscent());
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        setLine(x1, y1, x2, y2);
        graphics2D.draw(floatLine);
    }

    public void drawImage(Image image, float x, float y) {
        imageTransform.setToTranslation(x, y);
        graphics2D.drawImage(((Java2DImage)image).image, imageTransform, null);
    }

    public void drawRect(float l, float t, float r, float b) {
        setRect(l, t, r, b);
        graphics2D.draw(floatRect);
    }

    public void fillRect(float l, float t, float r, float b) {
        setRect(l, t, r, b);
        graphics2D.fill(floatRect);
    }

    public void drawCircle(float x, float y, float radius) {
        setEllipse(x, y, radius, radius);
        graphics2D.draw(floatEllipse);
    }

    public void fillCircle(float x, float y, float radius) {
        setEllipse(x, y, radius, radius);
        graphics2D.fill(floatEllipse);
    }

    public Path preparePath() {
        return path;
    }

    public void drawPath() {
        graphics2D.draw(path.path);
        path.path.reset();
    }

    public void fillPath() {
        graphics2D.fill(path.path);
        path.path.reset();
    }

    public float getLineWidth() {
        final Stroke s = graphics2D.getStroke();
        if (s instanceof BasicStroke) {
            return ((BasicStroke)s).getLineWidth();
        }
        return 1f;
    }

    public void setLineWidth(float width) {
        graphics2D.setStroke(new BasicStroke(width));
    }

    public float getAlpha() {
        final Composite composite = graphics2D.getComposite();
        if (composite instanceof AlphaComposite) {
            return ((AlphaComposite)composite).getAlpha();
        }
        return 1f;
    }

    public void setAlpha(float alpha) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    private void setRect(float l, float t, float r, float b) {
        floatRect.setFrameFromDiagonal(l, t, r, b);
    }

    private void setLine(float x1, float y1, float x2, float y2) {
        floatLine.setLine(x1, y1, x2, y2);
    }

    private void setEllipse(float x, float y, float rx, float ry) {
        floatEllipse.setFrame(x-rx, y-ry, rx*2, ry*2);
    }

}
