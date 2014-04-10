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

        final VertexBufferLayout buf = new VertexBufferLayout.Builder().uints(2).shorts(3).floats(1).build();
        assertThat(buf.getStride()).isEqualTo(18);

        assertThat(buf.getSize(0)).isEqualTo(8);
        assertThat(buf.getSize(1)).isEqualTo(6);
        assertThat(buf.getSize(2)).isEqualTo(4);

        assertThat(buf.getCount(0)).isEqualTo(2);
        assertThat(buf.getCount(1)).isEqualTo(3);
        assertThat(buf.getCount(2)).isEqualTo(1);

        assertThat(buf.getType(0)).isEqualTo(OpenGL.UNSIGNED_INT);
        assertThat(buf.getType(1)).isEqualTo(OpenGL.SHORT);
        assertThat(buf.getType(2)).isEqualTo(OpenGL.FLOAT);

        assertThat(buf.getOffset(0)).isEqualTo(0);
        assertThat(buf.getOffset(1)).isEqualTo(8);
        assertThat(buf.getOffset(2)).isEqualTo(14);

    }

}
