package ru.spacearena.game;

import android.graphics.*;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Viewport;
import ru.spacearena.util.RandomUtils;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Sky extends EngineObject {

    private static final int PIXELS_PER_STAR = 150;

    private static final float MAX_SIZE = 6f;
    private static final float MIN_SIZE = 2f;

    private final Random random = new Random();
    private final long seed;

    private final Viewport viewport;

    private final Paint paint = new Paint();

    public Sky(Viewport viewport) {
        this.viewport = viewport;
        this.seed = random.nextLong();
    }

    static float firstVisiblePosition(float i, float grid) {
        return (float)Math.ceil(i/grid)*grid;
    }

    private void drawStarLayer(Canvas canvas, RectF viewRect, float pixelsPerStar, float scale) {

        final RectF scaledRect = new RectF(viewRect);

        final float twoStarDistance = pixelsPerStar * 2;

        // inflating rect
        scaledRect.inset(-(scaledRect.width()*scale + twoStarDistance), -(scaledRect.height()*scale + twoStarDistance));

        final float startX = firstVisiblePosition(scaledRect.left, pixelsPerStar);
        final float startY = firstVisiblePosition(scaledRect.top, pixelsPerStar);

        for (float y=startY; y<=scaledRect.bottom; y += pixelsPerStar) {
            for (float x=startX; x<=scaledRect.right; x += pixelsPerStar) {
                random.setSeed(seed ^ ((long)scale<<48) ^ ((long)x<<24) ^ ((long)y));
                final float randX = x + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float randY = y + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float halfSize = (MIN_SIZE + random.nextFloat() * (MAX_SIZE-MIN_SIZE))/2;

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                paint.setColor(color);

                final float translateX = (randX - scaledRect.centerX()) / scale + scaledRect.centerX();
                final float translateY = (randY - scaledRect.centerY()) / scale + scaledRect.centerY();

                canvas.drawCircle(translateX, translateY, halfSize, paint);
            }
        }
    }

    public void render(Canvas canvas) {

        paint.setColor(Color.WHITE);
        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawRect(-10f,-10f,10f,10f,paint);
        canvas.drawText("Center of world", 0, 50, paint);

        final RectF viewRectF = viewport.getViewRect();
        for (float scale=1; scale<=5; scale++) {
            drawStarLayer(canvas, viewRectF, 300*scale, scale);
        }
    }
}
