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
    private Engine engine = null;

    public EngineContainer() {
    }

    public EngineContainer(Engine engine) {
        this.engine = engine;
    }

    public Engine getEngine() {
        if (engine == null) {
            throw new NullPointerException("Attempt to get engine before object initialization");
        }
        return engine;
    }

    public void add(T entity) {
        if (engine != null) {
            attach(entity);
        }
        onAddChild(entity);
        children.add(entity);
    }

    public void remove(int index) {
        final T e = children.get(index);
        if (engine != null) {
            detach(e);
        }
        onRemoveChild(e);
        children.remove(index);
    }

    private void detach(T entity) {
        onDetachChild(entity);
        entity.onDetach(engine);
    }

    private void attach(T entity) {
        entity.onAttach(engine);
        onAttachChild(entity);
    }

    protected void onAddChild(T entity) {
    }

    protected void onRemoveChild(T entity) {
    }

    protected void onAttachChild(T entity) {
    }

    protected void onDetachChild(T entity) {
    }

    public int getChildCount() {
        return children.size();
    }

    public Iterable<T> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    public T getChild(int index) {
        return children.get(index);
    }

    public void onAttach(Engine engine) {
        if (this.engine == engine) {
            return;
        } else if (this.engine != null) {
            throw new IllegalStateException("Already initialized");
        }
        this.engine = engine;
        for (T child : children) {
            attach(child);
        }
    }

    public void onDetach(Engine engine) {
        if (this.engine != engine) {
            throw new IllegalStateException("Not initialized");
        }
        for (T child: children) {
            detach(child);
        }
        this.engine = null;
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
        int l = 0, r = children.size();
        while (l < r) {
            final T c = children.get(l);
            if (!c.onUpdate(seconds)) {
                onDetachChild(c);
                children.remove(l);
                --r;
            } else {
                ++l;
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
