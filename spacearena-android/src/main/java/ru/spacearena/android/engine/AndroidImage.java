package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import ru.spacearena.android.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidImage implements Image {

    Bitmap bitmap;

    public AndroidImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getWidth() {
        return bitmap.getWidth();
    }

    public float getHeight() {
        return bitmap.getHeight();
    }
}
