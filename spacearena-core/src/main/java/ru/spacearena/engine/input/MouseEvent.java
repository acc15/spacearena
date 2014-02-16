package ru.spacearena.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public interface MouseEvent extends InputEvent {

    public static enum Action {
        CLICK,
        DOWN,
        UP,
        MOVE,
        ENTER,
        LEAVE,
        DRAG,
        WHEEL
    }

    public static final int LEFT_BUTTON = 0x00;
    public static final int MIDDLE_BUTTON = 0x01;
    public static final int RIGHT_BUTTON = 0x01;

    Action getAction();
    int getButton();
    float getX();
    float getY();

}
