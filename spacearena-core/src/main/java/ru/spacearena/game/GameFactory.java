package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.geometry.shapes.Rect2IP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.integration.box2d.Box2dWorld;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

//    private static final class Ship extends Transform {
//
//        private static final TextureDefinition td = new TextureDefinition().url(GameFactory.class, "ship.png");
//
//        @Override
//        public void onInit(DrawContext context) {
//            final Texture t = context.get(td);
//            setPivot(t.getWidth()/3, t.getHeight()/2);
//        }
//
//        @Override
//        public boolean onUpdate(float seconds) {
//            rotate(FloatMathUtils.TWO_PI * seconds);
//            //scale(1.01f);
//            return true;
//        }
//
//        @Override
//        public void onDrawTransformed(DrawContext context) {
//            context.drawImage(0,0,td);
//        }
//    }

    private static class ColorRect extends Rect2IP {
        public final Color c = new Color();
    }


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


        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(75f));
        viewport.add(new Sky(viewport));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f, Color.WHITE));

        viewport.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                viewport.translate(10f*seconds, 0);
                return true;
            }
        });

        final Rect2FP levelBounds = new Rect2FP(-100f, -100f, 100f, 100f);

        final Box2dWorld box2dWorld = new Box2dWorld();
        box2dWorld.setFPS(60f);
        box2dWorld.add(new LevelBounds(levelBounds));
        viewport.add(box2dWorld);


        root.add(viewport);

//
//
//        final GenericContainer fxContainer = new GenericContainer();
//
//        final Ship ship1 = new Ship(fxContainer);
//        ship1.setInitialPosition(0, -10);
//        ship1.setInitialAngle(FloatMathUtils.HALF_PI);
//        box2dWorld.add(ship1);
//
//        for (int i=0; i<=10; i++) {
//            final Ship ship2 = new Ship(fxContainer);
//            ship2.setInitialPosition((i - 5) * 5, 7);
//            ship2.setInitialAngle(-FloatMathUtils.HALF_PI);
//            box2dWorld.add(ship2);
//        }
//
//
//
//        root.add(new InputTracker() {
//
//            private boolean canShoot = true;
//
//            private Point2F getDirection(Point2F pt) {
//                pt.set(getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT),
//                       getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN));
//                if (!pt.isZero()) {
//                    return pt;
//                }
//                if (isMouseKeyPressed(MouseEvent.BUTTON3)) {
//                    pt.set(getMouseX(), getMouseY());
//                } else if (isPointerActive(0)) {
//                    pt.set(getPointerX(0), getPointerY(0));
//                } else {
//                    return pt;
//                }
//                viewport.getWorldSpace().transformPoint(pt);
//                pt.sub(ship1.getPositionX(), ship1.getPositionY());
//                return pt;
//            }
//
//            @Override
//            public boolean onUpdate(float seconds) {
//                final Point2F dir = getDirection(TempUtils.POINT_1);
//                ship1.flyTo(dir.x, dir.y, seconds);
//
//                if (isKeyboardKeyPressed(KeyCode.VK_SUBTRACT)) {
//                    viewport.setScale(viewport.getScaleX() + 0.001f);
//                } else if (isKeyboardKeyPressed(KeyCode.VK_ADD)) {
//                    viewport.setScale(viewport.getScaleX() - 0.001f);
//                }
//
//                if (isKeyboardKeyPressed(KeyCode.VK_SPACE) || isMouseKeyPressed(MouseEvent.BUTTON1) || isPointerActive(1)) {
//                    if (canShoot) {
//                        final org.jbox2d.common.Transform tf = ship1.getTransform();
//                        for (Point2F gun: ship1.getGuns()) {
//                            final Point2F worldGun = ship1.mapPoint(TempUtils.tempPoint(gun));
//                            final Bullet bullet = new Bullet(ship1, worldGun.x, worldGun.y, tf.q.c, tf.q.s, ship1.getAngle());
//                            box2dWorld.add(bullet);
//                        }
//                        canShoot = false;
//                    }
//                } else {
//                    canShoot = true;
//                }
//
//                return true;
//            }
//        });
//        root.add(new EngineObject() {
//            @Override
//            public boolean onUpdate(float seconds) {
//                viewport.setPosition(ship1.getPositionX(), ship1.getPositionY());
//                return true;
//            }
//        });
//        root.add(new BoundChecker(levelBounds, viewport));
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

        final Viewport screen = new Viewport(new Viewport.RealSizeAdjustStrategy());

        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);
        multilineText.add(viewportText);
        multilineText.add(collisionText);
        screen.add(multilineText);

        root.add(screen);

        return root;
    }

}
