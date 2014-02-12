package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.DrawContext;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-13-02
 */
public class EngineContainer<T extends EngineObject> extends EngineObject {

    private List<T> children = new ArrayList<T>();

    public static <T> List<T> emptyIfNull(List<T> entities) {
        return entities != null ? entities : Collections.<T>emptyList();
    }

    public List<T> getChildren() {
        return emptyIfNull(children);
    }

    public EngineContainer<T> add(T entity) {
        if (children == null) {
            children = new ArrayList<T>();
        }
        children.add(entity);
        return this;
    }

    public void onSize(float width, float height) {
        for (T child : getChildren()) {
            child.onSize(width, height);
        }
    }

    public boolean onTouch(Collection<Point2F> points) {
        for (T child : getChildren()) {
            if (child.onTouch(points)) {
                return true;
            }
        }
        return false;
    }

    public boolean onUpdate(float seconds) {
        final Iterator<T> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            final T child = iterator.next();
            if (!child.onUpdate(seconds)) {
                iterator.remove();
            }
        }
        return true;
    }

    public boolean onPreDraw(DrawContext context) {
        return true;
    }

    public void onPostDraw(DrawContext context) {
    }

    public void onDraw(DrawContext context) {
        for (T child : getChildren()) {
            if (!child.onPreDraw(context)) {
                continue;
            }
            try {
                child.onDraw(context);
            } finally {
                child.onPostDraw(context);
            }
        }
    }
}
