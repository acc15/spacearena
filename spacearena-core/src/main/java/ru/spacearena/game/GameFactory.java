package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.font.DistanceFieldProgram;
import ru.spacearena.engine.graphics.texture.Texture;
import ru.spacearena.engine.graphics.texture.TextureDefinition;
import ru.spacearena.engine.graphics.texture.TextureProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    private static final class Ship extends Transform {

        private static final TextureDefinition td = new TextureDefinition();

        public Ship(float x, float y) {
            setPosition(x, y);
        }

        @Override
        public void onInit(DrawContext context) {
            if (!context.has(td)) {
                context.load(td, GameFactory.class.getResource("ship.png"));
            }
        }

        @Override
        public boolean onUpdate(float seconds) {
            rotate(FloatMathUtils.TWO_PI * seconds);
            //scale(-0.1f*seconds, -0.1f*seconds);
            return true;
        }

        @Override
        public void onDrawTransformed(DrawContext context) {
            context.drawImage(-10, -6, 10, 6, td);
        }
    }

    public EngineEntity createRoot(final Engine engine) {

        //engine.getDebug().setDrawAll(true);

        engine.setMaxFPS(0);
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

        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(100));
//        viewport.add(new Ship(0, 0));
//        viewport.add(new Ship(5, 5));
        root.add(viewport);

        //final Viewport hud = new Viewport(new Viewport.RealSizeAdjustStrategy());
        viewport.add(new Transform() {
            private float sizeV = 0f;
            private final float minSize = 100f;
            private final float maxSize = 500f;
            private float size = minSize;

            private final TextureDefinition td = new TextureDefinition(OpenGL.LINEAR, OpenGL.LINEAR);

            private final VertexBuffer vb = new VertexBuffer(100);

            @Override
            public void onInit(DrawContext context) {
                context.load(td, GameFactory.class.getResource("df-size-segoe.png"));
            }

            @Override
            public boolean onUpdate(float seconds) {
                sizeV += 100f * seconds;
                size += sizeV * seconds;
                if (size > maxSize) {
                    size = maxSize;
                    sizeV = -sizeV;
                } else if (size < minSize) {
                    size = minSize;
                    sizeV = -sizeV;
                }
                rotate(FloatMathUtils.HALF_PI * seconds);
                return true;
            }

            @Override
            public void onDrawTransformed(DrawContext context) {
                final Texture t = context.getTexture(td);
                final float tScale = 0.002f;
                final float hw = size * t.getAspectRatio() * tScale, hh = size * tScale;
                vb.reset(TextureProgram.LAYOUT_PT2).
                   put(-size * hw, -size * hh).put(0, 1).
                   put(-size * hw, size * hh).put(0, 0).
                   put(size * hw, size * hh).put(1, 0).
                   put(-size * hw, -size * hh).put(0, 1).
                   put(size * hw, size * hh).put(1, 0).
                   put(size * hw, -size * hh).put(1, 1);
                context.use(DistanceFieldProgram.DEFINITION).
                        bindAttr(DistanceFieldProgram.POSITION_ATTR, vb, 0).
                        bindAttr(DistanceFieldProgram.TEXCOORD_ATTR, vb, 1).
                        bindUniform(DistanceFieldProgram.MATRIX_UNIFORM, context.getActiveMatrix()).
                        bindUniform(DistanceFieldProgram.COLOR_UNIFORM, Color.WHITE).
                        bindUniform(DistanceFieldProgram.TEXTURE_UNIFORM, td, 0).
                        draw(OpenGL.TRIANGLES);

//                context.drawText("FPS: " + f.getFps(), 0, 0, FontRepository.SEGOE_UI_LIGHT, size, Color.YELLOW);
//                context.drawText("Size: " + size, 0, 100, FontRepository.SEGOE_UI_LIGHT, 18f, Color.GREEN);
            }
        });
        //root.add(hud);

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
