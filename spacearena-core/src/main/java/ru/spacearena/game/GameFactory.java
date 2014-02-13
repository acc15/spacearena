package ru.spacearena.game;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineContainer;
import ru.spacearena.android.engine.EngineFactory;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.common.Background;
import ru.spacearena.android.engine.common.FPSCounter;
import ru.spacearena.android.engine.common.MultilineText;
import ru.spacearena.android.engine.common.Timer;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.input.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine(EngineFactory engineFactory) {

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

        root.add(fpsCounter).
             add(new Timer(0.5f, true).add(fpsUpdater)).
             add(new Background());

        for (int i=0; i<200; i++) {

            final Rectangle rect = new Rectangle();
            rect.setColor(Color.rgb((float)Math.random(), (float)Math.random(), (float)Math.random()));
            rect.setPosition(320, 240);
            rect.setScale((float)(Math.random() * 50), (float)(Math.random() * 50));
            rect.setPivot(.5f, .5f);
            rect.setVelocity((float)(Math.random() * 200)-100, (float)(Math.random() * 200)-100);
            rect.setAngularVelocity((float)(Math.random() * 360));
            root.add(rect);

        }
        root.add(multilineText);


        final Engine engine = new Engine(root);
        engine.onInit(engineFactory);
        engineFactory.enableInput(InputType.KEYBOARD);
        engineFactory.enableInput(InputType.MOUSE);
        engineFactory.enableInput(InputType.TOUCH);
        return engine;
    }

}
