package ru.spacearena.engine.geometry.shapes;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-04
 */
public interface Rect2I extends PackedRect2I {

    int getLeft();
    int getTop();
    int getRight();
    int getBottom();

    boolean contains(Rect2I r);

    void setBounds(int l, int t, int r, int b);
    void setRect(int x, int y, int w, int h);
}
