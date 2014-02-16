package ru.spacearena.engine.input.trackers;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.input.InputEvent;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyEvent;

import java.util.HashSet;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class KeyTracker extends EngineObject {

    // TODO consider replacing this with more fast primitive collection (colt or trove)
    private HashSet<Integer> pressedKeys = new HashSet<Integer>();
    private boolean consumeEvent = false;

    public float getDirection(int negativeKeyCode, int positiveKeyCode, float amount) {
        float value = 0f;
        if (isKeyPressed(negativeKeyCode)) {
            value -= amount;
        }
        if (isKeyPressed(positiveKeyCode)) {
            value += amount;
        }
        return value;
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isConsumeEvent() {
        return consumeEvent;
    }

    public void setConsumeEvent(boolean consumeEvent) {
        this.consumeEvent = consumeEvent;
    }

    @Override
    public boolean onInput(InputEvent inputEvent) {
        if (inputEvent.getInputType() != InputType.KEYBOARD) {
            return false;
        }
        final KeyEvent keyEvent = (KeyEvent)inputEvent;
        final int keyCode = keyEvent.getKeyCode();
        switch (keyEvent.getAction()) {
        case UP: pressedKeys.remove(keyCode); break;
        case DOWN: pressedKeys.add(keyCode); break;
        }
        return consumeEvent;
    }
}
