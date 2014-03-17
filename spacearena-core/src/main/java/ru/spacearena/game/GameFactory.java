package ru.spacearena.game;

import org.jbox2d.collision.shapes.EdgeShape;
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

        engine.getDebug().setDrawAll(true);

        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer();

        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText.Line positionText = new MultilineText.Line();
        final MultilineText.Line viewportText = new MultilineText.Line();
        final MultilineText.Line collisionText = new MultilineText.Line();

        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);
        multilineText.add(viewportText);
        multilineText.add(collisionText);

        final BoundingBox2F mapBounds = new Rect2FPP(-2000f, -2000f, 2000f, 2000f);

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


        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));
        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-5, -5, 5, 5));

        root.add(viewport);
        root.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                final BoundingBox2F box = viewport.getBounds();
                viewportText.setText(String.format("L: %.2f; T: %.2f; R: %.2f; B: %.2f; X: %.2f; Y: %.2f; SX: %.2f; SY: %.2f",
                        box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(),
                        viewport.getX(), viewport.getY(),
                        viewport.getScaleX(), viewport.getScaleY()));
                return true;
            }
        });

        final Box2dWorld box2dWorld = new Box2dWorld(0, 0);
        box2dWorld.setScale(1/40f, 1/40f);
        box2dWorld.setTimeScale(2f);

        box2dWorld.add(new Ship());

        viewport.add(box2dWorld);


        root.add(new InputTracker() {

            private Vec2 force = new Vec2();

            @Override
            public boolean onUpdate(float seconds) {
                force.set(getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT),
                        getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN));
                if (FloatMathUtils.isZero(force.x, force.y)) {
                    return true;
                }

                force.normalize();
                force.mulLocal(10000f);

                return true;
            }
        });
        root.add(new BoundChecker(mapBounds, viewport));
        root.add(multilineText);
        return root;
    }

    private static EdgeShape createEdge(float x1, float y1, float x2, float y2) {
        final EdgeShape edgeShape = new EdgeShape();
        edgeShape.m_vertex1.set(x1, y1);
        edgeShape.m_vertex2.set(x2, y2);
        return edgeShape;
    }

}
