package ru.spacearena.engine.graphics;

import org.junit.Test;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayoutTest {


    @Test
    public void testLayout() throws Exception {

        final VertexBufferLayout buf = new VertexBufferLayout.Builder().floats(2).floats(3).floats(1).build();
        assertThat(buf.getStride()).isEqualTo(24);
        assertThat(buf.getCount(0)).isEqualTo(8);
        assertThat(buf.getCount(1)).isEqualTo(12);
        assertThat(buf.getCount(2)).isEqualTo(4);
        assertThat(buf.getOffset(0)).isEqualTo(0);
        assertThat(buf.getOffset(1)).isEqualTo(8);
        assertThat(buf.getOffset(2)).isEqualTo(20);

    }

}
