package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.Image;
import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface EngineEnvironment {

    Matrix createMatrix();
    Image loadImage(String resource);
    void enableInput(InputType inputType);
    float getWidth();
    float getHeight();
}
