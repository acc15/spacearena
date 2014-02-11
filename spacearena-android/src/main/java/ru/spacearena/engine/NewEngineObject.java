package ru.spacearena.engine;

import android.graphics.Canvas;
import ru.spacearena.engine.handlers.DrawHandler;
import ru.spacearena.engine.handlers.SizeHandler;
import ru.spacearena.engine.handlers.TouchHandler;
import ru.spacearena.engine.handlers.UpdateHandler;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class NewEngineObject implements EngineEntity {

    private Map<Class<?>, List<?>> listeners = null;
    private List<EngineEntity> children = null;

    public static <T> List<T> emptyIfNull(List<T> entities) {
        return entities != null ? entities : Collections.<T>emptyList();
    }

    public List<EngineEntity> getChildren() {
        return emptyIfNull(children);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> castList(List<?> list) {
        return (List<T>) list;
    }

    public <T> List<T> getHandlers(Class<T> clazz) {
        if (listeners == null) {
            return Collections.emptyList();
        }
        return emptyIfNull(NewEngineObject.<T>castList(listeners.get(clazz)));
    }

    private <T> void removeHandler(Class<T> clazz, T listener) {
        if (listeners == null) {
            return;
        }
        final List<T> list = castList(listeners.get(clazz));
        if (list == null) {
            return;
        }
        list.remove(listener);
    }

    private <T> void addHandler(Class<T> clazz, T listener) {
        if (listeners == null) {
            listeners = new HashMap<Class<?>, List<?>>();
        }
        List<T> list = castList(listeners.get(clazz));
        if (list == null) {
            list = new ArrayList<T>();
            listeners.put(clazz, list);
        }
        list.add(listener);
    }

    public void addUpdateHandler(UpdateHandler updateHandler) {
        addHandler(UpdateHandler.class, updateHandler);
    }

    public void addDrawHandler(DrawHandler drawHandler) {
        addHandler(DrawHandler.class, drawHandler);
    }

    public void addSizeHandler(SizeHandler sizeHandler) {
        addHandler(SizeHandler.class, sizeHandler);
    }

    public void addTouchHandler(TouchHandler touchHandler) {
        addHandler(TouchHandler.class, touchHandler);
    }

    public void removeUpdateHandler(UpdateHandler updateHandler) {
        removeHandler(UpdateHandler.class, updateHandler);
    }

    public void removeDrawHandler(DrawHandler drawHandler) {
        removeHandler(DrawHandler.class, drawHandler);
    }

    public void removeSizeHandler(SizeHandler sizeHandler) {
        removeHandler(SizeHandler.class, sizeHandler);
    }

    public void removeTouchHandler(TouchHandler touchHandler) {
        removeHandler(TouchHandler.class, touchHandler);
    }

    public void onSize(Point2F newSize) {
        for (EngineEntity child : getChildren()) {
            child.onSize(newSize);
        }
        for (SizeHandler sizeHandler : getHandlers(SizeHandler.class)) {
            sizeHandler.onSize(newSize);
        }
    }

    public boolean onTouch(Collection<Point2F> points) {
        for (EngineEntity child : getChildren()) {
            if (child.onTouch(points)) {
                return true;
            }
        }
        for (TouchHandler touchHandler : getHandlers(TouchHandler.class)) {
            if (touchHandler.onTouch(points)) {
                return true;
            }
        }
        return false;
    }

    public boolean onUpdate(float seconds) {
        final Iterator<EngineEntity> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            final EngineEntity child = iterator.next();
            if (!child.onUpdate(seconds)) {
                iterator.remove();
            }
        }
        for (UpdateHandler handler : getHandlers(UpdateHandler.class)) {
            if (!handler.onUpdate(seconds)) {
                return false;
            }
        }
        return true;
    }

    public void onPreDraw(Canvas canvas) {
    }

    public void onPostDraw(Canvas canvas) {
    }

    public void onDraw(Canvas canvas) {
        int pos = 0;
        final List<DrawHandler> drawHandlers = getHandlers(DrawHandler.class);
        try {
            while (pos < drawHandlers.size()) {
                drawHandlers.get(pos).onPreDraw(canvas);
                ++pos;
            }
            for (EngineEntity child : getChildren()) {
                try {
                    child.onPreDraw(canvas);
                    child.onDraw(canvas);
                } finally {
                    child.onPostDraw(canvas);
                }
            }
            for (DrawHandler dh : drawHandlers) {
                dh.onDraw(canvas);
            }
        } finally {
            while (pos >= 0) {
                --pos;
                drawHandlers.get(pos).onPostDraw(canvas);
            }
        }
    }

}
