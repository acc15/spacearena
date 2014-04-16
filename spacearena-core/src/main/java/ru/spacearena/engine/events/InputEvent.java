package ru.spacearena.engine.events;

import ru.spacearena.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class InputEvent implements EngineEvent {

    private InputType inputType;

    public InputEvent(InputType inputType) {
        this.inputType = inputType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public MouseEvent asMouseEvent() { return inputType == InputType.MOUSE ? (MouseEvent)this : null; }
    public KeyEvent asKeyEvent() { return inputType == InputType.KEYBOARD ? (KeyEvent)this : null; }
    public TouchEvent asTouchEvent() { return inputType == InputType.TOUCH ? (TouchEvent)this : null; }

    public void run(Engine engine) {
        engine.onInput(this);
    }

}
