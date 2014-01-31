package ru.spacearena.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.graphics.RenderContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class AndroidRenderContext implements RenderContext {
    private Canvas canvas;
    private Paint paint = new Paint();

    public AndroidRenderContext(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setColor(Color color) {
        paint.setColor(color.getValue());
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
    }

    public void fillScreen() {
        canvas.drawColor(paint.getColor());
    }

    public void drawText(String text, int x, int y) {
        canvas.drawText(text, x, y, paint);
    }

    public Matrix getMatrix() {
        return new AndroidMatrix(canvas.getMatrix());
    }

    public void setMatrix(Matrix matrix) {
        canvas.setMatrix(matrix.getNativeMatrix(android.graphics.Matrix.class));
    }

    public void drawImage(Image image, Matrix matrix) {
        canvas.drawBitmap(
                image.getNativeImage(Bitmap.class),
                matrix.getNativeMatrix(android.graphics.Matrix.class),
                paint);
    }

    public void drawCircle(float x, float y, float size) {
        canvas.drawCircle(x, y, size, paint);
    }
}
