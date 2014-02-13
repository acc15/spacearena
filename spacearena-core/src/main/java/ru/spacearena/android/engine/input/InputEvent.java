package ru.spacearena.android.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class InputEvent {

    private InputType inputType;
    private long eventTime;

    public InputEvent(InputType inputType, long eventTime) {
        this.inputType = inputType;
        this.eventTime = eventTime;
    }

    public KeyboardEvent asKeyboardEvent() {
        return (KeyboardEvent) this;
    }

}
