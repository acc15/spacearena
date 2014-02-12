package ru.spacearena.android.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface DrawContext {

    void setColor(int color);
    void fillRect(float left, float top, float right, float bottom);
    void fillCircle(float x, float y, float radius);
    void fill();

    void drawText(String text, float x, float y);
    float getTextHeight();

    void setMatrix(Matrix matrix);
    Matrix getMatrix();

}
