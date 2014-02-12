package ru.spacearena.game;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.common.Background;
import ru.spacearena.android.engine.common.FPSCounter;
import ru.spacearena.android.engine.common.Text;
import ru.spacearena.android.engine.common.Timer;
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

        final Text fpsText = new Text() {
            @Override
            public boolean onUpdate(float seconds) {
                setText(String.format("FPS: %.2f", fpsCounter.getFps()));
                return true;
            }
        };
        fpsText.setColor(Color.WHITE);
        fpsText.setPosition(0, 0);

        return new Engine(new EngineObject().
                add(fpsCounter).
                add(new Background()).
                add(rect).
                add(new Timer(1f/10, true).
                    add(fpsText)));
    }

}
