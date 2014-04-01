package ru.spacearena.engine.math;

import org.junit.Test;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-31-03
 */
public class MatrixTest {

    @Test
    public void testTranslate() throws Exception {
        final Matrix m = new Matrix();
        m.preTranslate(-10, -10);
        assertThat(m.transformPoint(new Point2F(30, 40)).near(new Point2F(20, 30))).isTrue();
    }

    @Test
    public void testScale() throws Exception {
        final Matrix m = new Matrix();
        m.preScale(2f, -2f);
        assertThat(m.transformPoint(new Point2F(30, 40)).near(new Point2F(60, -80))).isTrue();
    }

    @Test
    public void testRotate() throws Exception {
        final Matrix m = new Matrix();
        m.preRotate(FloatMathUtils.toRadians(45f));
        assertThat(m.transformPoint(new Point2F(1, -1)).near(new Point2F(FloatMathUtils.sqrt(2), 0))).isTrue();
    }

    @Test
    public void testComplex() throws Exception {
        final Matrix m = new Matrix();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);
        assertThat(m.transformPoint(new Point2F(11, 9)).near(new Point2F(2 * (10 + FloatMathUtils.sqrt(2)), -10f))).isTrue();
    }

    @Test
    public void testIsIdentity() throws Exception {
        final Matrix m = new Matrix();
        assertThat(m.isIdentity());
    }

    @Test
    public void testInvert() throws Exception {
        final Matrix m = new Matrix();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);
        m.postShear(-5f, 3f);

        final Matrix i = new Matrix(m);
        i.invert();

        i.postMultiply(m);
        assertThat(i.isIdentity());
    }

    @Test
    public void testInvert2() throws Exception {

        final Matrix m1 = new Matrix();
        final Matrix m2 = new Matrix();
        for (int i=0; i<100;i++) {

            final float sx = TempUtils.RAND.nextFloatBetween(-10f, 10f),
                        sy = TempUtils.RAND.nextFloatBetween(-10f, 10f);
            final float tx = TempUtils.RAND.nextFloatBetween(-10f, 10f),
                        ty = TempUtils.RAND.nextFloatBetween(-10f, 10f);
//            final float nx = TempUtils.RAND.nextFloat(), ny = TempUtils.RAND.nextFloat();
//            final float kx = TempUtils.RAND.nextFloatBetween(-10f, 10f),
//                        ky = TempUtils.RAND.nextFloatBetween(-10f, 10f);

            m1.identity();
            m1.postScale(sx, sy);
            m1.postTranslate(tx, ty);

            m2.identity();
            m2.postTranslate(-tx, -ty);
            m2.postScale(1/sx, 1/sy);

            m2.invert();
            assertThat(m2).isEqualTo(m1);
        }

    }

    @Test
    public void testInvertTransform() throws Exception {
        final Matrix m = new Matrix();
        m.preTranslate(-10, -10);
        m.preRotate(FloatMathUtils.toRadians(45));
        m.preTranslate(10, 10);
        m.preScale(2f, -1f);
        m.invert();
        assertThat(m.invertTransformPoint(new Point2F(11, 9)).near(new Point2F(2 * (10 + FloatMathUtils.sqrt(2)), -10f))).isTrue();
    }

    @Test
    public void testTransformVector() throws Exception {

        final Matrix m = new Matrix();
        m.preRotate(FloatMathUtils.HALF_PI);
        m.preTranslate(100, 100);

        assertThat(m.transformVector(new Point2F(1,0)).near(new Point2F(0,1))).isTrue();

        assertThat(m.invertTransformVector(new Point2F(1,0)).near(new Point2F(0,-1))).isTrue();

    }

    @Test
    public void testTransformPoints() throws Exception {

        final float[] buf1 = new float[100];
        for (int i=0; i<20; i++) {
            buf1[i*2+50] = i;
            buf1[i*2+51] = i;
        }

        final float[] buf2 = new float[100];
        final Matrix m = new Matrix();
        m.postScale(2f, 3f);

        m.transformPoints(buf1, 50, buf2, 20, 20);
        for (int i=0; i<20; i++) {

            final float srcX = buf1[i*2+50];
            final float srcY = buf1[i*2+51];
            final float dstX = buf2[i*2+20];
            final float dstY = buf2[i*2+21];

            assertThat(dstX).isEqualTo(srcX*2);
            assertThat(dstY).isEqualTo(srcY*3);
        }


    }
}
