package ru.spacearena.engine.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ru.spacearena.engine.EngineObject;

import java.util.ArrayList;
import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-01
*/
public class TextDisplay extends EngineObject {

    private final Paint paint = new Paint();

    private List<String> messages = new ArrayList<String>();

    public TextDisplay() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
    }

    public void printMessage(String message) {
        messages.add(message);
    }

    public boolean process(float time) {
        return true;
    }

    public void render(Canvas canvas) {
        int pos = 0;
        for (String message: messages) {
            pos += 30;
            canvas.drawText(message, 0, pos, paint);
        }
        messages.clear();

    }
}
