package ru.spacearena.engine.graphics;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayoutTest {


    @Test
    public void testLayout() throws Exception {

        final VertexBufferLayout buf = new VertexBufferLayout();
        buf.reset().attrSize(2).attrSize(3).attrSize(1);
        assertThat(buf.getStride()).isEqualTo(24);
        assertThat(buf.getCount(0)).isEqualTo(2);
        assertThat(buf.getCount(1)).isEqualTo(3);
        assertThat(buf.getCount(2)).isEqualTo(1);
        assertThat(buf.getOffset(0)).isEqualTo(0);
        assertThat(buf.getOffset(1)).isEqualTo(2);
        assertThat(buf.getOffset(2)).isEqualTo(5);

    }

}
