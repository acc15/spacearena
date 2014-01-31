package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.Background;
import ru.spacearena.engine.common.FPSDisplay;
import ru.spacearena.engine.primitives.Point2F;
import ru.spacearena.engine.PlatformManager;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine(PlatformManager platformManager) {
        return new Engine(platformManager, new EngineContainer().
                add(new Background()).
                add(new Viewport().
                        scale(new Point2F(0.33f, 0.33f)).
                        add(new Sky()).
                        add(new Ship())).
                add(new FPSDisplay()));
    }

}
