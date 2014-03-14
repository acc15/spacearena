package ru.spacearena.engine.events;

import ru.spacearena.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class InputEvent implements EngineEvent {

    private InputType inputType;

    public InputEvent(InputType inputType) {
        this.inputType = inputType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void run(Engine engine) {
        engine.onInput(this);
    }

}
