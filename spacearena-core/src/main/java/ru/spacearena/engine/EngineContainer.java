package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.events.InputEvent;

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
        onAttach(entity);
        children.add(entity);
    }

    protected void onAttach(T entity) {
    }

    protected void onDetach(T entity) {
    }

    public int getChildrenCount() {
        return children.size();
    }

    public List<T> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    public <X extends EngineEntity> X getChild(int index) {
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
                onDetach(child);
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
