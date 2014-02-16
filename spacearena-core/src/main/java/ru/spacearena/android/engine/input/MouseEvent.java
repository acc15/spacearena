package ru.spacearena.android.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class MouseEvent implements InputEvent {

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

    public static final int NO_BUTTON = 0;
    public static final int LEFT_BUTTON = 1;
    public static final int RIGHT_BUTTON = 2;
    public static final int MIDDLE_BUTTON = 3;

    private Action action;
    private int button;
    private float x, y;

    public MouseEvent(Action action, int button, float x, float y) {
        this.action = action;
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public Action getAction() {
        return action;
    }

    public int getButton() {
        return button;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public InputType getInputType() {
        return InputType.MOUSE;
    }
}
