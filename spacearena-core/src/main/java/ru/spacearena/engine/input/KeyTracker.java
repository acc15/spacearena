package ru.spacearena.engine.input;

import ru.spacearena.engine.EngineObject;

import java.util.HashSet;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class KeyTracker extends EngineObject {

    private HashSet<Integer> pressedKeys = new HashSet<Integer>();
    private boolean consumeEvent = false;

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
