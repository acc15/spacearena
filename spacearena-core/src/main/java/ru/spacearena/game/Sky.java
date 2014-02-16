package ru.spacearena.game;

import ru.spacearena.engine.AABB;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.Viewport;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.RandomUtils;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Sky extends EngineObject {

    private static final int PIXELS_PER_STAR = 150;

    private static final float MAX_SIZE = 6f;
    private static final float MIN_SIZE = 2f;

    private final Random random;
    private final long seed;
    private final Viewport viewport;
    private final AABB bounds = new AABB();

    public Sky(Viewport viewport, Random random) {
        this.viewport = viewport;
        this.random = random;
        this.seed = random.nextLong();
    }

    private void drawStarLayer(DrawContext context, float scale) {

        viewport.calculateBounds(bounds);

        final float pixelsPerStar = PIXELS_PER_STAR * scale;
        final float twoStarDistance = pixelsPerStar * 2;

        bounds.scale(scale, scale);
        bounds.inflate(twoStarDistance, twoStarDistance);

        final float startX = FloatMathUtils.firstVisiblePosition(bounds.minX, pixelsPerStar);
        final float startY = FloatMathUtils.firstVisiblePosition(bounds.minY, pixelsPerStar);

        for (float y=startY; y<=bounds.maxY; y += pixelsPerStar) {
            for (float x=startX; x<=bounds.maxX; x += pixelsPerStar) {
                random.setSeed(seed ^ ((long)scale<<48) ^ ((long)x<<24) ^ ((long)y));

                final float randX = x + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float randY = y + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float halfSize = (MIN_SIZE + random.nextFloat() * (MAX_SIZE-MIN_SIZE))/2;

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                context.setColor(color);

                final float translateX = (randX - bounds.getCenterX()) / scale + bounds.getCenterX();
                final float translateY = (randY - bounds.getCenterY()) / scale + bounds.getCenterY();

                context.fillCircle(translateX, translateY, halfSize);
            }
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        for (float scale=5; scale>=1; scale--) {
            drawStarLayer(context, scale);
        }
    }

}