package ru.spacearena.engine;

import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public interface PlatformManager {

    Image loadImage(String name);
    Matrix createMatrix();

}
