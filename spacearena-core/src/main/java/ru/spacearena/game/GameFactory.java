package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.common.viewport.GLViewportArea;
import ru.spacearena.engine.common.viewport.LargestSideAdjustStrategy;
import ru.spacearena.engine.common.viewport.PixelSizeAdjustStrategy;
import ru.spacearena.engine.common.viewport.Viewport;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.events.KeyCode;
import ru.spacearena.engine.events.trackers.InputTracker;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.Rect2FP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.integration.box2d.Box2dWorld;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.game.ship.Ship;

import java.awt.event.MouseEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineEntity createRoot(final Engine engine) {

        //engine.getDebug().setDrawAll(true);

        engine.setMaxFPS(0);
        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer();

        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText.Line timeText = new MultilineText.Line();

        root.add(new Background());

        root.add(new FPSCounter() {
            @Override
            public void onUpdate(float seconds) {
                super.onUpdate(seconds);
                fpsText.setText(String.format("FPS: %.2f", getFps()));
            }
        });
        root.add(new Watch() {
            @Override
            public void onUpdate(float seconds) {
                super.onUpdate(seconds);
                timeText.setText(String.format("Time: %.2f", getTime()));
            }
        });

        final Viewport screen = new Viewport(GLViewportArea.getInstance(), new PixelSizeAdjustStrategy());
        root.add(screen);

        final Viewport viewport = new Viewport(screen, new LargestSideAdjustStrategy(75f));
        screen.add(viewport);

        viewport.add(new Sky(viewport));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f, Color.WHITE));

        final Rect2FP levelBounds = new Rect2FP(-100f, -100f, 100f, 100f);

        final GenericContainer low = new GenericContainer();
        viewport.add(low);

        final Box2dWorld box2dWorld = new Box2dWorld();
        viewport.add(box2dWorld);
        box2dWorld.setFPS(60f);
        box2dWorld.add(new LevelBounds(levelBounds));

        final GenericContainer high = new GenericContainer();
        viewport.add(high);

        final Ship ship1 = new Ship(box2dWorld, low, high);
        ship1.setInitialPosition(0, -10);
        ship1.setInitialAngle(FloatMathUtils.HALF_PI);
        ship1.setAcceleration(100f);
        ship1.setMaxSpeed(100f);
        box2dWorld.add(ship1);

        for (int i=0; i<=0; i++) {
            for (int j=0; j<=10; j++) {
                final Ship ship2 = new Ship(box2dWorld, low, high) {

                    private final float fireInterval = 0.5f;
                    private float fireTimer = fireInterval;

                    @Override
                    public void onUpdate(float seconds) {
                        super.onUpdate(seconds);
                        final float px = ship1.getPositionX() - getPositionX(), py = ship1.getPositionY() - getPositionY();
                        flyTo(px, py, seconds);
//
//                        fireTimer -= seconds;
//                        if (fireTimer < 0) {
//                            fire(true);
//                            fire(false);
//                            fireTimer = fireInterval;
//                        }
                    }
                };
                ship2.setInitialPosition((j - 5) * 5, (i+1) * 7);
                ship2.setInitialAngle(-FloatMathUtils.HALF_PI);
                box2dWorld.add(ship2);
            }

        }

        root.add(new InputTracker() {

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
                viewport.getWorldSpace().transformPoint(pt);
                pt.sub(ship1.getPositionX(), ship1.getPositionY());
                return pt;
            }

            @Override
            public void onUpdate(float seconds) {
                final Point2F dir = getDirection();
                ship1.flyTo(dir.x, dir.y, seconds);

                final float zoomF = seconds;
                if (isKeyboardKeyPressed(KeyCode.VK_SUBTRACT)) {
                    viewport.setScale(viewport.getScaleX() * (1+zoomF));
                } else if (isKeyboardKeyPressed(KeyCode.VK_ADD)) {
                    viewport.setScale(viewport.getScaleX() * (1-zoomF));
                }

                ship1.fire(isKeyboardKeyPressed(KeyCode.VK_SPACE) || isMouseKeyPressed(MouseEvent.BUTTON1) || isPointerActive(1));
            }
        });
        root.add(new EngineObject() {
            @Override
            public void onUpdate(float seconds) {
                viewport.setPosition(ship1.getPositionX(), ship1.getPositionY());
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
        multilineText.add(timeText);

        screen.add(multilineText);

//        screen.add(new EngineObject() {
//
//            public final Texture.Definition IMAGE = new Texture.Definition().url(GameFactory.class, "tw.png");
//
//            private float blur = 1.0f;
//
//            @Override
//            public void onUpdate(float seconds) {
//                blur += seconds;
//            }
//
//            @Override
//            public void onDraw(DrawContext2f context) {
//                final VertexBuffer vb = context.getSharedBuffer();
//                final Texture t = context.obtain(IMAGE);
//                vb.reset(DefaultShaders.LAYOUT_P2T2);
//                vb.put(0,0).put(t.getLeft(), t.getTop()).
//                   put(0, t.getHeight()).put(t.getLeft(), t.getBottom()).
//                   put(t.getWidth(), t.getHeight()).put(t.getRight(), t.getBottom()).
//                   put(t.getWidth(), 0).put(t.getRight(), t.getTop());
//                context.use(Explosion.BLUR_PROGRAM).
//                        attrs(vb).
//                        uniform(context.getActiveMatrix()).
//                        uniform(IMAGE).
//                        uniform(blur / t.getWidth(), blur / t.getHeight()).
//                        draw(OpenGL.GL_TRIANGLE_FAN);
//            }
//        });

        return root;

    }

}
