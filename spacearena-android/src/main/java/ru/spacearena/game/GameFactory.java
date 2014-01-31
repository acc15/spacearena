package ru.spacearena.game;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import ru.spacearena.android.R;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.Background;
import ru.spacearena.engine.common.TextDisplay;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static final int TEXT_DISPLAY = 10;
    public static final int PLAYER_SHIP = 11;
    public static final int VIEWPORT = 12;

    public static Engine createEngine(Resources resources) {
        return new Engine(new EngineContainer().
                add(new Background()).
                add(new Viewport().
                        scale(new PointF(0.5f, 0.5f)).
                        add(new Player()).
                        add(new Sky()).
                        add(new Ship(BitmapFactory.decodeResource(resources, R.drawable.ship)).withId(PLAYER_SHIP)).
                        withId(VIEWPORT)).
                add(new TextDisplay().withId(TEXT_DISPLAY)));
    }

}
