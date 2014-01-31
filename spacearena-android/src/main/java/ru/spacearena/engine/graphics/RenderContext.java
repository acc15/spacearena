package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public interface RenderContext {

    void setColor(Color color);
    void setTextSize(float textSize);
    void fillScreen();
    void drawText(String text, int x, int y);

    Matrix getMatrix();
    void setMatrix(Matrix matrix);

    void drawImage(Image image, Matrix matrix);
    void drawCircle(float x, float y, float size);
}
