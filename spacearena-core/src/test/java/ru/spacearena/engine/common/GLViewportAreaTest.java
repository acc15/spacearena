package ru.spacearena.engine.common;

import org.junit.Test;
import ru.spacearena.engine.common.viewport.GLViewportArea;
import ru.spacearena.engine.geometry.shapes.Rect2F;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class GLViewportAreaTest {
    @Test
    public void testGetRect() throws Exception {
        final Rect2F r = GLViewportArea.getInstance().getRect();
        final float h = r.getHeight();
        assertThat(h).isEqualTo(-2);
    }
}
