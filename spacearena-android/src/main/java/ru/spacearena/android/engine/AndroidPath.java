package ru.spacearena.android.engine;

import ru.spacearena.engine.graphics.Path;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-03
 */
public class AndroidPath implements Path {

    final android.graphics.Path androidPath;

    public AndroidPath() {
        this.androidPath = new android.graphics.Path();
    }

    public AndroidPath(android.graphics.Path androidPath) {
        this.androidPath = androidPath;
    }

    public void reset() {
        androidPath.reset();
    }

    public void moveTo(float x, float y) {
        androidPath.moveTo(x, y);
    }

    public void lineTo(float x, float y) {
        androidPath.lineTo(x, y);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        androidPath.quadTo(x1, y1, x2, y2);
    }
}
