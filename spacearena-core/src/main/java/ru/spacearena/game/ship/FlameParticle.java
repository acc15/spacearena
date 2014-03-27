package ru.spacearena.game.ship;

import ru.spacearena.engine.geometry.primitives.Point2F;

/**
* @author Vyacheslav Mayorov
* @since 2014-26-03
*/
public class FlameParticle {
    public float x, y;
    public long timestamp;
    public boolean active;

    public FlameParticle(long timestamp, Point2F position, boolean active) {
        this.timestamp = timestamp;
        this.x = position.x;
        this.y = position.y;
        this.active = active;
    }
}
