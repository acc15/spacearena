package ru.spacearena.game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.integration.box2d.Box2dObject;
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
        box2dWorld.setScale(0.05f, 0.05f);
        box2dWorld.setTimeScale(2f);

        viewport.add(box2dWorld);

        createWall(box2dWorld, -40, 20, 40, 20);
        createWall(box2dWorld, -40, -30, -40, 20);
        createWall(box2dWorld, 40, -30, 40, 20);

        for (int i=0; i<10; i++) {
            final Shape s = i % 2 == 0 ? createCircle(1f) : createBox(5f, 0.5f);
            createBody(box2dWorld, BodyType.DYNAMIC, s, (i-5)*4f, -i * 5f);
        }

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

    private static Vec2 vec1 = new Vec2();

    private static PolygonShape createBox(float hx, float hy) {
        final PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(hx, hy);
        return polygonShape;
    }

    private static PolygonShape createBox(float x, float y, float hx, float hy, float angle) {
        final PolygonShape polygonShape = new PolygonShape();
        vec1.set(x, y);
        polygonShape.setAsBox(hx, hy, vec1, angle);
        return polygonShape;
    }

    private static CircleShape createCircle(float r) {
        return createCircle(0f, 0f, r);
    }

    private static CircleShape createCircle(float x, float y, float r) {
        final CircleShape c = new CircleShape();
        c.m_p.set(x, y);
        c.m_radius = r;
        return c;
    }

    private Box2dObject createWall(Box2dWorld world, float x1, float y1, float x2, float y2) {
        return createBody(world, BodyType.STATIC, createEdge(x1, y1, x2, y2), 0, 0);
    }

    private Box2dObject createBody(Box2dWorld world, BodyType type, Shape shape, float x, float y) {
        final BodyDef bd = new BodyDef();
        bd.type = type;
        bd.position.set(x, y);

        final FixtureDef bf = new FixtureDef();
        bf.shape = shape;
        bf.density = 10f;
        bf.restitution = 0.7f;
        bf.friction = 0.05f;//0.01f;

        final Body bb = world.getWorld().createBody(bd);
        bb.createFixture(bf);

        final Box2dObject o = new Box2dObject();
        o.addBody(bb);
        world.add(o);
        return o;
    }

}
