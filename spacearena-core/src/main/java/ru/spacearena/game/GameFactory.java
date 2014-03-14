package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.geometry.shapes.BoundingBox2F;
import ru.spacearena.engine.geometry.shapes.Rect2FPP;
import ru.spacearena.engine.input.InputType;

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

        /*
        final Box2dWorld box2dWorld = new Box2dWorld();
        viewport.add(box2dWorld);

        final BodyDef floor = new BodyDef();
        floor.type = BodyType.STATIC;
        floor.position.set(0, 100);

        final FixtureDef f = new FixtureDef();
        final PolygonShape poly = new PolygonShape();
        poly.setAsBox(100, 10);
        f.shape = poly;

        final Body b = box2dWorld.getWorld().createBody(floor);
        b.createFixture(f);

        box2dWorld.add(new Box2dBody(b));
        */

        root.add(new BoundChecker(mapBounds, viewport));
        root.add(multilineText);
        return root;
    }

}
