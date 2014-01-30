package ru.spacearena.android;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.Matrix2F;

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

    private Matrix toAndroidMatrix(Matrix2F matrix) {
        final Matrix mx = new Matrix();
        mx.setValues(matrix.getValues());
        return mx;
    }

    public Matrix2F getMatrix() {
        final Matrix mx = canvas.getMatrix();
        final float[] values = new float[9];
        mx.getValues(values);
        return new Matrix2F(values);
    }

    public void setMatrix(Matrix2F matrix) {
        canvas.setMatrix(toAndroidMatrix(matrix));
    }

    public void drawImage(Image image, Matrix2F matrix) {
        if (!(image instanceof AndroidImage)) {
            throw new IllegalArgumentException("Image of unsupported type. AndroidImage expected");
        }
        canvas.drawBitmap(((AndroidImage)image).getBitmap(), toAndroidMatrix(matrix), paint);
    }

    public void drawCircle(float x, float y, float size) {
        canvas.drawCircle(x, y, size, paint);
    }
}
