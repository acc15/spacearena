package ru.spacearena.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.spacearena.android.R;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.NewEngineObject;
import ru.spacearena.engine.common.BackgroundHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class GameFactory {

    public static Engine createEngine(Resources resources) {

        final Bitmap shipImage = BitmapFactory.decodeResource(resources, R.drawable.ship);

        return new Engine(new NewEngineObject().
                add(new NewEngineObject().addDrawHandler(new BackgroundHandler())));
    }

}
