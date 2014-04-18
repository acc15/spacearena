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
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.integration.box2d.Box2dWorld;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.game.ship.Ship;

import java.awt.event.MouseEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineEntity createRoot(final Engine engine) {

        //engine.getDebug().setDrawAll(true);

        engine.setMaxFPS(100);
        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer();

        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText.Line positionText = new MultilineText.Line();
        final MultilineText.Line viewportText = new MultilineText.Line();
        final MultilineText.Line collisionText = new MultilineText.Line();

        root.add(new Background());

        final FPSCounter fpsCounter = new FPSCounter() {
            @Override
            public boolean onUpdate(float seconds) {
                if (!super.onUpdate(seconds)) {
                    return false;
                }
                fpsText.setText(String.format("FPS: %.2f", getFps()));
                return true;
            }
        };
        root.add(fpsCounter);

        final Viewport screen = new Viewport(new Viewport.RealSizeAdjustStrategy());
        root.add(screen);

        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(75f));
        root.add(viewport);

        viewport.add(new Sky(viewport));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f, Color.WHITE));

//        viewport.add(new EngineObject() {
//            @Override
//            public boolean onUpdate(float seconds) {
//                viewport.translate(10f*seconds, 0);
//                return true;
//            }
//        });

        final Rect2FP levelBounds = new Rect2FP(-100f, -100f, 100f, 100f);

        final Box2dWorld box2dWorld = new Box2dWorld();

        viewport.add(box2dWorld);
        box2dWorld.setFPS(60f);
        box2dWorld.add(new LevelBounds(levelBounds));

        final GenericContainer fxContainer = new GenericContainer();

        final Ship ship1 = new Ship(fxContainer);
        ship1.setInitialPosition(0, -10);
        ship1.setInitialAngle(FloatMathUtils.HALF_PI);
        box2dWorld.add(ship1);

        for (int i=0; i<=0; i++) {
            for (int j=0; j<=10; j++) {
                final Ship ship2 = new Ship(fxContainer);
                ship2.setInitialPosition((j - 5) * 5, (i+1) * 7);
                ship2.setInitialAngle(-FloatMathUtils.HALF_PI);
                box2dWorld.add(ship2);
            }

        }

        root.add(new InputTracker() {

            private boolean canShoot = true;
            private final Point2F pt = new Point2F();

            private Point2F getDirection() {
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
                screen.getLocalSpace().transformPoint(pt);
                viewport.getWorldSpace().transformPoint(pt);
                pt.sub(ship1.getPositionX(), ship1.getPositionY());
                return pt;
            }

            @Override
            public boolean onUpdate(float seconds) {
                final Point2F dir = getDirection();
                ship1.flyTo(dir.x, dir.y, seconds);

                if (isKeyboardKeyPressed(KeyCode.VK_SUBTRACT)) {
                    viewport.setScale(viewport.getScaleX() + 0.001f);
                } else if (isKeyboardKeyPressed(KeyCode.VK_ADD)) {
                    viewport.setScale(viewport.getScaleX() - 0.001f);
                }

                if (isKeyboardKeyPressed(KeyCode.VK_SPACE) || isMouseKeyPressed(MouseEvent.BUTTON1) || isPointerActive(1)) {
                    if (canShoot) {
                        final org.jbox2d.common.Transform tf = ship1.getTransform();
                        for (Point2F gun: ship1.getGuns()) {
                            final Point2F worldGun = ship1.mapPoint(TempUtils.tempPoint(gun));
                            final Bullet bullet = new Bullet(ship1, worldGun.x, worldGun.y, tf.q.c, tf.q.s, ship1.getAngle());
                            box2dWorld.add(bullet);
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
//
//        root.add(new EngineObject() {
//            private float time;
//
//            private long t0 = -1;
//
//            @Override
//            public boolean onUpdate(float seconds) {
//                final Rect2F box = viewport.getBounds();
//                viewportText.setText(String.format("L: %.2f; T: %.2f; R: %.2f; B: %.2f; X: %.2f; Y: %.2f; SX: %.2f; SY: %.2f",
//                        box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(),
//                        viewport.getPositionX(), viewport.getPositionY(),
//                        viewport.getScaleX(), viewport.getScaleY()));
//
//                float bulletSpeed = 0;
//                for (Box2dObject b2o: box2dWorld.getChildren()) {
//                    if (b2o instanceof Bullet) {
//                        final Bullet bt = (Bullet) b2o;
//                        bulletSpeed = FloatMathUtils.length(bt.getVelocityX(), bt.getVelocityY());
//                        break;
//                    }
//                }
//
//                positionText.setText(String.format("Speed: %.2f; Bullet speed: %.2f",
//                        FloatMathUtils.length(ship1.getVelocityX(), ship1.getVelocityY()), bulletSpeed));
//                time += seconds;
//
//                final long ct = System.currentTimeMillis();
//                if (t0 < 0) {
//                    t0 = ct - (long)(engine.getTimer().getElapsedSeconds() * 1000f);
//                }
//
//                final float real = (float)(ct - t0)/1000;
//                collisionText.setText(String.format("Time: %.4f; Real: %.4f; Diff: %.4f", time, real, time - real));
//                return true;
//            }
//        });

        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);
        multilineText.add(viewportText);
        multilineText.add(collisionText);
        screen.add(multilineText);

        return root;

    }

}
