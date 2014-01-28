package ru.spacearena.android.engine.events;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class FrameEvent {

    private float timeDelta;
    private int width, height;

    public FrameEvent(float timeDelta, int width, int height) {
        this.timeDelta = timeDelta;
        this.width = width;
        this.height = height;
    }
}
