package ru.spacearena.game;

import ru.spacearena.engine.AABB;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyCode;
import ru.spacearena.engine.input.MouseEvent;
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
        final Ship ship = new Ship();
        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));

        root.add(new InputTracker() {

            boolean canShot = true;

            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT, 1f);
                final float yVelocity = getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN, 1f);
                if (!FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    final float length = 500f/FloatMathUtils.length(xVelocity, yVelocity);
                    ship.getPhysics().setVelocity(xVelocity * length, yVelocity * length);
                } else {
                    ship.getPhysics().setVelocity(0, 0);
                }

                final Transform shipTx = ship.getTransform();
                final float shipX = shipTx.getX(), shipY = shipTx.getY();
                if (isMouseKeyPressed(MouseEvent.LEFT_BUTTON)) {
                    final float[] pt = new float[] {getMouseX(), getMouseY()};
                    viewport.mapPoints(pt);

                    final float dx = pt[0] - shipX, dy = pt[1] - shipY;
                    if (!FloatMathUtils.isZero(dx, dy)) {
                        final float angle = FloatMathUtils.angle(dx, dy);
                        ship.getTransform().setRotation(angle);
                        if (canShot) {
                            final float[] gunPositions = ship.getGunPositions();
                            for (int i=0; i<gunPositions.length; i+=2) {
                                viewport.add(new Bullet(gunPositions[i], gunPositions[i+1], angle));
                            }
                            canShot = false;
                        }
                    }

                } else {
                    canShot = true;
                    if (!FloatMathUtils.isZero(xVelocity, yVelocity)) {
                        ship.getTransform().setRotation(FloatMathUtils.angle(xVelocity, yVelocity));
                    }
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


        ship.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                positionText.setText(String.format("Position: %.2f, %.2f",
                        ship.getTransform().getX(), ship.getTransform().getY()));
                return true;
            }
        });
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
