package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

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

    private final Point2F pt = new Point2F();

    @Override
    public void onDraw(DrawContext context) {
        final List<FlameParticle> particles = ship.getEngineParticles();
        final float fSize = (float) particles.size();
        float prevX = 0f, prevY = 0f;

        int i = 0;
        boolean hasPrev = false;

        final VertexBuffer vb = context.getSharedBuffer();
        vb.reset(LAYOUT_P2E1);

        for (FlameParticle p: particles) {


            if (hasPrev) {

                pt.set(p.x, p.y).sub(prevX, prevY).div(p.l).rperp();
                final float perpX = p.x - prevX, perpY = p.y - prevY;

//                vb.put()
//
//                final float prevSize = (float) i / fSize;
//                context.setColor(ColorU.rgb(prevSize, prevSize, 1f));
//                context.setLineWidth(prevSize * 0.5f);
//                context.drawLine(prevX, prevY, p.x, p.y);
            }
            prevX = p.x;
            prevY = p.y;
            hasPrev = p.active;
            ++i;
        }

    }

    public static final VertexBufferLayout LAYOUT_P2E1 = VertexBufferLayout.create().floats(2).floats(1).build();

    public static final ShaderProgram.Definition SHADER = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            final ShaderProgram p = new ShaderProgram();
            p.shader(EngineFlame.class, "flame.vert");
            p.shader(EngineFlame.class, "frame.frag");
            p.attribute("a_Position");
            p.attribute("a_Edge");
            p.uniform("u_MVPMatrix");
            return p;
        }
    };
}
