package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class MultilineText extends EngineContainer<MultilineText.Line> {

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


    private float x = 0, y = 0;
    private Color color = Color.WHITE;

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void onDraw(DrawContext context) {
        float y = this.y;
        context.color(color).fontSize(32, DrawContext.Unit.SP);
        for (int i=0; i < getChildCount(); i++) {
            final Line line = getChild(i);
            context.drawText(line.text, x, y);
            y += context.getFont().getLineHeight();
        }
    }

}
