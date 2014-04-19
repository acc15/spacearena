package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.OpenGL;
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

    public static final Color INVISIBLE_RED = new Color(1,0,0,0);
    public static final Color YELLOW =        new Color(1,0.5f,0,1);

    @Override
    public void onDraw(DrawContext context) {
        final List<FlameParticle> particles = ship.getEngineParticles();
        final VertexBuffer vb = context.getSharedBuffer();

        FlameParticle prev = null;

        final int l = particles.size();
        for (int i=0; i<l; i++) {
            final FlameParticle p = particles.get(i);
            final float t = (float)(i+1)/l;

            if (prev == null) {
                vb.reset(LAYOUT_P2E1T1);
            } else {
                final Point2F pt = Point2F.temp(p.x, p.y).sub(prev.x, prev.y).mul(1 / prev.l).rperp();
                vb.put(prev.x + pt.x, prev.y + pt.y).put(-1).put(t).
                        put(prev.x - pt.x, prev.y - pt.y).put(1).put(t);
            }

            boolean last = i == l-1;
            if (last || !p.active) {
                if (prev != null) {

                    final Point2F pt = Point2F.temp(p.x, p.y).sub(prev.x, prev.y).mul(1/prev.l);
                    final float el = 1.4f;
                    vb.put(prev.x + pt.x * el + pt.y, prev.y + pt.y * el - pt.x).put(-1).put(0).
                       put(prev.x + pt.x * el - pt.y, prev.y + pt.y * el + pt.x).put(1).put(0);

                    context.use(SHADER).
                            attrs(vb).
                            uniform(context.getActiveMatrix()).
                            uniform(YELLOW).
                            uniform(INVISIBLE_RED).
                            draw(OpenGL.TRIANGLE_STRIP);
                    prev = null;
                }
            } else {
                prev = p;
            }
        }



    }

    public static final VertexBufferLayout LAYOUT_P2E1T1 = VertexBufferLayout.create().floats(2).floats(1).floats(1).build();

    public static final ShaderProgram.Definition SHADER = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            final ShaderProgram p = new ShaderProgram();
            p.shader(EngineFlame.class, "flame.vert");
            p.shader(EngineFlame.class, "flame.frag");
            p.attribute("a_Position");
            p.attribute("a_Edge");
            p.attribute("a_Time");
            p.uniform("u_MVPMatrix");
            p.uniform("u_CenterColor");
            p.uniform("u_EdgeColor");
            return p;
        }
    };
}
