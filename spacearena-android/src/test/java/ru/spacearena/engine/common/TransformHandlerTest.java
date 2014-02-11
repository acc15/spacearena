package ru.spacearena.engine.common;

import org.fest.assertions.Delta;
import org.junit.Test;
import ru.spacearena.game.Matrix;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class TransformHandlerTest {

    @Test
    public void testMatrix() throws Exception {

        final Matrix matrix = Matrix.identity();

        Matrix m = Matrix.translate(-10, -10).mul(matrix);
        m = Matrix.rotate((float)Math.PI/2).mul(m);
        m = Matrix.scale(10, 10).mul(m);
        m = Matrix.translate(10, 10).mul(m);

        Matrix r = m.mul(new Matrix(1, new float[]{15,15,1}));
        assertThat(r.getValue(0, 0)).isEqualTo(-40, Delta.delta(0.0001));
        assertThat(r.getValue(1,0)).isEqualTo(60, Delta.delta(0.0001));

    }
}
