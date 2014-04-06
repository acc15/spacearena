package ru.spacearena.android.engine.common;

import org.fest.assertions.data.Offset;
import org.junit.Test;
import ru.spacearena.game.Matrix;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class TransformObjectTest {

    @Test
    public void testMatrix() throws Exception {

        final Matrix matrix = Matrix.identity();

        Matrix m = Matrix.translate(-10, -10).mul(matrix);
        m = Matrix.rotate((float)Math.PI/2).mul(m);
        m = Matrix.scale(10, 10).mul(m);
        m = Matrix.translate(10, 10).mul(m);

        Matrix r = m.mul(new Matrix(1, new float[]{15,15,1}));
        assertThat(r.getValue(0, 0)).isEqualTo(-40f, Offset.offset(0.0001f));
        assertThat(r.getValue(1,0)).isEqualTo(60f, Offset.offset(0.0001f));

    }
}
