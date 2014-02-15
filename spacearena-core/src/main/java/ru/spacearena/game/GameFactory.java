package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.KeyCode;
import ru.spacearena.engine.input.KeyTracker;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory implements EngineFactory {

    public EngineObject createRoot(Engine engine) {

        engine.enableInput(InputType.KEYBOARD);
        engine.enableInput(InputType.MOUSE);
        engine.enableInput(InputType.TOUCH);

        final GenericContainer root = new GenericContainer();

        final FPSCounter fpsCounter = new FPSCounter();
        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText.Line positionText = new MultilineText.Line();
        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(positionText);

        final EngineObject fpsUpdater = new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        };

        final Ship ship = new Ship();
        ship.add(new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                positionText.setText(String.format("Position: %.2f, %.2f",
                        ship.getTransform().getX(), ship.getTransform().getY()));
                return true;
            }
        });

        final KeyTracker keyTracker = new KeyTracker() {
            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT);
                final float yVelocity = getDirection(KeyCode.VK_UP, KeyCode.VK_DOWN);
                if (FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    ship.getPhysics().setVelocity(0, 0);
                    return true;
                }
                final float length = 500f/FloatMathUtils.length(xVelocity, yVelocity);
                ship.getPhysics().setVelocity(xVelocity * length, yVelocity * length);
                ship.getTransform().setRotation(FloatMathUtils.angle(xVelocity, yVelocity));
                return true;
            }
        };

        final Rectangle r = new Rectangle(-5, -5, 5, 5);


        final Viewport viewport = new Viewport(new Viewport.LargestSideAdjustStrategy(2000f));
        //viewport.setRotation(45);

        viewport.add(r);
        viewport.add(ship);
        viewport.setPosition(0f, 0f);

        final PositionHandler viewportPosition = new PositionHandler(ship.getTransform(), viewport);

        root.add(new Background());
        root.add(fpsCounter);

        final Timer timer = new Timer(0.5f, true);
        timer.add(fpsUpdater);
        root.add(timer);
        root.add(keyTracker);
        root.add(viewport);
        root.add(viewportPosition);
        root.add(multilineText);
        return root;
    }

}
