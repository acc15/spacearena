package ru.spacearena.game;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.RenderContext;
import ru.spacearena.engine.primitives.RectI;

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
        private Color color;
        public float size;
    }

    private static final float LOW_STAR_SPEED = 200f;
    private static final float HIGH_STAR_SPEED = 400f;
    private static final int STAR_COUNT = 100;

    private final Random random = new Random();
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

    private static Color generateStarColor(Random random) {
        final int rnd = random.nextInt(512);
        if (rnd >= 256) {
            final int clr = rnd - 256;
            return Color.fromRGB(clr, clr, 255);
        } else {
            return Color.fromRGB(0, 0, rnd);
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
        final RectI displayRect = getEngine().getDisplayRect();
        star.x = random.nextFloat() * displayRect.getWidth() + displayRect.left;
        star.y = random.nextFloat() * displayRect.getHeight() + displayRect.top;
        star.size = random.nextFloat() * 5;
        star.color = generateStarColor(random);
    }

    private void processStars(List<Star> stars, float speed) {
        for (final Star star : stars) {
            star.y = star.y - speed;
            if (star.y < 0) {
                initStar(star);
                star.y += getEngine().getDisplayRect().getHeight();
            }
        }
    }

    private void renderStars(RenderContext canvas, List<Star> stars) {
        for (final Star star: stars) {
            canvas.setColor(star.color);
            canvas.drawCircle(star.x, star.y, star.size);
        }
    }

    public void render(RenderContext context) {
        renderStars(context, lowStars);
        renderStars(context, highStars);
    }
}
