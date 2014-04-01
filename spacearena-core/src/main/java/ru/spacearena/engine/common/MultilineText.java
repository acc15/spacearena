package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.ColorU;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class MultilineText extends EngineContainer<MultilineText.Line> {

    private float x = 0, y = 0;
    private int color = ColorU.WHITE;

    public static class Line extends EngineObject {

        private String text = "";

        public Line() {
        }

        public Line(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void onDraw(DrawContext context) {
        float y = this.y;
        context.setColor(color);
        context.setTextSize(12f);
        for (int i=0; i < getChildCount(); i++) {
            final Line line = getChild(i);
            context.drawText(line.text, x, y);
            y += context.getTextHeight();
        }
    }
}
