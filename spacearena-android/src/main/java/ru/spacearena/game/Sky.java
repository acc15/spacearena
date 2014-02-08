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

    private int firstVisiblePosition(int i, int grid) {
        final int v = i > 0 ? i + grid-1 : i;
        return v - v % grid;
    }

    public void render(Canvas canvas) {

        final int twoStarDistance = PIXELS_PER_STAR * 2;

        final RectF viewRectF = viewport.getViewRect();
        final Rect viewRect = new Rect((int)viewRectF.left, (int)viewRectF.top, (int)viewRectF.right, (int)viewRectF.bottom);
        viewRect.inset(-twoStarDistance, -twoStarDistance);

        final int startX = firstVisiblePosition(viewRect.left, PIXELS_PER_STAR);
        final int startY = firstVisiblePosition(viewRect.top, PIXELS_PER_STAR);

        for (int y=startY; y<=viewRect.bottom; y+=PIXELS_PER_STAR) {
            for (int x=startX; x<=viewRect.right; x+=PIXELS_PER_STAR) {
                random.setSeed(seed ^ ((long)x << 32 ^ y));
                final int realX = x + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final int realY = y + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float halfSize = (MIN_SIZE + random.nextFloat() * (MAX_SIZE-MIN_SIZE))/2;

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                paint.setColor(color);
                canvas.drawRect(realX-halfSize, realY-halfSize, realX+halfSize, realY+halfSize, paint);
            }
        }

    }
}
