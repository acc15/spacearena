package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.util.BitUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class EngineObject implements EngineEntity {

    private int state = 0;

    private static final int HIDDEN = 0x01;
    private static final int DELETED = 0x02;

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

    public boolean isHidden() {
        return BitUtils.getBit(state, HIDDEN);
    }

    public boolean isDeleted() {
        return BitUtils.getBit(state, DELETED);
    }

    public void setHidden(boolean hidden) {
        state = BitUtils.setBit(state, HIDDEN, hidden);
    }

    public void setVisible(boolean visible) {
        setHidden(!visible);
    }

    public void delete() {
        state = BitUtils.setBit(state, DELETED, true);
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

}
