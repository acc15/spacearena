package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class AndroidEngineFactory implements EngineFactory {
    public Matrix createMatrix() {
        return new AndroidMatrix();
    }
}