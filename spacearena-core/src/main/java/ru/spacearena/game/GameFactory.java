package ru.spacearena.game;

import org.jbox2d.common.Vec2;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.events.KeyCode;
import ru.spacearena.engine.events.trackers.InputTracker;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.integration.box2d.Box2dWorld;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineEntity createRoot(final Engine engine) {

        //engine.getDebug().setDrawAll(true);

        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer(engine);

        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText.Line positionText = new MultilineText.Line();
        final MultilineText.Line viewportText = new MultilineText.Line();
        final MultilineText.Line collisionText = new MultilineText.Line();

        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);
        multilineText.add(viewportText);
        multilineText.add(collisionText);

        root.add(new Background());

        final FPSCounter fpsCounter = new FPSCounter();
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

        final Rect2FPP levelBounds = new Rect2FPP(-100f, -100f, 100f, 100f);

        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(75f));
        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f));

        root.add(viewport);

        root.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                final BoundingBox2F box = viewport.getBounds();
                viewportText.setText(String.format("L: %.2f; T: %.2f; R: %.2f; B: %.2f; X: %.2f; Y: %.2f; SX: %.2f; SY: %.2f",
                        box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(),
                        viewport.getPositionX(), viewport.getPositionY(),
                        viewport.getScaleX(), viewport.getScaleY()));
                return true;
            }
        });

        final Box2dWorld box2dWorld = new Box2dWorld();
        box2dWorld.setTimeScale(2f);
        box2dWorld.add(new LevelBounds(levelBounds));

        final Ship ship1 = new Ship();
        ship1.setInitialPosition(0, -5);
        ship1.setInitialAngle(FloatMathUtils.HALF_PI);
        box2dWorld.add(ship1);

        for (int i=0; i<20; i++) {
            final Ship ship2 = new Ship();
            ship2.setInitialPosition((i-10)*5, 5);
            ship2.setInitialAngle(-FloatMathUtils.HALF_PI);
            box2dWorld.add(ship2);
        }

        viewport.add(box2dWorld);
        root.add(new InputTracker() {

            private Vec2 moveTo = new Vec2();
            private boolean canShoot = true;

            @Override
            public boolean onUpdate(float seconds) {

                moveTo.set(getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT),
                           getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN));
                ship1.flyTo(moveTo.x, moveTo.y, seconds);

                if (isKeyboardKeyPressed(KeyCode.VK_SPACE)) {
                    if (canShoot) {
                        final org.jbox2d.common.Transform tf = ship1.getTransform();
                        for (Vec2 gun: ship1.getGuns()) {
                            ship1.getBody().getWorldPointToOut(gun, moveTo);
                            box2dWorld.add(new Bullet(ship1, moveTo.x, moveTo.y, tf.q.c, tf.q.s, ship1.getAngle()));
                        }
                        canShoot = false;
                    }
                } else {
                    canShoot = true;
                }

                return true;
            }
        });
        root.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                viewport.setPosition(ship1.getPositionX(), ship1.getPositionY());
                return true;
            }
        });
        root.add(new BoundChecker(levelBounds, viewport));

        root.add(multilineText);
        return root;
    }

}
