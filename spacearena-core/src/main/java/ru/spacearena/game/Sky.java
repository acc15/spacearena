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
        final float starDistanceScale = starDistance * scale;
        final float twoStarDistance = starDistance * 2;

        bounds.set(viewport.getBounds());
        bounds.scale(scale, scale);
        bounds.inflate(starDistanceScale*2, starDistanceScale*2);

        final float startX = FloatMathUtils.firstVisiblePosition(bounds.getMinX(), starDistanceScale),
                    startY = FloatMathUtils.firstVisiblePosition(bounds.getMinY(), starDistanceScale),
                    endX = bounds.getMaxX(), endY = bounds.getMaxY();

        for (float y=startY; y<= endY; y += starDistanceScale) {
            for (float x=startX; x<= endX; x += starDistanceScale) {
                random.setSeed(((long)Float.floatToRawIntBits(scale)<<32) ^
                               ((long)Float.floatToRawIntBits(x)<<24) ^
                               ((long)Float.floatToRawIntBits(y)<<16) ^ seed);

                context.setLineWidth(0f);

                final float halfSize = RandomUtils.randomBetween(random, minStarSize, maxStarSize)/2;
                final float dx = RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance),
                            dy = RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);

                final float tx = (x + dx - bounds.x) / scale + bounds.x,
                            ty = (y + dy - bounds.y) / scale + bounds.y;

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                context.fillColor(color);
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