package ru.spacearena.engine.input.trackers;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.input.InputEvent;
import ru.spacearena.engine.input.KeyEvent;
import ru.spacearena.engine.input.MouseEvent;

import java.util.HashSet;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class InputTracker extends EngineObject {

    // TODO consider replacing this with more fast primitive collection (colt or trove)
    private HashSet<Integer> keyboardKeys = new HashSet<Integer>();
    private HashSet<Integer> mouseKeys = new HashSet<Integer>();

    private float mouseX, mouseY;
    private boolean consumeEvent = false;

    public float getKeyboardDirection(int negativeKeyCode, int positiveKeyCode, float amount) {
        float value = 0f;
        if (isKeyboardKeyPressed(negativeKeyCode)) {
            value -= amount;
        }
        if (isKeyboardKeyPressed(positiveKeyCode)) {
            value += amount;
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

    @Override
    public boolean onInput(InputEvent inputEvent) {
        switch (inputEvent.getInputType()) {
            case KEYBOARD:
                final KeyEvent keyEvent = (KeyEvent)inputEvent;
                final int keyCode = keyEvent.getKeyCode();
                switch (keyEvent.getAction()) {
                case UP: keyboardKeys.remove(keyCode); break;
                case DOWN: keyboardKeys.add(keyCode); break;
                }
                break;

            case MOUSE:
                final MouseEvent mouseEvent = (MouseEvent)inputEvent;
                switch (mouseEvent.getAction()) {
                case UP: mouseKeys.remove(mouseEvent.getButton()); break;
                case DOWN: mouseKeys.add(mouseEvent.getButton()); break;
                }
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
                break;

        }
        return consumeEvent;
    }

}
