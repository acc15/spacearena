package ru.spacearena.engine.events.trackers;

import cern.colt.map.OpenIntIntHashMap;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.KeyEvent;
import ru.spacearena.engine.events.MouseEvent;
import ru.spacearena.engine.events.TouchEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class InputTracker extends EngineObject {

    private static final int DUMMY_INT = 0;
    private static final int MAX_POINTER_COUNT = 50;

    private final OpenIntIntHashMap keyboardKeys = new OpenIntIntHashMap();
    private final OpenIntIntHashMap mouseKeys = new OpenIntIntHashMap();

    private final float[] pointers = new float[MAX_POINTER_COUNT<<1];
    private final int[] pointerSequence = new int[MAX_POINTER_COUNT];
    private int pointerCount = 0;

    private float mouseX, mouseY;
    private final boolean consumeEvent;

    public InputTracker() {
        this(false);
    }

    public InputTracker(boolean consumeEvent) {
        this.consumeEvent = consumeEvent;
    }

    public int getKeyboardDirection(int negativeKeyCode, int positiveKeyCode) {
        int value = 0;
        if (isKeyboardKeyPressed(negativeKeyCode)) {
            value -= 1;
        }
        if (isKeyboardKeyPressed(positiveKeyCode)) {
            value += 1;
        }
        return value;
    }

    public boolean isKeyboardKeyPressed(int keyCode) {
        return keyboardKeys.containsKey(keyCode);
    }

    public boolean isMouseKeyPressed(int button) {
        return mouseKeys.containsKey(button);
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public boolean isConsumeEvent() {
        return consumeEvent;
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public boolean isPointerActive(int i) {
        return i < pointerCount;
    }

    public float getPointerX(int i) {
        return pointers[pointerSequence[i] << 1];
    }

    public float getPointerY(int i) {
        return pointers[(pointerSequence[i] << 1) + 1];
    }

    @Override
    public boolean onInput(InputEvent inputEvent) {
        switch (inputEvent.getInputType()) {
            case KEYBOARD:
                final KeyEvent keyEvent = inputEvent.asKeyEvent();
                final int keyCode = keyEvent.getKeyCode();
                switch (keyEvent.getAction()) {
                case UP: keyboardKeys.removeKey(keyCode); break;
                case DOWN: keyboardKeys.put(keyCode, DUMMY_INT); break;
                }
                break;

            case MOUSE:
                final MouseEvent mouseEvent = inputEvent.asMouseEvent();
                final int button = mouseEvent.getButton();
                switch (mouseEvent.getAction()) {
                case UP: mouseKeys.removeKey(button); break;
                case DOWN: mouseKeys.put(button, DUMMY_INT); break;
                }
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
                break;

            case TOUCH:
                final TouchEvent touchEvent = inputEvent.asTouchEvent();

                final float[] rawPointers = touchEvent.getRawPointers();
                final int bufSize = rawPointers.length;
                if (touchEvent.getAction() == TouchEvent.Action.MOVE) {
                    System.arraycopy(rawPointers, 0, pointers, 0, bufSize);
                    break;
                }
                if (touchEvent.getAction() == TouchEvent.Action.CANCEL) {
                    pointerCount = 0;
                    break;
                }

                switch (touchEvent.getAction()) {
                case DOWN:
                    final int downIndex = touchEvent.getPointerIndex();
                    System.arraycopy(rawPointers, 0, pointers, 0, bufSize);
                    for (int i=0; i<pointerCount; i++) {
                        if (pointerSequence[i] >= downIndex) {
                            ++pointerSequence[i];
                        }
                    }
                    pointerSequence[pointerCount] = downIndex;
                    ++pointerCount;
                    break;

                case UP:
                    final int upIndex = touchEvent.getPointerIndex();
                    final int pointerPosition = upIndex << 1;
                    System.arraycopy(rawPointers, 0, pointers, 0, pointerPosition);
                    System.arraycopy(rawPointers, pointerPosition+2, pointers, pointerPosition,
                            bufSize - pointerPosition - 2);
                    --pointerCount;

                    boolean found = false;
                    for (int i=0; i<pointerCount; i++) {
                        if (pointerSequence[i] == upIndex) {
                            found = true;
                        }
                        if (found) {
                            pointerSequence[i] = pointerSequence[i+1];
                        }
                        if (pointerSequence[i] > upIndex) {
                            --pointerSequence[i];
                        }
                    }
                    break;

                case MOVE:
                    System.arraycopy(rawPointers, 0, pointers, 0, bufSize);
                    break;

                case CANCEL:
                    pointerCount = 0;
                    break;
                }
                break;
        }
        return consumeEvent;
    }

}
