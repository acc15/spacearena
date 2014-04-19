package ru.spacearena.engine.integration.box2d;

import org.jbox2d.common.Vec2;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class Box2dUtils {

    public static Vec2 VEC2_1 = new Vec2();

    public static Vec2 vec2(float x, float y) {
        VEC2_1.set(x, y);
        return VEC2_1;
    }


}
