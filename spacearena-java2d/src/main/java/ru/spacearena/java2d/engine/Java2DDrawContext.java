package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DDrawContext implements DrawContext {

    private Graphics2D graphics2D;
    private int fontOffset;
    private int fontHeight;

    private final Line2D.Float floatLine = new Line2D.Float();
    private final Ellipse2D.Float floatEllipse = new Ellipse2D.Float();
    private final Rectangle2D.Float floatRect = new Rectangle2D.Float();
    private final Path2D.Float floatPath = new Path2D.Float();

    private final Stack<AffineTransform> matrixStack = new Stack<AffineTransform>();
    private final AffineTransform imageTransform = new AffineTransform();

    //public static final int MAX_POLY_POINTS = 100;
    //private static final int[] xPointBuf = new int[MAX_POLY_POINTS];
    //private static final int[] yPointBuf = new int[MAX_POLY_POINTS];

    public DrawContext wrap(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        final FontMetrics fm = graphics2D.getFontMetrics();
        this.fontOffset = fm.getAscent();
        this.fontHeight = fm.getHeight();
        return this;
    }

    public float getTextHeight() {
        return fontHeight;
    }

    public void setColor(int color) {
        graphics2D.setColor(new Color(color, true));
    }

    public int getColor() {
        return graphics2D.getColor().getRGB();
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
        graphics2D.drawString(text, x, y + fontOffset);
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

    public void drawPoly(float[] points, int start, int pointCount) {
        setPath(points, start, pointCount);
        graphics2D.draw(floatPath);
    }

    public void fillPoly(float[] pointBuf, int start, int pointCount) {
        setPath(pointBuf, start, pointCount);
        graphics2D.fill(floatPath);
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

    private void setRect(float l, float t, float r, float b) {
        floatRect.setFrameFromDiagonal(l, t, r, b);
    }

    private void setLine(float x1, float y1, float x2, float y2) {
        floatLine.setLine(x1, y1, x2, y2);
    }

    private void setEllipse(float x, float y, float rx, float ry) {
        floatEllipse.setFrame(x-rx, y-ry, rx*2, ry*2);
    }

    private void setPath(float[] points, int start, int pointCount) {
        floatPath.reset();
        for (int i=0; i<=pointCount; i++) {
            final int imod = i%pointCount;
            final float x = points[start+imod*2];
            final float y = points[start+imod*2+1];
            if (i == 0) {
                floatPath.moveTo(x, y);
            } else{
                floatPath.lineTo(x, y);
            }
        }
        /*

        for (int i=0;i<pointCount;i++) {
            xPointBuf[i] = (int)points[i*2+start];
            yPointBuf[i] = (int)points[i*2+start+1];
        }*/
    }



}
