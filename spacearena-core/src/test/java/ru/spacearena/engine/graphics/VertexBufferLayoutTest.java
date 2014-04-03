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

        final VertexBufferLayout buf = new VertexBufferLayout.Builder().size(2).size(3).size(1).build();
        assertThat(buf.getStride()).isEqualTo(24);
        assertThat(buf.getCount(0)).isEqualTo(2);
        assertThat(buf.getCount(1)).isEqualTo(3);
        assertThat(buf.getCount(2)).isEqualTo(1);
        assertThat(buf.getOffset(0)).isEqualTo(0);
        assertThat(buf.getOffset(1)).isEqualTo(2);
        assertThat(buf.getOffset(2)).isEqualTo(5);

    }

}
