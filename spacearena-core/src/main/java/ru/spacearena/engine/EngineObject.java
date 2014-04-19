package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.util.BitUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class EngineObject implements EngineEntity {

    private int state = VISIBLE | LIVE;

    private static final int VISIBLE = 0x01;
    private static final int LIVE =    0x02;

    public void onAttach(Engine engine) {
    }

    public void onDetach(Engine engine) {
    }

    public void onInit(DrawContext2f context) {
    }

    public void onDispose(DrawContext2f context) {
    }

    public void onSize(float width, float height) {
    }

    public boolean onInput(InputEvent inputEvent) {
        return false;
    }

    public void onUpdate(float seconds) {
    }

    public void onDraw(DrawContext2f context) {
    }

    public boolean isVisible() {
        return BitUtils.getBit(state, VISIBLE);
    }

    public boolean isLive() {
        return BitUtils.getBit(state, LIVE);
    }

    public void setVisible(boolean visible) {
        state = BitUtils.setBit(state, VISIBLE, visible);
    }

    public void kill() {
        state = BitUtils.setBit(state, LIVE, false);
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

}
