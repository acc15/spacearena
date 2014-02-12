package ru.spacearena.java2d.engine;

import ru.spacearena.android.engine.EngineFactory;
import ru.spacearena.android.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Java2DEngineFactory implements EngineFactory {
    public Matrix createMatrix() {
        return new Java2DMatrix();
    }
}
