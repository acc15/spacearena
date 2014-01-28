package ru.spacearena.android.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import ru.spacearena.android.R;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Ship implements EngineObject {

    private Bitmap bitmap;

    public Ship(Resources resources) {
        this.bitmap = BitmapFactory.decodeResource(resources, R.drawable.ship);
    }

    public boolean process(float timeDelta) {
        // TODO implement..
        return false;
    }

    public boolean onTouch(MotionEvent motionEvent) {
        // TODO implement..
        return false;
    }

    public void render(Canvas canvas) {
        // TODO implement..

    }
}
