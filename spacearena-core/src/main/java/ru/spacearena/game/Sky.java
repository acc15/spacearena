package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.Viewport;
import ru.spacearena.engine.geometry.shapes.Rect2FPR;
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

    private float starDistance = 20f;
    private float minStarSize = 0.05f;
    private float maxStarSize = 0.1f;

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

        final float sd2 = starDistance*2;

        bounds.set(viewport.getBounds());
        bounds.halfSize.add(sd2);
        bounds.halfSize.div(scale);

        final int x1 = (int)FloatMathUtils.ceil(bounds.getMinX()/starDistance),
                  x2 = (int)FloatMathUtils.floor(bounds.getMaxX()/starDistance),
                  y1 = (int)FloatMathUtils.ceil(bounds.getMinY()/starDistance),
                  y2 = (int)FloatMathUtils.floor(bounds.getMaxY()/starDistance);

        for (int i = y1; i<=y2; i++) {
            for (int j = x1; j<=x2; j++) {
                random.setSeed(((long)Float.floatToRawIntBits(scale)<<16) ^
                               ((long)Float.floatToRawIntBits(i)<<8) ^
                               ((long)Float.floatToRawIntBits(j)) ^
                               seed);

                final float dx = RandomUtils.randomBetween(random, -sd2, sd2),
                            dy = RandomUtils.randomBetween(random, -sd2, sd2),
                            halfSize = RandomUtils.randomBetween(random, minStarSize, maxStarSize);

                final float x = j * starDistance, y = i * starDistance;
                final float gx = (x-bounds.position.x)*scale+bounds.position.x,
                            gy = (y-bounds.position.y)*scale+bounds.position.y;
                final float sx = gx + dx*scale, sy = gy + dy * scale;

//                context.setLineWidth(0f);
//                context.fillColor(Color.argb(1f, scale, scale, scale));
//                DrawUtils.drawArrow(context, gx, gy, sx, sy,
//                        DrawUtils.HeadType.CIRCLE, 0.4f,
//                        DrawUtils.HeadType.ARROW, 1f);

                final int bright = random.nextInt(256);
                context.fillColor(Color.rgb(bright, bright, 0xff));
                context.fillRect(sx-halfSize, sy-halfSize, sx+halfSize, sy+halfSize);

            }
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        for (float scale=5; scale>0; scale--) {
            drawStarLayer(context, 1-scale/10);
        }
    }

}