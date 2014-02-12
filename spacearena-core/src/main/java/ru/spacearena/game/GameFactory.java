package ru.spacearena.game;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.common.Background;
import ru.spacearena.android.engine.common.FPSCounter;
import ru.spacearena.android.engine.common.Timer;
import ru.spacearena.android.engine.graphics.Color;
import ru.spacearena.android.engine.graphics.DrawContext;

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

        final FPSCounter fpsCounter = new FPSCounter();

        return new Engine(new EngineObject().
                add(fpsCounter).
                add(new Background()).
                add(rect).
                add(new Timer(1f/10, true).add(new EngineObject() {

                    private String str;

                    @Override
                    public boolean onUpdate(float seconds) {
                        str = String.format("FPS: %.2f", fpsCounter.getFps());
                        return true;
                    }

                    @Override
                    public void onDraw(DrawContext context) {
                        context.setColor(Color.WHITE);
                        context.drawText(str, 50, 50);
                    }
                })));
    }

}
