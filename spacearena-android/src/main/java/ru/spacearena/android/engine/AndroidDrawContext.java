package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Paint;
import ru.spacearena.android.engine.graphics.DrawContext;
import ru.spacearena.android.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidDrawContext implements DrawContext {
    public static final int DEFAULT_TEXT_SIZE = 30;
    private Canvas canvas;
    private final Paint paint = new Paint();
    private float fontHeight;

    public AndroidDrawContext() {
        paint.setTextSize(DEFAULT_TEXT_SIZE);
        final Paint.FontMetrics fm = paint.getFontMetrics();
        fontHeight = -fm.top;
    }

    public DrawContext wrap(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void fillRect(float left, float top, float right, float bottom) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void fill() {
        canvas.drawColor(paint.getColor());
    }

    public void drawText(String text, float x, float y) {
        canvas.drawText(text, x, y + fontHeight, paint);
    }

    public void setMatrix(Matrix matrix) {
        canvas.setMatrix(((AndroidMatrix)matrix).matrix);
    }

    public Matrix getMatrix() {
        return new AndroidMatrix(canvas.getMatrix());
    }
}
