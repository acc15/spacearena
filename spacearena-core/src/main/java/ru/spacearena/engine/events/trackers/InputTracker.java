package ru.spacearena.engine.events.trackers;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.KeyEvent;
import ru.spacearena.engine.events.MouseEvent;
import ru.spacearena.engine.events.TouchEvent;

import java.util.HashSet;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class InputTracker extends EngineObject {

    private static final int MAX_POINTER_COUNT = 50;

    // TODO consider replacing this with more fast primitive collection (colt or trove)
    private final HashSet<Integer> keyboardKeys = new HashSet<Integer>();
    private final HashSet<Integer> mouseKeys = new HashSet<Integer>();
    private final float[] pointers = new float[MAX_POINTER_COUNT];
    private int pointerCount = 0;

    private float mouseX, mouseY;
    private boolean consumeEvent = false;

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
        return keyboardKeys.contains(keyCode);
    }

    public boolean isMouseKeyPressed(int button) {
        return mouseKeys.contains(button);
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

    public void setConsumeEvent(boolean consumeEvent) {
        this.consumeEvent = consumeEvent;
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public boolean isPointerActive(int i) {
        return i < pointerCount;
    }

    public float getPointerX(int i) {
        return pointers[i*2];
    }

    public float getPointerY(int i) {
        return pointers[i*2+1];
    }

    @Override
    public boolean onInput(InputEvent inputEvent) {
        switch (inputEvent.getInputType()) {
            case KEYBOARD:
                final KeyEvent keyEvent = inputEvent.asKeyEvent();
                final int keyCode = keyEvent.getKeyCode();
                switch (keyEvent.getAction()) {
                case UP: keyboardKeys.remove(keyCode); break;
                case DOWN: keyboardKeys.add(keyCode); break;
                }
                break;

            case MOUSE:
                final MouseEvent mouseEvent = inputEvent.asMouseEvent();
                switch (mouseEvent.getAction()) {
                case UP: mouseKeys.remove(mouseEvent.getButton()); break;
                case DOWN: mouseKeys.add(mouseEvent.getButton()); break;
                }
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
                break;

            case TOUCH:
                final TouchEvent touchEvent = inputEvent.asTouchEvent();
                System.arraycopy(touchEvent.getRawPointers(), 0, pointers, 0, touchEvent.getPointerCount()*2);
                pointerCount = touchEvent.getPointerCount();
                break;
        }
        return consumeEvent;
    }

}
