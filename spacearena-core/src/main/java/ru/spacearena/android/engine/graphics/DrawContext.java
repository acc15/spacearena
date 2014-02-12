package ru.spacearena.android.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface DrawContext {

    void setColor(int color);
    void fillRect(float left, float top, float right, float bottom);
    void fill();

    void drawText(String text, float x, float y);

    void setMatrix(Matrix matrix);
    Matrix getMatrix();

}
