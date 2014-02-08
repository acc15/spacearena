package ru.spacearena.game;

import android.graphics.*;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Viewport;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Sky extends EngineObject {

    private static final int PIXELS_PER_STAR = 300;

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



    private int randomInt(int v, int offset) {
        return v - offset + random.nextInt((offset+1)*2);
    }

    public void render(Canvas canvas) {

        final int gridX = PIXELS_PER_STAR, gridY = PIXELS_PER_STAR;

        final RectF viewRectF = viewport.getViewRect();
        final Rect viewRect = new Rect((int)viewRectF.left, (int)viewRectF.top, (int)viewRectF.right, (int)viewRectF.bottom);
        viewRect.inset(-gridX*2, -gridY*2);

        final int startX = firstVisiblePosition(viewRect.left, gridX);
        final int startY = firstVisiblePosition(viewRect.top, gridY);

        for (int y=startY; y<=viewRect.bottom; y+=gridY) {
            for (int x=startX; x<=viewRect.right; x+=gridX) {
                random.setSeed(seed ^ ((long)x << 32 ^ y));
                final int realX = randomInt(x, gridX*2);
                final int realY = randomInt(y, gridY*2);
                final float halfSize = (MIN_SIZE + random.nextFloat() * (MAX_SIZE-MIN_SIZE))/2;

                final int bright = random.nextInt(256);
                final int color = Color.rgb(bright, bright, 0xff);

                paint.setColor(color);
                canvas.drawRect(realX-halfSize, realY-halfSize, realX+halfSize, realY+halfSize, paint);
            }
        }

    }
}
