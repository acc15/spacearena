package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-27-03
 */
public class Explosion extends EngineObject {

    private static final int PARTICLE_COUNT = 20;
    private static final int EXPLOSION_COUNT = 100;

    private final float[] particles = new float[PARTICLE_COUNT << 1];
    private final int[] colors = new int[PARTICLE_COUNT];

    private final float[] explosion = new float[EXPLOSION_COUNT << 1];
    private final int[] explosionColors = new int[EXPLOSION_COUNT];

    private final float x, y;
    private float duration;

//    private Image image;

    public Explosion(float x, float y, float angle) {
        this.x = x;
        this.y = y;

        /* TODO
        for (int i=0;i<PARTICLE_COUNT; i++) {
            final float l = TempUtils.RAND.nextFloatBetween(0, 4f);
            final float a = i < PARTICLE_COUNT/4 ? TempUtils.RAND.nextFloatBetween(0, FloatMathUtils.TWO_PI)
                                                 : TempUtils.RAND.nextFloatBetween(angle-0.3f, angle+0.3f);
            particles[i*2] = FloatMathUtils.cos(a) * l;
            particles[i*2+1] = FloatMathUtils.sin(a) * l;

            final float c = TempUtils.RAND.nextFloatBetween(0.6f, 0.8f);
            colors[i] = ColorU.rgb(c, c, c);
        }*/
//
//        for (int i=0;i<EXPLOSION_COUNT; i++) {
//            final float l = TempUtils.RAND.nextFloatBetween(3f, 6f);
//            final float a = (float)i / EXPLOSION_COUNT * FloatMathUtils.TWO_PI;
//            explosion[i*2] = FloatMathUtils.cos(a) * l;
//            explosion[i*2+1] = FloatMathUtils.sin(a) * l;
//            explosionColors[i] = ColorU.rgb(1f, TempUtils.RAND.nextFloatBetween(0.2f, 1f), 0f);
//        }


    }
//
//    @Override
//    public void onAttach(Engine engine) {
//        this.image = engine.createImage();
//    }

    @Override
    public boolean onUpdate(float seconds) {
        duration += seconds * 2;//(duration + seconds);
        return duration < 1f;
    }

    @Override
    public void onDraw(DrawContext context) {
        /*
        final float lw = context.getLineWidth();
        try {
            context.setLineWidth(0f);

            final float v = duration * 40f;
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                final int color = colors[i];
                final float dx = particles[i * 2];
                final float dy = particles[i * 2 + 1];
                final float px = x + dx * v;
                final float py = y + dy * v;
                context.setColor(color);
                context.drawLine(px, py, px + dx, py + dy);
            }
            context.setAlpha(1.0f);
        } finally {
            context.setLineWidth(lw);
        }
        */
    }
}
