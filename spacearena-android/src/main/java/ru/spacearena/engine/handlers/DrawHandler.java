package ru.spacearena.engine.handlers;

import android.graphics.Canvas;

/**
* @author Vyacheslav Mayorov
* @since 2014-11-02
*/
public interface DrawHandler {
    void onDraw(Canvas canvas);
    void onPreDraw(Canvas canvas);
    void onPostDraw(Canvas canvas);
}
