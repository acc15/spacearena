package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface EngineFactory {

    Matrix createMatrix();
    //Image loadImage(String resource);
    void enableInput(InputType inputType);

}
