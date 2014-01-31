package ru.spacearena.swing;

import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.PlatformManager;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-01
 */
public class Java2DPlatformManager implements PlatformManager {

    public Image loadImage(String name) {
        // TODO implement..
        return null;
    }

    public Matrix createMatrix() {
        return new Java2DMatrix();
    }
}
