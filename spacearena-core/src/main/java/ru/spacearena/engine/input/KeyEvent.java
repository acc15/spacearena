package ru.spacearena.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class KeyEvent implements InputEvent {

    public static enum Action {
        UP,
        TYPED,
        DOWN
    }

    private int keyCode;
    private char keyChar;
    private Action action;

    public KeyEvent(Action action, int keyCode, char keyChar) {
        this.action = action;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    public InputType getInputType() {
        return InputType.KEYBOARD;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public Action getAction() {
        return action;
    }
}
