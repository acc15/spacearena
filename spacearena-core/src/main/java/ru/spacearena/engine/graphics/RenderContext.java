package ru.spacearena.engine.graphics;

import ru.spacearena.engine.primitives.Matrix2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public interface RenderContext {

    void setColor(Color color);
    void setTextSize(float textSize);
    void fillScreen();
    void drawText(String text, int x, int y);

    Matrix2F getMatrix();
    void setMatrix(Matrix2F matrix);

    void drawImage(Image image, Matrix2F matrix);
    void drawCircle(float x, float y, float size);
}
