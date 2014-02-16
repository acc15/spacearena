package ru.spacearena.java2d.engine.input;

import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.MouseEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Java2DMouseEvent implements MouseEvent {

    private java.awt.event.MouseEvent mouseEvent;
    private Action action;

    public MouseEvent init(java.awt.event.MouseEvent event, Action action) {
        this.mouseEvent = event;
        this.action = action;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public int getButton() {
        return mouseEvent.getButton();
    }

    public float getX() {
        return mouseEvent.getX();
    }

    public float getY() {
        return mouseEvent.getY();
    }

    public InputType getInputType() {
        return InputType.MOUSE;
    }
}
