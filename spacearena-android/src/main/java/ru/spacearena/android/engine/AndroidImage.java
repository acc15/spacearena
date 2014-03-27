package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidImage implements Image {

    final Bitmap bitmap;

    public AndroidImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public DrawContext createContext() {
        return new AndroidDrawContext().wrap(new Canvas(bitmap));
    }
}
