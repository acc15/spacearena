package ru.spacearena.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public interface KeyEvent extends InputEvent {

    public static enum Action {
        UP,
        TYPED,
        DOWN
    }

    int getKeyCode();
    Action getAction();
    char getKeyChar();
}
