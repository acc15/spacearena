package ru.spacearena.game;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineContainer;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.common.*;
import ru.spacearena.android.engine.graphics.Color;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine() {
        final Rectangle rect = new Rectangle();
        rect.setColor(Color.RED);
        rect.setPosition(100, 100);
        rect.setScale(100, 50);
        rect.setPivot(.5f, .5f);
        rect.setVelocity(10, 10);
        rect.setAngularVelocity(360);

        final FPSCounter fpsCounter = new FPSCounter();

        final MultilineText.Line fpsText = new MultilineText.Line();

        final MultilineText multilineText = new MultilineText();
        multilineText.add(fpsText);
        multilineText.add(new MultilineText.Line("TEST"));

        final EngineObject fpsUpdater = new EngineObject() {
            @Override
            public boolean onUpdate(float seconds) {
                fpsText.setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        };

        return new Engine(new EngineContainer<EngineObject>().
                add(fpsCounter).
                add(new Timer(0.5f, true).add(fpsUpdater)).
                add(new Background()).
                add(rect).
                add(multilineText));
    }

}
