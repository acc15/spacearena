package ru.spacearena.android.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import ru.spacearena.android.engine.EngineObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Sky extends EngineObject {

    private static class Star {
        private float x;
        private float y;
        private int color;
        public float size;
    }

    private static final float LOW_STAR_SPEED = 200f;
    private static final float HIGH_STAR_SPEED = 400f;
    private static final int STAR_COUNT = 100;

    private final Random random = new Random();
    private final Paint paint = new Paint();
    private final List<Star> lowStars = new ArrayList<Star>();
    private final List<Star> highStars = new ArrayList<Star>();

    @Override
    public void init() {
        initStars(lowStars);
        initStars(highStars);
    }

    public boolean process(float time) {
        processStars(lowStars, LOW_STAR_SPEED * time);
        processStars(highStars, HIGH_STAR_SPEED * time);
        return true;
    }

    private static int generateStarColor(Random random) {
        final int rnd = random.nextInt(512);
        if (rnd >= 256) {
            final int clr = rnd - 256;
            return Color.argb(0xff, clr, clr, 255);
        } else {
            return Color.argb(0xff, 0, 0, rnd);
        }
    }

    private void initStars(List<Star> stars) {
        for (int i=0; i<STAR_COUNT; i++) {
            final Star star = new Star();
            initStar(star);
            stars.add(star);
        }
    }

    private void initStar(Star star) {
        final Rect displayRect = getEngine().getDisplayRect();
        star.x = random.nextFloat() * displayRect.width() + displayRect.left;
        star.y = random.nextFloat() * displayRect.height() + displayRect.top;
        star.size = random.nextFloat() * 5;
        star.color = generateStarColor(random);
    }

    private void processStars(List<Star> stars, float speed) {
        for (final Star star : stars) {
            star.y = star.y - speed;
            if (star.y < 0) {
                initStar(star);
                star.y += getEngine().getDisplayRect().height();
            }
        }
    }

    private void renderStars(Canvas canvas, List<Star> stars) {
        for (final Star star: stars) {
            paint.setColor(star.color);
            canvas.drawCircle(star.x, star.y, star.size, paint);
        }
    }

    public void render(Canvas canvas) {
        renderStars(canvas, lowStars);
        renderStars(canvas, highStars);
    }
}
