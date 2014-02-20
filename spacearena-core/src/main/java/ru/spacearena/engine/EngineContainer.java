package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.input.InputEvent;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class EngineContainer<T extends EngineEntity> implements EngineEntity {

    protected final List<T> children = new ArrayList<T>();
    protected Engine engine = null;

    public void add(T entity) {
        if (engine != null) {
            entity.onInit(engine);
        }
        children.add(entity);
    }

    public int getChildrenCount() {
        return children.size();
    }

    public List<T> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    public <X extends EngineObject> X get(int index) {
        return (X)children.get(index);
    }

    public void onInit(Engine engine) {
        if (this.engine != null) {
            throw new IllegalStateException("Already initialized");
        }
        this.engine = engine;
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
