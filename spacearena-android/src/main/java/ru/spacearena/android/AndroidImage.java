package ru.spacearena.android;

import android.graphics.Bitmap;
import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class AndroidImage implements Image {

    private Bitmap bitmap;

    public AndroidImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    @SuppressWarnings("unchecked")
    public <T> T getNativeImage(Class<T> clazz) {
        if (Bitmap.class.isAssignableFrom(clazz)) {
            return (T)bitmap;
        }
        throw new IllegalArgumentException();
    }
}
