package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.input.InputEvent;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class EngineContainer<T extends EngineObject> extends EngineObject {

    protected final List<T> children = new ArrayList<T>();

    public EngineContainer<T> add(T entity) {
        children.add(entity);
        return this;
    }

    @Override
    public void onInit(Engine engine) {
        for (T child : children) {
            child.onInit(engine);
        }
    }

    public void onSize(float width, float height) {
        for (T child : children) {
            child.onSize(width, height);
        }
    }

    public boolean onInput(InputEvent inputEvent) {
        for (T child : children) {
            if (child.onInput(inputEvent)) {
                return true;
            }
        }
        return false;
    }

    public boolean onUpdate(float seconds) {
        final Iterator<T> iterator = children.iterator();
        while (iterator.hasNext()) {
            final T child = iterator.next();
            if (!child.onUpdate(seconds)) {
                iterator.remove();
            }
        }
        return true;
    }


    public void onDraw(DrawContext context) {
        for (T child : children) {
            child.onDraw(context);
        }
    }
}
