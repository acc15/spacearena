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
    public static final int DEFAULT_TEXT_SIZE = 30;
    private final Paint paint = new Paint();
    private final Stack<android.graphics.Matrix> matrixStack = new Stack<android.graphics.Matrix>();
    private final android.graphics.Matrix concat = new android.graphics.Matrix();

    private Canvas canvas;
    private float fontAscent;
    private float fontHeight;

    private void enableFill() {
        paint.setStyle(Paint.Style.FILL);
    }

    private void enableStroke() {
        paint.setStyle(Paint.Style.STROKE);
    }

    public AndroidDrawContext() {
        paint.setTextSize(DEFAULT_TEXT_SIZE);
        final Paint.FontMetrics fm = paint.getFontMetrics();
        fontAscent = -fm.top;
        fontHeight = fm.bottom - fm.top;
    }

    public DrawContext wrap(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public float getTextHeight() {
        return fontHeight;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void fillRect(float left, float top, float right, float bottom) {
        enableFill();
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void fillCircle(float x, float y, float radius) {
        enableFill();
        canvas.drawCircle(x, y, radius, paint);
    }

    public void drawText(String text, float x, float y) {
        canvas.drawText(text, x, y + fontAscent, paint);
    }

    public void drawRect(float left, float top, float right, float bottom) {
        enableStroke();
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void drawImage(Image image, float x, float y) {
        canvas.drawBitmap(((AndroidImage)image).bitmap, x, y, paint);
    }

    public int getColor() {
        return paint.getColor();
    }

    public void drawCircle(float x, float y, float radius) {
        enableStroke();
        canvas.drawCircle(x, y, radius, paint);
    }

    public void pushMatrix(Matrix matrix) {
        final android.graphics.Matrix stackCopy = canvas.getMatrix();
        matrixStack.push(stackCopy);
        canvas.getMatrix(concat);
        concat.preConcat(((AndroidMatrix)matrix).androidMatrix);
        canvas.setMatrix(concat);
    }

    public void popMatrix() {
        canvas.setMatrix(matrixStack.pop());
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void setTextSize(float size) {
        paint.setTextSize(size);
    }

    public float getTextSize() {
        return paint.getTextSize();
    }

    public void drawPoly(float[] points, int start, int pointCount) {
        enableStroke();
        canvas.drawPoints(points, start, pointCount << 1, paint);
    }

    public float getLineWidth() {
        return paint.getStrokeWidth();
    }

    public void setLineWidth(float width) {
        paint.setStrokeWidth(width);
    }

    private final Path path = new Path();

    public void fillPoly(float[] pointBuf, int start, int pointCount) {
        enableFill();
        path.reset();
        for (int i=0; i<pointCount; i++) {
            final float x = pointBuf[start+i*2], y = pointBuf[start+i*2+1];
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, paint);
    }
}
