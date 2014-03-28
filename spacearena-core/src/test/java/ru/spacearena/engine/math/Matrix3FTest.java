package ru.spacearena.engine.math;

import org.junit.Test;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.util.FloatMathUtils;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-15-03
 */
public class Matrix3FTest {

    @Test
    public void testTranslate() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preTranslate(-10, -10);
        assertThat(m.transform(new Point2F(30, 40))).isEqualTo(new Point2F(20, 30));
    }

    @Test
    public void testScale() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preScale(2f, -2f);
        assertThat(m.transform(new Point2F(30, 40))).isEqualTo(new Point2F(60, -80));
    }

    @Test
    public void testRotate() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preRotate(FloatMathUtils.toRadians(45f));
        assertThat(m.transform(new Point2F(1, -1))).isEqualTo(new Point2F(FloatMathUtils.sqrt(2), 0));
    }

    @Test
    public void testComplex() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);
        assertThat(m.transform(new Point2F(11, 9))).isEqualTo(new Point2F(2 * (10 + FloatMathUtils.sqrt(2)), -10f));
    }

    @Test
    public void testIsIdentity() throws Exception {
        final Matrix3F m = new Matrix3F();
        assertThat(m.isIdentity());
    }

    @Test
    public void testInvert() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);

        final Matrix3F i = new Matrix3F(m);
        i.invert();

        i.postMultiply(m);
        assertThat(i.isIdentity());
    }

    @Test
    public void testInvertTransform() throws Exception {
        final Matrix3F m = new Matrix3F();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);
        m.invert();
        assertThat(m.invertTransform(new Point2F(11, 9))).isEqualTo(new Point2F(2 * (10 + FloatMathUtils.sqrt(2)), -10f));
    }
}
