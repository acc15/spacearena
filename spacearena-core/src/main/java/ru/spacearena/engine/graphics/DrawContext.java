package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface DrawContext {

    void setColor(int color);
    void fillRect(float left, float top, float right, float bottom);
    void fillCircle(float x, float y, float radius);

    void drawImage(Image image, float x, float y);
    void drawText(String text, float x, float y);
    void drawRect(float left, float top, float right, float bottom);

    float getTextHeight();
    void setMatrix(Matrix matrix);

    Matrix getMatrixCopy();

    void drawLine(float x1, float y1, float x2, float y2);

    void setTextSize(float size);
    float getTextSize();

    void drawPoly(float[] points);

    float getLineWidth();
    void setLineWidth(float width);
}
