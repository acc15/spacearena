package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.input.InputType;

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
        final Sprite ship = new Sprite(image);

        final float w = image.getWidth(), h = image.getHeight();

        ship.setPivot(w/2, h/2);
        ship.setPosition(300, 300);
        final PhysicsHandler physicsHandler = new PhysicsHandler(ship);

        physicsHandler.setAngularVelocity(100);

        return root.add(fpsCounter).
             add(new Timer(0.5f, true).add(fpsUpdater)).
             add(new Background()).
             add(ship).
             add(physicsHandler).
             add(multilineText);
    }

}
