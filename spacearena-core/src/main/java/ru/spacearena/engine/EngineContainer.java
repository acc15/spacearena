package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class EngineContainer<T extends EngineEntity> implements EngineEntity {

    protected final List<T> children = new ArrayList<T>();
    private DrawContext context = null;

    public EngineContainer() {
    }

    public void add(T entity) {
        if (context != null) {
            attach(entity);
        }
        children.add(entity);
    }

    public void remove(int index) {
        final T e = children.get(index);
        if (context != null) {
            detach(e);
        }
        children.remove(index);
    }

    private void detach(T entity) {
        onDetachChild(entity);
        entity.onDispose(context);
    }

    private void attach(T entity) {
        entity.onInit(context);
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

    public void onInit(DrawContext context) {
        if (this.context == context) {
            return;
        } else if (this.context != null) {
            throw new IllegalStateException("Already initialized");
        }
        this.context = context;
        for (T child : children) {
            attach(child);
        }
    }

    public void onDispose(DrawContext context) {
        if (this.context != context) {
            throw new IllegalStateException("Not initialized");
        }
        for (T child: children) {
            detach(child);
        }
        this.context = null;
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
