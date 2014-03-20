package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

import java.util.Stack;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidDrawContext implements DrawContext {

    private final Paint strokePaint = new Paint();
    private final Paint fillPaint = new Paint();
    private final Stack<android.graphics.Matrix> matrixStack = new Stack<android.graphics.Matrix>();

    private android.graphics.Matrix current = new android.graphics.Matrix();

    private Canvas canvas;

    public AndroidDrawContext() {
        strokePaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public DrawContext wrap(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public float getTextHeight() {
        final Paint.FontMetrics fm = strokePaint.getFontMetrics();
        return fm.bottom - fm.top;
    }

    public float getTextAscent() {
        return strokePaint.getFontMetrics().top;
    }

    public void setColor(int color) {
        fillPaint.setColor(color);
        strokePaint.setColor(color);
    }

    public void fillRect(float left, float top, float right, float bottom) {
        canvas.drawRect(left, top, right, bottom, fillPaint);
    }

    public void fillCircle(float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, fillPaint);
    }

    public void drawText(String text, float x, float y) {
        canvas.drawText(text, x, y - getTextAscent(), strokePaint);
    }

    public void drawRect(float left, float top, float right, float bottom) {
        canvas.drawRect(left, top, right, bottom, strokePaint);
    }

    public void drawImage(Image image, float x, float y) {
        canvas.drawBitmap(((AndroidImage)image).bitmap, x, y, strokePaint);
    }

    public int getColor() {
        return strokePaint.getColor();
    }

    public void drawCircle(float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, strokePaint);
    }

    public void pushMatrix(Matrix matrix) {
        final android.graphics.Matrix mx = new android.graphics.Matrix(current);
        mx.preConcat(((AndroidMatrix)matrix).androidMatrix);
        matrixStack.push(current);
        current = mx;
        canvas.setMatrix(mx);
    }

    public void popMatrix() {
        current = matrixStack.pop();
        canvas.setMatrix(current);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, strokePaint);
    }

    public void setTextSize(float size) {
        strokePaint.setTextSize(size);
    }

    public float getTextSize() {
        return strokePaint.getTextSize();
    }

    public void drawPoly(float[] points, int start, int pointCount) {
        canvas.drawPoints(points, start, pointCount << 1, strokePaint);
    }

    public float getLineWidth() {
        return strokePaint.getStrokeWidth();
    }

    public void setLineWidth(float width) {
        strokePaint.setStrokeWidth(width);
    }

    private final Path path = new Path();

    public void fillPoly(float[] pointBuf, int start, int pointCount) {
        path.reset();
        for (int i=0; i<pointCount; i++) {
            final float x = pointBuf[start+i*2], y = pointBuf[start+i*2+1];
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, fillPaint);
    }
}
