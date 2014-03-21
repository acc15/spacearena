package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.Viewport;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.util.RandomUtils;
import ru.spacearena.engine.util.FloatMathUtils
;
import ru.spacearena.engine.geometry.shapes.Rect2FPR;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Sky extends EngineObject {

    private float starDistance = 15f;
    private float minStarSize = 0.1f;
    private float maxStarSize = 0.2f;

    private final Random random;
    private final long seed;
    private final Viewport viewport;
    private final Rect2FPR bounds = new Rect2FPR();

    public Sky(Viewport viewport, Random random) {
        this.viewport = viewport;
        this.random = random;
        this.seed = random.nextLong();
    }

    public float getStarDistance() {
        return starDistance;
    }

    public void setStarDistance(float starDistance) {
        this.starDistance = starDistance;
    }

    public float getMinStarSize() {
        return minStarSize;
    }

    public void setMinStarSize(float minStarSize) {
        this.minStarSize = minStarSize;
    }

    public float getMaxStarSize() {
        return maxStarSize;
    }

    public void setMaxStarSize(float maxStarSize) {
        this.maxStarSize = maxStarSize;
    }

    private void drawStarLayer(DrawContext context, float scale) {

        bounds.set(viewport.getBounds());

        final float starDistanceScale = starDistance * scale;
        final float twoStarDistance = starDistanceScale * 2;

        bounds.scale(scale, scale);
        bounds.inflate(twoStarDistance, twoStarDistance);

        final float startX = FloatMathUtils.firstVisiblePosition(bounds.getMinX(), starDistanceScale);
        final float startY = FloatMathUtils.firstVisiblePosition(bounds.getMinY(), starDistanceScale);

        for (float y=startY; y<=bounds.getMaxY(); y += starDistanceScale) {
            for (float x=startX; x<=bounds.getMaxX(); x += starDistanceScale) {
                random.setSeed(seed ^ ((long)scale<<48) ^ ((long)x<<24) ^ ((long)y));
                final float halfSize = RandomUtils.randomBetween(random, minStarSize, maxStarSize)/2;
                final float randX = x + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float randY = y + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                context.fillColor(color);

                final float tx = (randX - bounds.x) / scale + bounds.x,
                            ty = (randY - bounds.y) / scale + bounds.y;
                context.fillRect(tx-halfSize, ty-halfSize, tx+halfSize, ty+halfSize);
            }
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        for (float scale=6; scale>1; scale--) {
            drawStarLayer(context, scale);
        }
    }

}