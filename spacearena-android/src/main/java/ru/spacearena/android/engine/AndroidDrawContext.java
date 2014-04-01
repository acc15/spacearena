package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidDrawContext implements DrawContext {

    public static final int MATRIX_STACK_DEPTH = 40;
    private final Paint paint = new Paint();
    private Paint.Style style = null;

    private final AndroidPath path = new AndroidPath();

    private final android.graphics.Matrix[] matrixPool = new android.graphics.Matrix[MATRIX_STACK_DEPTH];
    private int matrixIndex = 0;

    private Canvas canvas;

    public AndroidDrawContext() {
        for (int i=0; i<matrixPool.length; i++) {
            matrixPool[i] = new android.graphics.Matrix();
        }
    }

    private void setMode(Paint.Style style) {
        if (this.style == style) {
            return;
        }
        paint.setStyle(this.style = style);
    }

    public AndroidDrawContext wrap(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public float getTextHeight() {
        final Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void fillRect(float left, float top, float right, float bottom) {
        setMode(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void fillCircle(float x, float y, float radius) {
        setMode(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void drawText(String text, float x, float y) {
        canvas.drawText(text, x, y - paint.getFontMetrics().top, paint);
    }

    public void drawRect(float left, float top, float right, float bottom) {
        setMode(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void drawImage(Image image, float x, float y) {
        canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, paint);
    }

    public void drawCircle(float x, float y, float radius) {
        setMode(Paint.Style.STROKE);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void pushMatrix(Matrix matrix) {
        final android.graphics.Matrix c = matrixPool[matrixIndex];
        ++matrixIndex;
        final android.graphics.Matrix m = matrixPool[matrixIndex];
        m.setConcat(c, ((AndroidMatrix)matrix).androidMatrix);
        canvas.setMatrix(m);
    }

    public void popMatrix() {
        canvas.setMatrix(matrixPool[--matrixIndex]);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public Path preparePath() {
        return path;
    }

    public void drawPath() {
        setMode(Paint.Style.STROKE);
        canvas.drawPath(path.androidPath, paint);
        path.androidPath.reset();
    }

    public void fillPath() {
        setMode(Paint.Style.FILL);
        canvas.drawPath(path.androidPath, paint);
        path.androidPath.reset();
    }

    public void setTextSize(float size) {
        paint.setTextSize(size);
    }

    public float getTextSize() {
        return paint.getTextSize();
    }

    public float getLineWidth() {
        return paint.getStrokeWidth();
    }

    public void setLineWidth(float width) {
        paint.setStrokeWidth(width);
    }

    public float getAlpha() {
        return paint.getAlpha();
    }

    public void setAlpha(float alpha) {
        paint.setAlpha((int)(alpha * 255f));
    }
}
