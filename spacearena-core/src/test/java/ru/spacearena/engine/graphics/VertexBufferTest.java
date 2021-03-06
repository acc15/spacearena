package ru.spacearena.engine.graphics;

import org.junit.Test;
import ru.spacearena.engine.graphics.shaders.DefaultShaders;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

import java.nio.ByteBuffer;

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
    public void testGetBuffer() throws Exception {

        final VertexBuffer buf = new VertexBuffer(50);
        buf.reset(new VertexBufferLayout.Builder().floats(2).floats(3).floats(1).build());

        buf.put(2,2).put(1,2,3).put(4);

        final ByteBuffer a = buf.prepareBuffer(2);
        assertThat(a.getFloat()).isEqualTo(4);

        final ByteBuffer b = buf.prepareBuffer(1);
        assertThat(b.getFloat()).isEqualTo(1);
        assertThat(b.getFloat()).isEqualTo(2);
        assertThat(b.getFloat()).isEqualTo(3);
    }


    @Test
    public void testLimit() throws Exception {

        final VertexBuffer vb = new VertexBuffer();
        vb.reset(new VertexBufferLayout.Builder().floats(2).floats(3).build());

    }

    @Test
    public void testBufferMustGrow() throws Exception {

        final VertexBuffer vb = new VertexBuffer(4);
        vb.reset(DefaultShaders.LAYOUT_P2);

        vb.put(1);
        vb.put(1,2,3,4);
        final ByteBuffer bb = vb.prepareBuffer();
        assertThat(bb.getFloat()).isEqualTo(1);
        assertThat(bb.getFloat()).isEqualTo(1);
        assertThat(bb.getFloat()).isEqualTo(2);
        assertThat(bb.getFloat()).isEqualTo(3);
        assertThat(bb.getFloat()).isEqualTo(4);

    }
}
