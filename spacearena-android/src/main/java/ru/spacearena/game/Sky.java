package ru.spacearena.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Sky extends EngineObject {

    private static final float PIXELS_PER_STAR = 10000;

    private static class Star {
        private float x;
        private float y;
        private int color;
        private float size;
        private float depth;
    }

    private static final float FAR_STAR_DEPTH = 5f;
    private static final float CLOSE_STAR_DEPTH = 1f;

    private final Random random = new Random();
    private final List<Star> stars = new ArrayList<Star>();

    private RectF lastViewRect;
    private final Viewport viewport;

    private final Paint paint = new Paint();

    public Sky(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public void init() {
        lastViewRect = new RectF(viewport.getViewRect());
        final int starCount = (int)(lastViewRect.width() * lastViewRect.height() / PIXELS_PER_STAR);
        for (int i=0; i<starCount; i++) {
            final Star star = new Star();
            initStar(star, lastViewRect);
            stars.add(star);
        }
    }

    @Override
    public void postProcess() {

        final RectF viewRect = viewport.getViewRect();

        final float xOffset = viewRect.centerX() - lastViewRect.centerX();
        final float yOffset = viewRect.centerY() - lastViewRect.centerY();
        if (xOffset == 0 && yOffset == 0) {
            return;
        }
        for (final Star star: stars) {
            star.x += xOffset - xOffset / star.depth;
            star.y += yOffset - yOffset / star.depth;
        }
        lastViewRect = new RectF(viewRect);
    }

    private static int generateStarColor(Random random) {
        final int rnd = random.nextInt(512);
        if (rnd >= 256) {
            final int clr = rnd - 256;
            return Color.rgb(clr, clr, 255);
        } else {
            return Color.rgb(0, 0, rnd);
        }
    }

    private void initStar(Star star, RectF rect) {
        star.x = rect.left + random.nextFloat() * rect.width();
        star.y = rect.top + random.nextFloat() * rect.height();
        star.depth = CLOSE_STAR_DEPTH + random.nextFloat() * (FAR_STAR_DEPTH-CLOSE_STAR_DEPTH);
        star.color = generateStarColor(random);
        star.size = 1 + random.nextFloat() * (FAR_STAR_DEPTH / star.depth);
    }

    private void processStars(List<Star> stars, float speed) {
        /*for (final Star star : stars) {
            star.y = star.y - speed;
            if (star.y < 0) {
                initStar(star);
                star.y += getEngine().getDisplaySize().height();
            }
        }*/
    }

    public void render(Canvas canvas) {
        for (final Star star: stars) {
            paint.setColor(star.color);
            canvas.drawCircle(star.x, star.y, star.size, paint);
        }
    }
}
