package ru.spacearena.game;

import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyCode;
import ru.spacearena.engine.input.trackers.InputTracker;
import ru.spacearena.engine.util.FloatMathUtils;

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

        final AABB mapBounds = new AABB(-10000f, -10000f, 10000f, 10000f);

        final Ship ship = new Ship();
        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));

        root.add(new InputTracker() {

            boolean canShot = true;

            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT, 1f);
                final float yVelocity = getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN, 1f);
                if (!FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    final float length = FloatMathUtils.length(xVelocity, yVelocity);
                    ship.getPhysics().setTargetVelocity(xVelocity/length, yVelocity/length);

                    final float angle = FloatMathUtils.angle(xVelocity, yVelocity);
                    ship.getPhysics().setTargetAngle(angle);
                } else {
                    ship.getPhysics().setTargetVelocity(0, 0);
                }

                if (!isKeyboardKeyPressed(KeyCode.VK_SPACE)) {
                    canShot = true;
                    return true;
                }
                if (canShot) {
                    final float[] gunPositions = ship.getGunPositions();
                    for (int i=0; i<gunPositions.length; i+=2) {
                        viewport.add(new Bullet(mapBounds, gunPositions[i], gunPositions[i+1], ship.getAngle()));
                    }
                    canShot = false;
                }
                return true;
            }
        });

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

        root.add(viewport);

        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-5, -5, 5, 5));
        viewport.add(ship);

        root.add(new PositionHandler(ship, viewport));
        root.add(new BoundChecker(mapBounds, ship));
        root.add(new BoundChecker(mapBounds, viewport));
        root.add(new EngineObject() {

            @Override
            public boolean onUpdate(float seconds) {
                viewportText.setText(String.format("Viewport: (%.2f,%.2f)(%.2f,%.2f)",
                        viewport.getMinX(), viewport.getMinY(), viewport.getMaxX(), viewport.getMaxY()));
                positionText.setText(String.format("Position: %.2f, %.2f",
                        ship.getX(), ship.getY()));
                return true;
            }
        });

        root.add(multilineText);
        return root;
    }

}
