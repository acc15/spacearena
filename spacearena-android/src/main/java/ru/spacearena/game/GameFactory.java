package ru.spacearena.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.spacearena.android.R;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.Viewport;
import ru.spacearena.engine.common.Background;
import ru.spacearena.engine.common.TextDisplay;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine(Resources resources) {

        final Bitmap shipImage = BitmapFactory.decodeResource(resources, R.drawable.ship);

        final TextDisplay textDisplay = new TextDisplay();

        final Ship ship = new Ship(shipImage);
        final Viewport viewport = new Viewport();
        final Player player = new Player(textDisplay, viewport, ship);
        final Sky sky = new Sky(viewport);

        viewport.scale(Point2F.cartesian(0.5f, 0.5f)).add(player).add(sky).add(ship);

        return new Engine(new EngineContainer().
                add(new Background()).
                add(viewport).
                add(textDisplay));
    }

}
