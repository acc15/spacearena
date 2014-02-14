package ru.spacearena.engine.input;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public abstract class InputEvent {

    private long eventTime;

    public abstract InputType getInputType();

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public TouchEvent asTouchEvent() {
        return (TouchEvent) this;
    }

}
