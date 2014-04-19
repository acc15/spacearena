package ru.spacearena.game.ship;

import ru.spacearena.engine.geometry.primitives.Point2F;

/**
* @author Vyacheslav Mayorov
* @since 2014-26-03
*/
public class FlameParticle {
    public float x, y;
    public long t;
    public boolean active;
    public float l = 0;

    public FlameParticle(Point2F p, long t, boolean active) {
        this.x = p.x;
        this.y = p.y;
        this.t = t;
        this.active = active;
    }

}
