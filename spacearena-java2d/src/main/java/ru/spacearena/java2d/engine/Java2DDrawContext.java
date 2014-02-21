package ru.spacearena.java2d.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

import java.awt.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DDrawContext implements DrawContext {

    private Graphics2D graphics2D;
    private int fontOffset;
    private int fontHeight;

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

    public void fillRect(float left, float top, float right, float bottom) {
        graphics2D.fillRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    }

    public void fillCircle(float x, float y, float radius) {
        graphics2D.fillOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
    }

    public void drawText(String text, float x, float y) {
        graphics2D.drawString(text, x, y + fontOffset);
    }

    public void drawRect(float left, float top, float right, float bottom) {
        graphics2D.drawRect((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    }

    public void drawImage(Image image, float x, float y) {
        graphics2D.drawImage(((Java2DImage)image).image, (int)x, (int)y, null);
    }

    public void setMatrix(Matrix matrix) {
        graphics2D.setTransform(((Java2DMatrix)matrix).affineTransform);
    }

    public Matrix getMatrixCopy() {
        return new Java2DMatrix(graphics2D.getTransform());
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        graphics2D.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    public void setTextSize(float size) {
        final Font newFont = graphics2D.getFont().deriveFont(size);
        graphics2D.setFont(newFont);
    }

    public float getTextSize() {
        return graphics2D.getFont().getSize2D();
    }

    public static final int MAX_POLY_POINTS = 100;
    private static final int[] xPointBuf = new int[MAX_POLY_POINTS];
    private static final int[] yPointBuf = new int[MAX_POLY_POINTS];

    public void drawPoly(float[] points) {
        final int pc = points.length/2;
        for (int i=0;i<pc;i++) {
            xPointBuf[i] = (int)points[i*2];
            yPointBuf[i] = (int)points[i*2+1];
        }
        graphics2D.drawPolygon(xPointBuf, yPointBuf, pc);
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
}
