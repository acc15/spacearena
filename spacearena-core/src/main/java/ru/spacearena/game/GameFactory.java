package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.geometry.shapes.Rect2IP;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.font.FontRepository;
import ru.spacearena.engine.graphics.texture.TextureDefinition;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    private static final class Ship extends Transform {

        private static final TextureDefinition td = new TextureDefinition().url(GameFactory.class, "ship.png");

        public Ship(float x, float y) {
            setPosition(x, y);
        }

        @Override
        public boolean onUpdate(float seconds) {
            rotate(FloatMathUtils.TWO_PI * seconds);
            //setPivot();
            //scale(-0.1f*seconds, -0.1f*seconds);
            return true;
        }

        @Override
        public void onDrawTransformed(DrawContext context) {
            context.drawImage(0,0,td);
        }
    }

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
        root.add(new Background());

        final FPSCounter f = new FPSCounter() {
            @Override
            public boolean onUpdate(float seconds) {
                if (computeFPS(seconds)) {
                    System.out.println(getFps());
                }
                return true;
            }
        };
        root.add(f);

        //final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(100));


        final Viewport viewport = new Viewport(new Viewport.RealSizeAdjustStrategy());
        root.add(viewport);
//
//        viewport.add(new Ship(100, 100));
//        viewport.add(new Ship(200, 200));


        viewport.add(new EngineObject() {

            private float scale = 1;
            private float v = 10f;
            private float minSmooth = 1;
            private float maxSmooth = 1000f;

            @Override
            public boolean onUpdate(float seconds) {
                v += 50f * seconds;
                scale += v * seconds;
                if (scale > maxSmooth) {
                    scale = maxSmooth;
                    v = -v;
                } else if (scale < minSmooth) {
                    scale = minSmooth;
                    v = -v;
                }
                return true;
            }

            @Override
            public void onDraw(DrawContext context) {

                //context.dpToPx()

//                context.drawText(String.format("FPS: %d",
//                        f.getFrameCount()), 100, 100, FontRepository.SEGOE_UI_LIGHT, 15, Color.WHITE);
//
//
                int y = 0;
                for (int i=0; i<1; i++) {
                    final int size = i * 2 + 4;
                    context.drawText(String.format("Time: %1$tH:%1$tM:%1$tS.%1$tL", System.currentTimeMillis()), 0, 0,
                            FontRepository.CALIBRI, scale, Color.WHITE);
                    y += size;
                }
//
//                y = 0;
//                for (int i=0; i<10; i++) {
//                    final int size = i * 2 + 40;
//                    context.drawText(String.format("FPS: %d", f.getFrameCount()), 200, y, FontRepository.CALIBRI, size, Color.WHITE);
//                    y += size;
//                }
                //context.drawImage(10,10,FontRepository.SEGOE_UI.getTexture());//FontRepository.SEGOE_UI.getTexture());
            }
        });
        /*
        viewport.add(new Transform() {

            private final RectPacker rectPacker = new RectPacker();
            private final List<ColorRect> rectangles = new ArrayList<ColorRect>();

            private void initLayout() {
                rectPacker.reset();
                rectangles.clear();

                final int rectCount = 256;
                for (int i=0; i<rectCount; i++) {
                    final ColorRect r = new ColorRect();
                    r.r = TempUtils.RAND.nextIntBetween(5, 50);
                    r.b = TempUtils.RAND.nextIntBetween(5, 50);
                    r.c.set(TempUtils.RAND.nextFloatBetween(0.25f, 1f),
                            TempUtils.RAND.nextFloatBetween(0.25f, 1f),
                            TempUtils.RAND.nextFloatBetween(0.25f, 1f));
                    rectangles.add(r);
                }
                Collections.sort(rectangles, new Comparator<ColorRect>() {
                    public int compare(ColorRect o1, ColorRect o2) {
                        final int m1 = IntMathUtils.max(o1.getWidth(), o1.getHeight());
                        final int m2 = IntMathUtils.max(o2.getWidth(), o2.getHeight());
                        return -IntMathUtils.compare(m1, m2);
//                        return -IntMathUtils.compare(o1.getWidth(), o2.getWidth());
                    }
                });
                rectPacker.pack(rectangles);
            }

            @Override
            public boolean onInput(InputEvent inputEvent) {
                MouseEvent me = inputEvent.asMouseEvent();
                if (me == null) {
                    return true;
                }
                if (me.getAction() != MouseEvent.Action.CLICK) {
                    return true;
                }
                if (me.getButton() != MouseEvent.LEFT_BUTTON) {
                    return true;
                }
                initLayout();
                return true;
            }

            @Override
            public void onAttach(Engine engine) {
                initLayout();
            }


            @Override
            public void onDraw(DrawContext context) {
                for (Rect2I f: rectPacker.getFreeAreas()) {
                    context.fillRect(f.getLeft(), f.getTop(), f.getRight(), f.getBottom(), new Color(1,1,1,0.1f));
                }
                for (ColorRect r: rectangles) {
                    context.fillRect(r.l, r.t, r.r, r.b, r.c);
                    context.drawRect(r.l, r.t, r.r, r.b, Color.WHITE);
                }
                context.drawRect(0,0,rectPacker.getPackWidth(),rectPacker.getPackHeight(), Color.WHITE);
            }
        });
        */

        /*
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
        viewport.add(new Sky(viewport));
        viewport.add(new Rectangle(-0.5f, -0.5f, 0.5f, 0.5f));

        final GenericContainer fxContainer = new GenericContainer();
        viewport.add(fxContainer);

        final Box2dWorld box2dWorld = new Box2dWorld();
        box2dWorld.setFPS(60f);
        box2dWorld.add(new LevelBounds(levelBounds));

        final Ship ship1 = new Ship(fxContainer);
        ship1.setInitialPosition(0, -10);
        ship1.setInitialAngle(FloatMathUtils.HALF_PI);
        box2dWorld.add(ship1);

        for (int i=0; i<=10; i++) {
            final Ship ship2 = new Ship(fxContainer);
            ship2.setInitialPosition((i - 5) * 5, 7);
            ship2.setInitialAngle(-FloatMathUtils.HALF_PI);
            box2dWorld.add(ship2);
        }

        viewport.add(box2dWorld);

        root.add(viewport);
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

        root.add(new EngineObject() {
            private float time;

            private long t0 = -1;

            @Override
            public boolean onUpdate(float seconds) {
                final BoundingBox2F box = viewport.getBounds();
                viewportText.setText(String.format("L: %.2f; T: %.2f; R: %.2f; B: %.2f; X: %.2f; Y: %.2f; SX: %.2f; SY: %.2f",
                        box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(),
                        viewport.getPositionX(), viewport.getPositionY(),
                        viewport.getScaleX(), viewport.getScaleY()));

                float bulletSpeed = 0;
                for (Box2dObject b2o: box2dWorld.getChildren()) {
                    if (b2o instanceof Bullet) {
                        final Bullet bt = (Bullet) b2o;
                        bulletSpeed = FloatMathUtils.length(bt.getVelocityX(), bt.getVelocityY());
                        break;
                    }
                }

                positionText.setText(String.format("Speed: %.2f; Bullet speed: %.2f",
                        FloatMathUtils.length(ship1.getVelocityX(), ship1.getVelocityY()), bulletSpeed));
                time += seconds;

                final long ct = System.currentTimeMillis();
                if (t0 < 0) {
                    t0 = ct - (long)(engine.getTimer().getElapsedSeconds() * 1000f);
                }

                final float real = (float)(ct - t0)/1000;
                collisionText.setText(String.format("Time: %.4f; Real: %.4f; Diff: %.4f", time, real, time - real));
                return true;
            }
        });
        root.add(multilineText);
        */
        return root;
    }

}
