package ru.spacearena.android.engine;

import android.view.MotionEvent;
import android.view.View;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.events.TouchEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class AndroidInputContext implements InputContext {

    private final View view;

    public AndroidInputContext(View view) {
        this.view = view;
    }

    public static TouchEvent.Action mapAction(int motionAction) {
        switch (motionAction) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            return TouchEvent.Action.DOWN;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            return TouchEvent.Action.UP;

        case MotionEvent.ACTION_MOVE:
            return TouchEvent.Action.MOVE;

        case MotionEvent.ACTION_CANCEL:
            return TouchEvent.Action.CANCEL;
        }
        throw new IllegalArgumentException(String.format("Unknown action code: 0x%02x", motionAction));
    }

    public boolean enableInput(final Engine engine, final InputType inputType) {
        if (inputType == InputType.TOUCH) {

            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {

                    // remapping is bad.. maybe wrap event?.. if will be slow
                    final int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
                    final TouchEvent.Action action = mapAction(actionCode);
                    final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerCount = event.getPointerCount();
                    final TouchEvent touchEvent = new TouchEvent(action, pointerCount, pointerIndex);
                    for (int i=0; i<touchEvent.getPointerCount(); i++) {
                        final float x = event.getX(i);
                        final float y = event.getY(i);
                        final int id = event.getPointerId(i);
                        touchEvent.setPointer(i, id, x, y);
                    }
                    engine.scheduleEvent(touchEvent);
                    return true;
                }
            });
            return true;
        }
        return false;
    }

}
