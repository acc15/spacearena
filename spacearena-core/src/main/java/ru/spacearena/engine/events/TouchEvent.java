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
    private final int pointerIndex;

    public TouchEvent(Action action, int pointerCount, int pointerIndex) {
        super(InputType.TOUCH);
        this.action = action;
        this.pointerIndex = pointerIndex;
        pointerCoords = new float[pointerCount*2];
        pointerIds = new int[pointerCount];
    }

    private void checkIndex(int i) {
        if (i < 0 || i >= pointerIds.length)  {
            throw new IndexOutOfBoundsException("Illegal pointer index " + i);
        }
    }

    public void setPointer(int index, int id, float x, float y) {
        checkIndex(index);
        pointerCoords[index*2] = x;
        pointerCoords[index*2+1] = y;
        pointerIds[index] = id;
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
        return pointerIds[pointerIndex];
    }

    public int getPointerIndex() {
        return pointerIndex;
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
