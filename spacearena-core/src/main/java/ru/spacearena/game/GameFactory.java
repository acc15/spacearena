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

        final EngineContainer<EngineObject> root = new EngineContainer<EngineObject>();

        final FPSCounter fpsCounter = new FPSCounter();
        final MultilineText.Line fpsText = new MultilineText.Line();
        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);

        final EngineObject fpsUpdater = new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        };


        final Image image = engine.loadImage("ship.png");
        final PhysicalHandler ship = new PhysicalHandler();
        ship.add(new Sprite(image));
        ship.setPivot(image.getWidth() / 2, image.getHeight() / 2);
        ship.setPosition(engine.getWidth() / 2, engine.getHeight() / 2);

        //ship.setAngularVelocity(100f);

        final KeyTracker keyTracker = new KeyTracker() {
            @Override
            public boolean onUpdate(float seconds) {
                float xVelocity = 0;
                float yVelocity = 0;
                if (isKeyPressed(KeyCode.VK_LEFT)) {
                    xVelocity -= 1;
                }
                if (isKeyPressed(KeyCode.VK_RIGHT)) {
                    xVelocity += 1;
                }
                if (isKeyPressed(KeyCode.VK_UP)) {
                    yVelocity -= 1;
                }
                if (isKeyPressed(KeyCode.VK_DOWN)) {
                    yVelocity += 1;
                }
                if (FloatMathUtils.isZero(xVelocity, yVelocity)) {
                    ship.setVelocity(0, 0);
                    return true;
                }
                final float length = 500f/FloatMathUtils.length(xVelocity, yVelocity);
                ship.setVelocity(xVelocity * length, yVelocity * length);
                ship.setRotation(FloatMathUtils.angle(xVelocity, yVelocity));
                return true;
            }
        };


        return root.add(fpsCounter).
             add(new Timer(0.5f, true).add(fpsUpdater)).
             add(new Background()).
             add(keyTracker).
             add(ship).
             add(multilineText);
    }

}
