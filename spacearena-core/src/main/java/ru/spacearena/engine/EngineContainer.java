package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext2f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class EngineContainer<T extends EngineEntity> extends EngineObject {

    protected final List<T> children = new ArrayList<T>();
    private Engine engine = null;

    public Engine getEngine() {
        if (engine == null) {
            throw new IllegalStateException("Object is detached from engine");
        }
        return engine;
    }

    public void add(T entity) {
        if (engine != null) {
            attach(entity);
        }
        children.add(entity);
    }

    public void remove(int index) {
        final T e = children.get(index);
        if (engine != null) {
            detach(e);
        }
        children.remove(index);
    }

    private void detach(T entity) {
        onDetachChild(entity);
        final DrawContext2f dc = engine.getDrawContext();
        if (dc != null) {
            entity.onDispose(dc);
        }
        entity.onDetach(engine);
    }

    private void attach(T entity) {
        entity.onAttach(engine);
        final DrawContext2f dc = engine.getDrawContext();
        if (dc != null) {
            entity.onInit(dc);
        }
        onAttachChild(entity);
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
        if (this.engine != null) {
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

    public void onInit(DrawContext2f context) {
        for (T child: children) {
            child.onInit(context);
        }
    }

    public void onDispose(DrawContext2f context) {
        for (T child: children) {
            child.onDispose(context);
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

    public void onUpdate(float seconds) {
        int l = 0, r = children.size();
        while (l < r) {
            final T c = children.get(l);
            c.onUpdate(seconds);
            if (!c.isLive()) {
                onDetachChild(c);
                children.remove(l);
                --r;
            } else {
                ++l;
            }
        }
    }

    public void onDraw(DrawContext2f context) {
        for (T child : children) {
            if (child.isVisible()) {
                child.onDraw(context);
            }
        }
    }
}
