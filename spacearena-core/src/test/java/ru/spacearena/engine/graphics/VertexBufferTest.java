package ru.spacearena.engine.graphics;

import org.junit.Test;

import java.nio.FloatBuffer;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-02-04
 */
public class VertexBufferTest {

//    @Test
//    public void testArray() throws Exception {
//        final VertexBuffer buf = new VertexBuffer(50);
//        final Object a = buf.getWholeBuffer().array();
//        System.out.println(a);
//    }

    @Test
    public void testLayout() throws Exception {

        final VertexBuffer buf = new VertexBuffer(50);
        buf.reset().layout(2).layout(3).layout(1);

        assertThat(buf.getStride()).isEqualTo(24);
        assertThat(buf.getCount(0)).isEqualTo(2);
        assertThat(buf.getCount(1)).isEqualTo(3);
        assertThat(buf.getCount(2)).isEqualTo(1);
        assertThat(buf.getOffset(0)).isEqualTo(0);
        assertThat(buf.getOffset(1)).isEqualTo(2);
        assertThat(buf.getOffset(2)).isEqualTo(5);

    }

    @Test
    public void testGetBuffer() throws Exception {

        final VertexBuffer buf = new VertexBuffer(50);
        buf.reset().layout(2).layout(3).layout(1);

        buf.put(2,2).put(1,2,3).put(4);

        final FloatBuffer a = buf.getBuffer(2);
        assertThat(a.get()).isEqualTo(4);

        final FloatBuffer b = buf.getBuffer(1);
        assertThat(b.get()).isEqualTo(1);
        assertThat(b.get()).isEqualTo(2);
        assertThat(b.get()).isEqualTo(3);
    }
}
