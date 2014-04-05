package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-03
 */
public class DrawUtils {

    public enum HeadType {
        NONE,
        ARROW,
        CIRCLE
    }

    private static final float SIN_30 = 0.5f;
    private static final float COS_30 = 0.86602540378f;

    public static int getByteCount(int type, int amount) {
        switch (type) {
        case OpenGL.BYTE:
        case OpenGL.UNSIGNED_BYTE:
            return amount;

        case OpenGL.SHORT:
        case OpenGL.UNSIGNED_SHORT:
            return amount << 1;

        case OpenGL.FIXED:
        case OpenGL.FLOAT:
        case OpenGL.INT:
        case OpenGL.UNSIGNED_INT:
            return amount << 2;

        default:
            throw new UnsupportedOperationException("Unknown type: " + type);
        }
    }

    public static int getTypeCount(int type, int bytes) {
        switch (type) {
        case OpenGL.BYTE:
        case OpenGL.UNSIGNED_BYTE:
            return bytes;

        case OpenGL.SHORT:
        case OpenGL.UNSIGNED_SHORT:
            return bytes >> 1;

        case OpenGL.FIXED:
        case OpenGL.FLOAT:
        case OpenGL.INT:
        case OpenGL.UNSIGNED_INT:
            return bytes >> 2;

        default:
            throw new UnsupportedOperationException("Unknown type: " + type);
        }
    }

    /*
    public static void drawArrow(DrawContext context, float x1, float y1, float x2, float y2,
                                 HeadType head, float headSize,
                                 HeadType tail, float tailSize) {

        if (FloatMathUtils.isEqual(x1, y1, x2, y2)) {
            return;
        }

        context.drawLine(x1, y1, x2, y2);

        float p30x = 0f, p30y = 0f, s30x = 0f, s30y = 0f;
        if (head == HeadType.ARROW || tail == HeadType.ARROW) {
            final float vx = x2 - x1, vy = y2 - y1;
            final float l = FloatMathUtils.length(vx, vy);
            final float nx = vx/l, ny = vy/l;
            p30x = nx * COS_30 - ny * SIN_30;
            p30y = ny * COS_30 + nx * SIN_30;
            s30x = nx * COS_30 + ny * SIN_30;
            s30y = ny * COS_30 - nx * SIN_30;
        }

        switch (head) {
        case CIRCLE:
            context.fillCircle(x1, y1, headSize);
            break;

        case ARROW:
            context.drawLine(x1, y1, x1 + p30x * headSize, y1 + p30y * headSize);
            context.drawLine(x1, y1, x1 + s30x * headSize, y1 + s30y * headSize);
            break;
        }

        switch (tail) {
        case CIRCLE:
            context.fillCircle(x2, y2, headSize);
            break;

        case ARROW:
            context.drawLine(x2, y2, x2 - p30x * tailSize, y2 - p30y * tailSize);
            context.drawLine(x2, y2, x2 - s30x * tailSize, y2 - s30y * tailSize);
            break;
        }


    }
    */
}
