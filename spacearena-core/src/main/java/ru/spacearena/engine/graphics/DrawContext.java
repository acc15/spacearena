package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public interface DrawContext {

    void setColor(int color);

    void drawLine(float x1, float y1, float x2, float y2);
    void drawImage(Image image, float x, float y);
    void drawText(String text, float x, float y);

    void drawRect(float left, float top, float right, float bottom);
    void fillRect(float left, float top, float right, float bottom);

    void drawCircle(float x, float y, float radius);
    void fillCircle(float x, float y, float radius);

    Path preparePath();
    void drawPath();
    void fillPath();

    float getTextHeight();

    void pushMatrix(Matrix matrix);
    void popMatrix();

    void setTextSize(float size);
    float getTextSize();

    float getLineWidth();
    void setLineWidth(float width);

    float getAlpha();
    void setAlpha(float alpha);

}
