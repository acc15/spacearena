package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.events.KeyCode;
import ru.spacearena.engine.events.trackers.InputTracker;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.integration.box2d.Box2dWorld;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.engine.util.FloatMathUtils;

import java.awt.event.MouseEvent;
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

        final Rect2FPP levelBounds = new Rect2FPP(-1000f, -1000f, 1000f, 1000f);

        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(75f));
        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f));

        root.add(viewport);

        final Box2dWorld box2dWorld = new Box2dWorld();
        //box2dWorld.setTimeScale(2f);
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

            private boolean canShoot = true;

            private Point2F getDirection(Point2F pt) {
                pt.set(getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT),
                       getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN));
                if (!pt.isZero()) {
                    return pt;
                }
                if (isMouseKeyPressed(MouseEvent.BUTTON3)) {
                    pt.set(getMouseX(), getMouseY());
                } else if (isPointerActive(0)) {
                    pt.set(getPointerX(0), getPointerY(0));
                } else {
                    return pt;
                }
                viewport.getWorldSpace().mapPoint(pt);
                pt.sub(ship1.getPositionX(), ship1.getPositionY());
                return pt;
            }

            @Override
            public boolean onUpdate(float seconds) {
                final Point2F dir = getDirection(TempUtils.POINT_1);
                ship1.flyTo(dir.x, dir.y, seconds);

                if (isKeyboardKeyPressed(KeyCode.VK_SPACE) || isMouseKeyPressed(MouseEvent.BUTTON1) || isPointerActive(1)) {
                    if (canShoot) {
                        final org.jbox2d.common.Transform tf = ship1.getTransform();
                        for (Point2F gun: ship1.getGuns()) {
                            final Point2F worldGun = ship1.mapPoint(TempUtils.tempPoint(gun));
                            box2dWorld.add(new Bullet(ship1, worldGun.x, worldGun.y, tf.q.c, tf.q.s, ship1.getAngle()));
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

        root.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                final BoundingBox2F box = viewport.getBounds();
                viewportText.setText(String.format("L: %.2f; T: %.2f; R: %.2f; B: %.2f; X: %.2f; Y: %.2f; SX: %.2f; SY: %.2f",
                        box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(),
                        viewport.getPositionX(), viewport.getPositionY(),
                        viewport.getScaleX(), viewport.getScaleY()));
                positionText.setText(String.format("Speed: %.2f",
                        FloatMathUtils.length(ship1.getVelocityX(), ship1.getVelocityY())));
                return true;
            }
        });
        root.add(multilineText);
        return root;
    }

}
