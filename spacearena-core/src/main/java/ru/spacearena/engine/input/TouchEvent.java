package ru.spacearena.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class TouchEvent extends InputEvent {

    public static enum Action {
        DOWN,
        UP,
        MOVE
    }

    private int pointerIndex;
    private Action action;
    private float x, y;

    @Override
    public InputType getInputType() {
        return InputType.TOUCH;
    }

    public int getPointerIndex() {
        return pointerIndex;
    }

    public void setPointerId(int pointerIndex) {
        this.pointerIndex = pointerIndex;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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
}
