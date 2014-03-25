package ru.spacearena.game;

import ru.spacearena.engine.integration.box2d.Box2dBody;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-25-03
 */
public abstract class GameBody extends Box2dBody {
    public abstract ObjectType getType();

    public static ObjectType getObjectType(Box2dBody body) {
        return ((GameBody)body).getType();
    }

}
