package ru.spacearena.engine.events.trackers;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.KeyEvent;
import ru.spacearena.engine.events.MouseEvent;

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
