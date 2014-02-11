package ru.spacearena.android;

import android.graphics.RectF;
import android.opengl.GLES20;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import ru.spacearena.util.RandomUtils;

import java.nio.FloatBuffer;
import java.util.Random;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class Sky extends Entity {

    private static final float MAX_SIZE = 6f;
    private static final float MIN_SIZE = 2f;

    /*
    private static class FBuf {

        private int offset = 0;
        private float[] data;

        private FBuf(float[] data) {
            this.data = data;
        }

        public FBuf put(float v) {
            data[offset++] = v;
            return this;
        }

    }
    */

    private final HighPerformanceVertexBufferObject vbo;
    private final ShaderProgram shaderProgram = PositionColorShaderProgram.getInstance();
    private final Random random = new Random();
    private final long seed = random.nextLong();
    private final RectF viewRect = new RectF();

    private static final int PIXELS_PER_STAR = 300;
    private int totalStarCount = 0;

    public Sky(VertexBufferObjectManager vboManager) {
        this.vbo = new HighPerformanceVertexBufferObject(vboManager, 102400, DrawType.DYNAMIC, true, Rectangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
    }

    private static void assignViewRect(Camera camera, RectF rect) {
        rect.left = camera.getXMin();
        rect.right = camera.getXMax();
        rect.top = camera.getYMin();
        rect.bottom = camera.getYMax();
    }

    private void fillData(Camera cam) {
        final float[] buf = vbo.getBufferData();
        final FloatBuffer buffer = FloatBuffer.wrap(buf);
        for (int scale=5; scale>=0; scale--) {
            assignViewRect(cam, viewRect);
            drawStarLayer(scale, buffer);
        }
        this.totalStarCount = buffer.position() / 3;
        assignViewRect(cam, viewRect);
    }

    static float firstVisiblePosition(float i, float grid) {
        return (float)Math.ceil(i/grid)*grid;
    }

    private static void putVertex(FloatBuffer buf, float x, float y, float color) {
        buf.put(x).put(y).put(color);
    }

    private void drawStarLayer(float scale, FloatBuffer buf) {

        final float pixelsPerStar = PIXELS_PER_STAR * scale;
        final float twoStarDistance = pixelsPerStar * 2;

        // inflating rect
        viewRect.inset(
                viewRect.width()-(viewRect.width()*scale + twoStarDistance),
                viewRect.height()-(viewRect.height()*scale + twoStarDistance));

        final float startX = firstVisiblePosition(viewRect.left, pixelsPerStar);
        final float startY = firstVisiblePosition(viewRect.top, pixelsPerStar);

        for (float y=startY; y<=viewRect.bottom; y += pixelsPerStar) {
            for (float x=startX; x<=viewRect.right; x += pixelsPerStar) {
                random.setSeed(seed ^ ((long)scale<<48) ^ ((long)x<<24) ^ ((long)y));
                final float randX = x + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float randY = y + RandomUtils.randomBetween(random, -twoStarDistance, twoStarDistance);
                final float halfSize = (MIN_SIZE + random.nextFloat() * (MAX_SIZE-MIN_SIZE))/2;

                final float translateX = (randX - viewRect.centerX()) / scale + viewRect.centerX();
                final float translateY = (randY - viewRect.centerY()) / scale + viewRect.centerY();

                final float bright = random.nextFloat();
                final float color = new Color(bright, bright, 1f).getABGRPackedFloat();
                putVertex(buf, translateX, translateY-halfSize, color);
                putVertex(buf, translateX-halfSize, translateY+halfSize, color);
                putVertex(buf, translateX+halfSize, translateY+halfSize, color);
            }
        }
    }

    @Override
    protected void preDraw(GLState pGLState, Camera pCamera) {
        super.preDraw(pGLState, pCamera);
        if (pCamera.getXMin() != viewRect.left ||
            pCamera.getXMax() != viewRect.right ||
            pCamera.getYMin() != viewRect.top ||
            pCamera.getYMax() != viewRect.bottom) {
            fillData(pCamera);
            vbo.setDirtyOnHardware();
        }
        vbo.bind(pGLState, shaderProgram);
    }

    @Override
    protected void draw(GLState pGLState, Camera pCamera) {
        vbo.draw(GLES20.GL_TRIANGLES, totalStarCount);
    }

    @Override
    protected void postDraw(GLState pGLState, Camera pCamera) {
        vbo.unbind(pGLState, shaderProgram);
        super.postDraw(pGLState, pCamera);
    }

    @Override
    public void dispose() {
        super.dispose();
        if(vbo.isAutoDispose() && !vbo.isDisposed()) {
            vbo.dispose();
        }
    }
}
