package ru.spacearena.game;

import ru.spacearena.android.engine.AABB;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineFactory;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.common.*;
import ru.spacearena.android.engine.input.InputType;
import ru.spacearena.android.engine.input.KeyCode;
import ru.spacearena.android.engine.input.trackers.KeyTracker;
import ru.spacearena.android.engine.util.FloatMathUtils;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineObject createRoot(final Engine engine) {

        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer();
        root.add(new Background());

        final FPSCounter fpsCounter = new FPSCounter();
        final MultilineText.Line fpsText = new MultilineText.Line();
        root.add(fpsCounter);

        final Timer timer = new Timer(0.5f, true);
        root.add(timer);
        timer.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        });

        final MultilineText.Line positionText = new MultilineText.Line();
        final MultilineText.Line viewportText = new MultilineText.Line();
        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);
        multilineText.add(viewportText);

        final Ship ship = new Ship();
        ship.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                positionText.setText(String.format("Position: %.2f, %.2f",
                        ship.getTransform().getX(), ship.getTransform().getY()));
                return true;
            }
        });

        root.add(new KeyTracker() {
            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT, 1f);
                final float yVelocity = getDirection(KeyCode.VK_UP, KeyCode.VK_DOWN, 1f);
                if (FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    ship.getPhysics().setVelocity(0, 0);
                    return true;
                }
                final float length = 500f/FloatMathUtils.length(xVelocity, yVelocity);
                ship.getPhysics().setVelocity(xVelocity * length, yVelocity * length);
                ship.getTransform().setRotation(FloatMathUtils.angle(xVelocity, yVelocity));
                return true;
            }
        });

        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));
        root.add(viewport);

        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-5, -5, 5, 5));
        viewport.add(ship);
/*
        final Timer bulletTimer = new Timer(1/100f);
        viewport.add(bulletTimer);

        final GenericContainer bullets = new GenericContainer();
        viewport.add(bullets);

        final MultilineText.Line bulletCount = new MultilineText.Line();
        multilineText.add(bulletCount);
        bulletTimer.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                bullets.add(new Bullet(0,0,FloatMathUtils.random() * FloatMathUtils.CIRCLE_ANGLE));
                bulletCount.setText("Bullts: " + bullets.getChildrenCount());
                return true;
            }
        });
*/
        viewport.setPosition(0f, 0f);

        root.add(new PositionHandler(ship.getTransform(), viewport));
        root.add(new EngineObject() {

            final AABB viewRect = new AABB();

            @Override
            public boolean onUpdate(float seconds) {
                viewport.calculateBounds(viewRect);
                viewportText.setText(String.format("Viewport: (%.2f,%.2f)(%.2f,%.2f)",
                        viewRect.minX, viewRect.minY, viewRect.maxX, viewRect.maxY));
                return true;
            }
        });

        root.add(multilineText);
        return root;
    }

}
