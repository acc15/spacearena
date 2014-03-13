package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineEntity;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.collisions.CollisionContainer;
import ru.spacearena.engine.collisions.CollisionEntity;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyCode;
import ru.spacearena.engine.input.trackers.InputTracker;
import ru.vmsoftware.math.FloatMathUtils;
import ru.vmsoftware.math.geometry.shapes.AABB2F;
import ru.vmsoftware.math.geometry.shapes.Rect2FPP;

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

        final AABB2F mapBounds = new Rect2FPP(-2000f, -2000f, 2000f, 2000f);

        final Ship ship = new Ship() {

            @Override
            public boolean onCollision(CollisionEntity entity, boolean reference, Contact contact) {
                collisionText.setText("Collision: " + reference + " " + contact.getOverlapX() + " " + contact.getOverlapY());
                return super.onCollision(entity, reference, contact);
            }

        };
        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));

        final CollisionContainer collisionContainer = new CollisionContainer();
        collisionContainer.add(ship);

        final Ship s2 = new Ship();
        s2.setPosition(150, 150);
        collisionContainer.add(s2);

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
                    final Ship s = ships.getChild(i);
                    final float d = angle + (i*30);
                    final float r = FloatMathUtils.toRadiansTop(d-90);
                    final float x = FloatMathUtils.cos(r) * 500;
                    final float y = FloatMathUtils.sin(r) * 500;
                    s.setRotation(d+90);
                    s.setPosition(x,y);
                }
                angle += 100 * seconds;
                return true;
            }
        });
*/
        //ship.setAngularVelocity(360);

        root.add(new InputTracker() {

            boolean canShot = true;

            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getKeyboardDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT);
                final float yVelocity = getKeyboardDirection(KeyCode.VK_UP, KeyCode.VK_DOWN);

                if (FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    ship.accelerateTo(0, 0, Ship.ACCELERATION/10f, seconds);
                    ship.setAngularVelocity(0f);
                } else {
                    final float l = Ship.MAX_SPEED / FloatMathUtils.length(xVelocity, yVelocity);
                    final float angle = FloatMathUtils.angle(xVelocity, yVelocity);
                    ship.accelerateTo(xVelocity * l, yVelocity * l, Ship.ACCELERATION, seconds);
                    ship.rotateTo(angle, Ship.ANGULAR_VELOCITY, seconds);
                }

                /*
                if (!FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    final float angle = FloatMathUtils.angle(xVelocity, yVelocity);
                    ship.setAcceleration(2000f);
                    ship.setTargetVelocityByAngle(angle);
                    ship.setTargetRotation(angle);
                } else {
                    ship.setAcceleration(500f);
                    ship.setTargetVelocity(0, 0);
                }*/

                if (!isKeyboardKeyPressed(KeyCode.VK_SPACE)) {
                    canShot = true;
                    return true;
                }
                if (canShot) {
                    final float[] gunPositions = ship.getGunPositions();
                    for (int i=0; i<gunPositions.length; i+=2) {
                        viewport.add(new Bullet(gunPositions[i], gunPositions[i+1], ship.getRotation()));
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
        root.add(new BoundChecker(mapBounds, viewport));
        root.add(new EngineObject() {

            @Override
            public boolean onUpdate(float seconds) {
                final AABB2F bounds = viewport.getBounds();
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
