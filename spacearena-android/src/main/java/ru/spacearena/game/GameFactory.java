package ru.spacearena.game;

import android.content.res.Resources;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.Background;
import ru.spacearena.engine.common.FPSDisplay;
import ru.spacearena.engine.primitives.PointF;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine(Resources resources) {
        return new Engine(new EngineContainer().
                add(new Background()).
                add(new Viewport().
                        scale(new PointF(0.33f, 0.33f)).
                        add(new Sky()).
                        add(new Ship())).
                add(new FPSDisplay()));
    }

}
