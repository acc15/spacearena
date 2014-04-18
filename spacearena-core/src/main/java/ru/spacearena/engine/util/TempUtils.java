package ru.spacearena.engine.util;

import ru.spacearena.engine.geometry.primitives.Line2F;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.primitives.Projection2F;
import ru.spacearena.engine.geometry.primitives.Projection2F;
import ru.spacearena.engine.random.QRand;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class TempUtils {

    public static final QRand RAND = new QRand();
    public static final float[] POINT_BUF = new float[200];
    public static final Line2F EDGE_1 = new Line2F();
    public static final Point2F POINT_1 = new Point2F();
    public static final Projection2F PROJECTION_1 = new Projection2F();
    public static final Projection2F PROJECTION_2 = new Projection2F();

    public static float[] tempBuf(float x, float y) {
        POINT_BUF[0] = x;
        POINT_BUF[1] = y;
        return POINT_BUF;
    }

    public static Point2F tempPoint(Point2F pt) {
        return POINT_1.set(pt);
    }

    public static Point2F tempPoint(float x, float y) {
        return POINT_1.set(x, y);
    }

}
