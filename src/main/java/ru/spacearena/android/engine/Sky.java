package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Sky implements EngineObject {


    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 1000;

    private static class Star {
        private Point position;
        private int color;
    }

    private static final float LOW_STAR_SPEED = 200f;
    private static final float HIGH_STAR_SPEED = 700f;
    private static final int STAR_COUNT = 100;

    private final Random random = new Random();
    private final Paint paint = new Paint();
    private final List<Star> lowStars = new ArrayList<Star>();
    private final List<Star> highStars = new ArrayList<Star>();

    public Sky() {
        initStars(lowStars);
        initStars(highStars);
    }

    public boolean process(float timeDelta) {
        processStars(lowStars, CANVAS_WIDTH, CANVAS_HEIGHT, LOW_STAR_SPEED * timeDelta);
        processStars(highStars, CANVAS_WIDTH, CANVAS_HEIGHT, HIGH_STAR_SPEED * timeDelta);
        return true;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        return false;
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
            star.position = Point.create(CANVAS_WIDTH * random.nextFloat(), CANVAS_HEIGHT * random.nextFloat());
            star.color = generateStarColor(random);
            stars.add(star);
        }
    }

    private void processStars(List<Star> stars, int width, int height, float speed) {
        for (final Star star : stars) {
            final float newY = star.position.getY() - speed;
            if (newY < 0) {
                star.position = Point.create(random.nextFloat() * width, height + random.nextFloat() * height);
                continue;
            }
            star.position = Point.create(star.position.getX(), newY);
        }
    }

    private void renderStars(Canvas canvas, List<Star> stars) {
        for (final Star star: stars) {
            paint.setColor(star.color);
            canvas.drawCircle(star.position.getX(), star.position.getY(), 3, paint);
        }
    }

    public void render(Canvas canvas) {
        renderStars(canvas, lowStars);
        renderStars(canvas, highStars);
    }
}
