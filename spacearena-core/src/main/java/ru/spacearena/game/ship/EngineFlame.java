package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;

import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-26-03
*/
public class EngineFlame extends EngineObject {

    private Ship ship;

    public EngineFlame(Ship ship) {
        this.ship = ship;
    }

    @Override
    public void onDraw(DrawContext context) {
        final List<FlameParticle> particles = ship.getEngineParticles();
        final float fSize = (float) particles.size();
        float prevX = 0f, prevY = 0f;

        int i = 0;
        boolean hasPrev = false;

        /* TODO
        final float lw = context.getLineWidth();
        try {
            for (FlameParticle p: particles) {
                if (hasPrev) {
                    final float prevSize = (float) i / fSize;
                    context.setColor(ColorU.rgb(prevSize, prevSize, 1f));
                    context.setLineWidth(prevSize * 0.5f);
                    context.drawLine(prevX, prevY, p.x, p.y);
                }
                prevX = p.x;
                prevY = p.y;
                hasPrev = p.active;
                ++i;
            }
        } finally {
            context.setLineWidth(lw);
        }
        */
    }
}
