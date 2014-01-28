package ru.spacearena.android.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ru.spacearena.android.engine.Dimension;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.Frame;

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
    }

    private static final float LOW_STAR_SPEED = 200f;
    private static final float HIGH_STAR_SPEED = 400f;
    private static final int STAR_COUNT = 100;

    private final Random random = new Random();
    private final Paint paint = new Paint();
    private final List<Star> lowStars = new ArrayList<Star>();
    private final List<Star> highStars = new ArrayList<Star>();

    @Override
    public void init(Dimension dimension) {
        initStars(lowStars, dimension);
        initStars(highStars, dimension);
    }

    public boolean process(Frame frame) {
        processStars(lowStars, frame, LOW_STAR_SPEED * frame.getTimeDelta());
        processStars(highStars, frame, HIGH_STAR_SPEED * frame.getTimeDelta());
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

    private void initStars(List<Star> stars, Dimension dimension) {
        for (int i=0; i<STAR_COUNT; i++) {
            final Star star = new Star();
            initStar(star, dimension);
            stars.add(star);
        }
    }

    private void initStar(Star star, Dimension dimension) {
        star.x = random.nextFloat() * dimension.getWidth();
        star.y = random.nextFloat() * dimension.getHeight();
        star.color = generateStarColor(random);
    }

    private void processStars(List<Star> stars, Dimension dimension, float speed) {
        for (final Star star : stars) {
            star.y = star.y - speed;
            if (star.y < 0) {
                initStar(star, dimension);
                star.y += dimension.getHeight();
            }
        }
    }

    private void renderStars(Canvas canvas, List<Star> stars) {
        for (final Star star: stars) {
            paint.setColor(star.color);
            canvas.drawCircle(star.x, star.y, 3, paint);
        }
    }

    public void render(Canvas canvas) {
        renderStars(canvas, lowStars);
        renderStars(canvas, highStars);
    }
}
