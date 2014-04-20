package ru.spacearena.game.ship;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.LinkedList;

/**
* @author Vyacheslav Mayorov
* @since 2014-26-03
*/
public class EngineFlame extends EngineObject {

    public static final Color CENTER_COLOR = new Color(1,0.5f,0,1);
    public static final Color EDGE_COLOR = new Color(1,0,0,0);

    private static final float STEAM_TIME = 0.5f;


    private final LinkedList<FlameParticle> particles = new LinkedList<FlameParticle>();
    private final Ship ship;

    public EngineFlame(final Ship ship) {
        this.ship = ship;
        this.ship.add(new EngineObject() {
            private Timer timer;

            @Override
            public void onAttach(Engine engine) {
                timer = engine.getTimer();
            }

            @Override
            public void onUpdate(float seconds) {
                addFlameParticle(timer, ship.isEngineActive());
            }
        });
    }

    private void addFlameParticle(Timer timer, boolean active) {
        final long t = timer.getTimestamp();

        FlameParticle particle;
        while ((particle = particles.peek()) != null && timer.toSeconds(t - particle.t) > STEAM_TIME) {
            particles.remove();
        }

        final FlameParticle last = particles.peekLast();
        final Point2F pt = ship.getEnginePosition(Point2F.PT);
        if (last == null) {
            if (active) {
                particles.add(new FlameParticle(pt, t, true));
            }
            return;
        }

        if (pt.near(last.x, last.y)) {
            if (last.active) {
                last.t = t;
            }
            last.active = active;
            return;
        }
        if (active || last.active) {
            final FlameParticle p = new FlameParticle(pt, t, active);
            last.l = FloatMathUtils.length(p.x - last.x, p.y - last.y);
            particles.add(new FlameParticle(pt, t, active));
        }
    }

    @Override
    public void onDraw(DrawContext2f context) {
        final VertexBuffer vb = context.getSharedBuffer();

        final int l = particles.size();
        for (int i=0; i<l; i++) {
            final float t = (float)(i+1)/l;

            final FlameParticle c = particles.get(i);

            FlameParticle p = i > 0 ? particles.get(i-1) : null;
            if (p != null && !p.active) {
                p = null;
            }

            if (p == null) {
                vb.reset(LAYOUT_P2E1T1);
                continue;
            }

            final float el = 1.4f;
            final Point2F pt = Point2F.temp(c.x, c.y).sub(p.x, p.y).div(p.l);
            if (i-2 < 0 || !particles.get(i-2).active) {
                vb.put(p.x - pt.x * el + pt.y * t, p.y - pt.y * el - pt.x * t).put(-1).put(0);
                vb.put(p.x - pt.x * el - pt.y * t, p.y - pt.y * el + pt.x * t).put(1).put(0);
            }

            vb.put(p.x + pt.y * t, p.y - pt.x * t).put(-1).put(t);
            vb.put(p.x - pt.y * t, p.y + pt.x * t).put(1).put(t);

            if (i == l-1 || !c.active) {
                vb.put(p.x + pt.x * el + pt.y * t, p.y + pt.y * el - pt.x * t).put(-1).put(0);
                vb.put(p.x + pt.x * el - pt.y * t, p.y + pt.y * el + pt.x * t).put(1).put(0);
                context.use(SHADER).
                        attrs(vb).
                        uniform(context.getActiveMatrix()).
                        uniform(CENTER_COLOR).
                        uniform(EDGE_COLOR).
                        draw(OpenGL.TRIANGLE_STRIP);
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
