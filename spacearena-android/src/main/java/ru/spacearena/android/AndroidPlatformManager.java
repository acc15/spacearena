package ru.spacearena.android;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.PlatformManager;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class AndroidPlatformManager implements PlatformManager {

    private Resources resources;
    private String packageName;

    public AndroidPlatformManager(Resources resources, String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    public Image loadImage(String name) {
        final int imageId = resources.getIdentifier(name, "drawable", packageName);
        return new AndroidImage(BitmapFactory.decodeResource(resources, imageId));
    }

    public Matrix createMatrix() {
        return new AndroidMatrix();
    }
}
