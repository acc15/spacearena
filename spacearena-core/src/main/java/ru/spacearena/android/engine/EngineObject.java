package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.DrawContext;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class EngineObject {

    private List<EngineObject> children = null;

    public static <T> List<T> emptyIfNull(List<T> entities) {
        return entities != null ? entities : Collections.<T>emptyList();
    }

    public List<EngineObject> getChildren() {
        return emptyIfNull(children);
    }

    public EngineObject add(EngineObject entity) {
        if (children == null) {
            children = new ArrayList<EngineObject>();
        }
        children.add(entity);
        return this;
    }

    public void onSize(float width, float height) {
        for (EngineObject child : getChildren()) {
            child.onSize(width, height);
        }
    }

    public boolean onTouch(Collection<Point2F> points) {
        for (EngineObject child : getChildren()) {
            if (child.onTouch(points)) {
                return true;
            }
        }
        return false;
    }

    public boolean onUpdate(float seconds) {
        final Iterator<EngineObject> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            final EngineObject child = iterator.next();
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
        for (EngineObject child : getChildren()) {
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
