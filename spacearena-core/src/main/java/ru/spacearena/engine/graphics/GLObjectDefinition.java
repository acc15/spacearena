package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-04
 */
public interface GLObjectDefinition<T> {
    T create(GLDrawContext context);
    void reference(GLDrawContext context, T object);
    void delete(GLDrawContext context, T object);
}

