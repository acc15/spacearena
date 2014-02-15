package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.graphics.Image;
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
        multilineText.add(fpsText).add(positionText);

        final EngineObject fpsUpdater = new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        };

        final Image shipImage = engine.loadImage("ship.png");
        final Transform shipTransform = new Transform().add(new Sprite(shipImage));
        shipTransform.setPivot(shipImage.getWidth()/2, shipImage.getHeight()/2);

        final PhysicalHandler shipPhysics = new PhysicalHandler().add(shipTransform);
        final EngineContainer ship = new GenericContainer().
                add(shipPhysics).
                add(new EngineObject() {
                    @Override
                    public boolean onUpdate(float seconds) {
                        positionText.setText(String.format("Position: %.2f, %.2f", shipTransform.getX(), shipTransform.getY()));
                        return true;
                    }
                });

        final KeyTracker keyTracker = new KeyTracker() {
            @Override
            public boolean onUpdate(float seconds) {
                final float xVelocity = getDirection(KeyCode.VK_LEFT, KeyCode.VK_RIGHT);
                final float yVelocity = getDirection(KeyCode.VK_UP, KeyCode.VK_DOWN);
                if (FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    shipPhysics.setVelocity(0, 0);
                    return true;
                }
                final float length = 500f/FloatMathUtils.length(xVelocity, yVelocity);
                shipPhysics.setVelocity(xVelocity * length, yVelocity * length);
                shipTransform.setRotation(FloatMathUtils.angle(xVelocity, yVelocity));
                return true;
            }
        };

        final Rectangle r = new Rectangle(-5, -5, 5, 5);


        final Viewport gameView = new Viewport(new Viewport.LargestSideResolutionStrategy(2000f));
        gameView.setRotation(45);
        gameView.setChaseObject(shipTransform);
        gameView.add(r).add(keyTracker).add(ship);
        gameView.lookAt(0f, 0f);

        final PhysicalHandler viewportPhysics = new PhysicalHandler().add(gameView);
        viewportPhysics.setAngularVelocity(100f);

        return root.add(fpsCounter).
             add(new Timer(0.5f, true).add(fpsUpdater)).
             add(new Background()).
             add(viewportPhysics).
             add(multilineText);
    }

}
