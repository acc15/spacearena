package ru.spacearena.java2d.engine.input;

import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Java2DKeyEvent implements KeyEvent {

    private java.awt.event.KeyEvent event;
    private Action action;

    public KeyEvent init(java.awt.event.KeyEvent event, Action action) {
        this.event = event;
        this.action = action;
        return this;
    }

    public int getKeyCode() {
        return event.getKeyCode();
    }

    public Action getAction() {
        return action;
    }

    public char getKeyChar() {
        return event.getKeyChar();
    }

    public InputType getInputType() {
        return InputType.KEYBOARD;
    }

}
