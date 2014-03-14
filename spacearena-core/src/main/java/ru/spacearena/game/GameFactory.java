package ru.spacearena.game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.integration.box2d.Box2dWorld;

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

        final Box2dWorld box2dWorld = new Box2dWorld(0, 9.81f);
        box2dWorld.setScale(0.1f, 0.1f);

        viewport.add(box2dWorld);

        final BodyDef floor = new BodyDef();
        floor.type = BodyType.STATIC;
        floor.position.set(0, 5f);

        final FixtureDef f = new FixtureDef();
        final EdgeShape poly = new EdgeShape();
        poly.set(new Vec2(-100, 0), new Vec2(100, 0));
        f.shape = poly;

        final Body b = box2dWorld.getWorld().createBody(floor);
        b.createFixture(f);

        box2dWorld.add(new Box2dBody(b));

        for (int i=0; i<50; i++) {
            final BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(0, -i);
            final FixtureDef bf = new FixtureDef();
            final CircleShape bfs = new CircleShape();
            bfs.setRadius(0.01f);
            bf.shape = bfs;
            bf.restitution = 0.7f;
            bf.friction = 0.1f;

            final Body bb = box2dWorld.getWorld().createBody(bd);
            bb.createFixture(bf);

            box2dWorld.add(new Box2dBody(bb));
        }

        root.add(new BoundChecker(mapBounds, viewport));
        root.add(multilineText);
        return root;
    }

}
