package ru.spacearena.android;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.resources.ResourceManager;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class AndroidResourceManager implements ResourceManager {

    private Resources resources;
    private String packageName;

    public AndroidResourceManager(Resources resources, String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    public Image getImage(String name) {
        final int imageId = resources.getIdentifier(name, "drawable", packageName);
        return new AndroidImage(BitmapFactory.decodeResource(resources, imageId));
    }
}
