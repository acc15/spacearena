package ru.spacearena.engine.events.trackers;

import org.junit.Test;
import ru.spacearena.engine.events.TouchEvent;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-03
 */
public class InputTrackerTest {

    private TouchEvent event(TouchEvent.Action action, int id, float[] data, int[] ids) {
        final TouchEvent e = new TouchEvent(action, data.length/2, id);
        for (int i=0; i<ids.length; i++) {
            e.setPointer(i, ids[i], data[i*2], data[i*2+1]);
        }
        return e;
    }

    private void assertTracker(InputTracker it, float[] expectedData) {
        final int pointCount = expectedData.length >> 1;
        assertThat(it.getPointerCount()).as("Pointer count different").isEqualTo(pointCount);
        for (int i=0; i< pointCount;i++) {
            final float ex = expectedData[i<<1], ey = expectedData[(i<<1)+1];
            assertThat(it.getPointerX(i)).as("Pointer[" + i + "].X not correct").isEqualTo(ex);
            assertThat(it.getPointerY(i)).as("Pointer[" + i + "].Y not correct").isEqualTo(ey);
        }
    }

    @Test
    public void testOnInput() throws Exception {

        final InputTracker it = new InputTracker();
        it.onInput(event(TouchEvent.Action.DOWN, 0, new float[] {5, 5}, new int[] {0}));
        assertTracker(it, new float[] {5,5});

        it.onInput(event(TouchEvent.Action.DOWN, 1, new float[] {5,5, 10,10}, new int[] {0,1}));
        assertTracker(it, new float[] {5,5, 10,10});

        it.onInput(event(TouchEvent.Action.UP, 0, new float[] {5,5, 10,10}, new int[] {0,1}));
        assertTracker(it, new float[] {10,10});

        it.onInput(event(TouchEvent.Action.MOVE, 1, new float[] {11,11}, new int[] {1}));
        assertTracker(it, new float[] {11,11});

        it.onInput(event(TouchEvent.Action.DOWN, 0, new float[] {5,5, 10,10}, new int[] {0,1}));
        assertTracker(it, new float[] {10,10, 5,5});

        it.onInput(event(TouchEvent.Action.UP, 0, new float[] {5,5, 10,10}, new int[] {0,1}));
        assertTracker(it, new float[] {10,10});

    }
}
