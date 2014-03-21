package ru.spacearena.engine.events;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class TouchEvent extends InputEvent {

    public static enum Action {
        DOWN,
        UP,
        MOVE,
        CANCEL
    }

    private final float[] pointerCoords;
    private final int[] pointerIds;
    private final Action action;
    private final int pointerId;

    public TouchEvent(Action action, int pointerCount, int pointerId) {
        super(InputType.TOUCH);
        this.action = action;
        this.pointerId = pointerId;
        pointerCoords = new float[pointerCount*2];
        pointerIds = new int[pointerCount];
    }

    private void checkIndex(int i) {
        if (i < 0 || i >= pointerIds.length)  {
            throw new IndexOutOfBoundsException("Illegal pointer " + i);
        }
    }

    public void setPointer(int i, int id, float x, float y) {
        checkIndex(i);
        pointerCoords[i*2] = x;
        pointerCoords[i*2+1] = y;
        pointerIds[i] = id;
    }

    public int findPointerIndex(int id) {
        for (int i=0; i<pointerIds.length; i++) {
            if (pointerIds[i] == id) {
                return i;
            }
        }
        return -1;
    }

    public int getPointerCount() {
        return pointerIds.length;
    }

    public float[] getRawPointers() {
        return pointerCoords;
    }

    public int getPointerId() {
        return pointerId;
    }

    public int getPointerIndex() {
        return findPointerIndex(pointerId);
    }

    public Action getAction() {
        return action;
    }

    public float getPointerY(int i) {
        checkIndex(i);
        return pointerCoords[i*2+1];
    }

    public float getPointerX(int i) {
        checkIndex(i);
        return pointerCoords[i*2];
    }

    public int getPointerId(int i) {
        checkIndex(i);
        return pointerIds[i];
    }


}
