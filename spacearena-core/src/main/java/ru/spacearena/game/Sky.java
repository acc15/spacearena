package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.Viewport;
import ru.spacearena.engine.geometry.shapes.Rect2FPR;
import ru.spacearena.engine.graphics.ColorU;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.random.QRand;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Sky extends EngineObject {

    private float starDistance = 25f;
    private float minStarSize = 0.05f;
    private float maxStarSize = 0.1f;

    private final QRand random = new QRand();
    private final int seed = random.nextInt();
    private final Rect2FPR bounds = new Rect2FPR();
    private final Viewport viewport;

    public Sky(Viewport viewport) {
        this.viewport = viewport;
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
                random.setSeed(seed ^
                        Integer.rotateLeft(Float.floatToRawIntBits(scale), 16) ^
                        Integer.rotateLeft(i, 8) ^ j);

                final float dx = random.nextFloatBetween(-sd2, sd2),
                            dy = random.nextFloatBetween(-sd2, sd2),
                            halfSize = random.nextFloatBetween(minStarSize, maxStarSize);

                final float x = j * starDistance, y = i * starDistance;
                final float gx = (x-bounds.position.x)*scale+bounds.position.x,
                            gy = (y-bounds.position.y)*scale+bounds.position.y;
                final float sx = gx + dx*scale, sy = gy + dy * scale;

//                context.setLineWidth(0f);
//                context.fillColor(ColorU.argb(1f, scale, scale, scale));
//                DrawUtils.drawArrow(context, gx, gy, sx, sy,
//                        DrawUtils.HeadType.CIRCLE, 0.4f,
//                        DrawUtils.HeadType.ARROW, 1f);

                final int bright = random.nextInt(256);
                context.setColor(ColorU.rgb(bright, bright, 0xff));
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