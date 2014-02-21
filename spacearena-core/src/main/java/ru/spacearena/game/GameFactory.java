package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.collisions.CollisionContainer;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.geom.AABB;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyCode;
import ru.spacearena.engine.input.trackers.InputTracker;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineEntity createRoot(final Engine engine) {

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

        final AABB mapBounds = new AABB(-20000f, -20000f, 20000f, 20000f);

        final Ship ship = new Ship() {

            @Override
            public boolean onCollision(CollisionContainer.CollisionEntity entity, boolean first, float penetrationX, float penetrationY) {
                collisionText.setText("Collision: " + first + " " + penetrationX + " " + penetrationY);
                return super.onCollision(entity, first, penetrationX, penetrationY);
            }

            @Override
            public void applyVelocities(float seconds) {
                collisionText.setText("No collision");
                super.applyVelocities(seconds);
            }
        };
        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));


        final CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.add(ship);

        for (int i=0; i<5; i++) {

            final Ship s2 = new Ship();
            s2.setPosition(200 * i, 500);
            collisionContainer.add(s2);

        }

/*
        final List<Ship> ships = new ArrayList<Ship>();
        for (int i=0; i<10; i++) {
            final Ship s = new Ship();
            s.setPosition((i+1) * 500, (i+1) * 500);
            ships.add(s);
            collisionContainer.add(s);
        }

        viewport.add(new EngineObject() {

            float angle = 0f;

            @Override
            public boolean onUpdate(float seconds) {
                for (int i=0; i<ships.size(); i++) {
                    final Ship s = ships.get(i);
                    final float d = angle + (i*30);
                    final float r = FloatMathUtils.toRadians(d-90);
                    final float x = FloatMathUtils.cos(r) * 500;
                    final float y = FloatMathUtils.sin(r) * 500;
                    s.setAngle(d+90);
                    s.setPosition(x,y);
                }
                angle += 100 * seconds;
                return true;
            }
        });
*/
        root.add(new InputTracker() {

            boolean canShot = true;

            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT, 1f);
                final float yVelocity = getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN, 1f);
                if (!FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    final float length = ship.getSpeed() / FloatMathUtils.length(xVelocity, yVelocity);
                    ship.setAcceleration(2000f);
                    ship.setTargetVelocity(xVelocity*length, yVelocity*length);

                    final float angle = FloatMathUtils.angle(xVelocity, yVelocity);
                    ship.setTargetAngle(angle);
                } else {
                    ship.setAcceleration(500f);
                    ship.setTargetVelocity(0, 0);
                }

                if (!isKeyboardKeyPressed(KeyCode.VK_SPACE)) {
                    canShot = true;
                    return true;
                }
                if (canShot) {
                    final float[] gunPositions = ship.getGunPositions();
                    for (int i=0; i<gunPositions.length; i+=2) {
                        viewport.add(new Bullet(mapBounds, gunPositions[i], gunPositions[i+1], ship.getAngle()));
                    }
                    canShot = false;
                }
                return true;
            }
        });

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

        root.add(viewport);

        viewport.add(new Sky(viewport, new Random()));
        viewport.add(new Rectangle(-5, -5, 5, 5));
        viewport.add(collisionContainer);

        root.add(new PositionHandler(ship, viewport));
        root.add(new BoundChecker(mapBounds, ship));
        root.add(new BoundChecker(mapBounds, viewport));
        root.add(new EngineObject() {

            @Override
            public boolean onUpdate(float seconds) {
                final Bounds bounds = viewport.getTransformedBounds();
                viewportText.setText(String.format("Viewport: (%.2f,%.2f)(%.2f,%.2f)",
                        bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY()));
                positionText.setText(String.format("Position: %.2f, %.2f",
                        ship.getX(), ship.getY()));
                return true;
            }
        });

        root.add(multilineText);
        return root;
    }

}
