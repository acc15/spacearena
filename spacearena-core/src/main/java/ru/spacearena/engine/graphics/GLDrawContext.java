package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.geometry.shapes.Rect2ID;
import ru.spacearena.engine.graphics.fbo.FrameBufferObject;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.util.IntMathUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-04
 */
public class GLDrawContext {

    protected final OpenGL gl;
    protected final VertexBuffer sharedBuffer = new VertexBuffer();

    private final HashMap<GLObjectDefinition<?>, Object> objects = new HashMap<GLObjectDefinition<?>, Object>();
    private final Binder binder = new Binder();

    public GLDrawContext(OpenGL gl) {
        this.gl = gl;
    }

    public OpenGL getGL() {
        return gl;
    }

    public void init() {
    }

    public void dispose() {
        objects.clear();
    }

    @SuppressWarnings("unchecked")
    private <T> T getObject(GLObjectDefinition<T> definition) {
        return (T) objects.get(definition);
    }

    /**
     * Checks whether object is loaded in current context or not
     * @param definition object definition
     * @return <code>true</code> if object is loaded, <code>false</code> otherwise
     */
    public boolean has(GLObjectDefinition<?> definition) {
        return objects.containsKey(definition);
    }

    /**
     * Loads object by definition <b>only</b> if object doesn't exists yet.
     * @param definition object definition
     */
    public void load(GLObjectDefinition<?> definition) {
        if (!objects.containsKey(definition)) {
            objects.put(definition, definition.create(this));
        }
    }

    /**
     * Returns object by definition. If object doesn't exists then exception will be thrown.
     * @param definition object definition
     * @param <T> object type
     * @return object as specified by <code>definition</code>
     */
    public <T> T get(GLObjectDefinition<T> definition) {
        T obj = getObject(definition);
        if (obj == null) {
            throw new IllegalArgumentException("Object with definition: " + definition + " doesn't exists");
        }
        definition.reference(this, obj);
        return obj;
    }

    /**
     * Returns object by definition. If object doesn't exists it will be created and returned
     * @param definition object definition
     * @param <T> object type
     * @return object as specified by <code>definition</code>
     */
    public <T> T obtain(GLObjectDefinition<T> definition) {
        T obj = getObject(definition);
        if (obj == null) {
            obj = definition.create(this);
            objects.put(definition, obj);
        }
        definition.reference(this, obj);
        return obj;
    }

    public <T> void delete(GLObjectDefinition<T> definition) {
        final T obj = getObject(definition);
        if (obj == null) {
            throw new IllegalArgumentException("Can't find object by definition: " + definition);
        }
        definition.delete(this, obj);
        objects.remove(definition);
    }

    public Binder use(ShaderProgram.Definition shader) {
        return binder.use(shader);
    }

    public void drawTo(FrameBufferObject.Definition fbo) {
        gl.glBindFramebuffer(fbo != null ? obtain(fbo).getId() : 0);
    }

    public VertexBufferObject upload(VertexBufferObject.Definition definition, VertexBuffer buffer) {
        final VertexBufferObject vbo = obtain(definition);
        vbo.upload(gl, definition, buffer);
        return vbo;
    }

    public VertexBuffer getSharedBuffer() {
        return sharedBuffer;
    }

    public static final int[] VIEWPORT_BUF = new int[4];

    public Rect2ID getViewport(Rect2ID rect) {
        gl.glGetIntegerv(OpenGL.GL_VIEWPORT, VIEWPORT_BUF, 0);
        rect.setRect(VIEWPORT_BUF[0], VIEWPORT_BUF[1], VIEWPORT_BUF[2], VIEWPORT_BUF[3]);
        return rect;
    }

    public void setViewport(int x, int y, int width, int height) {
        gl.glViewport(x,y,width,height);
    }

    public void setViewport(Rect2ID rect) {
        setViewport(rect.x, rect.y, rect.w, rect.h);
    }

    public class Binder {

        private ShaderProgram program;
        private int attrCount = -1;
        private int vertexCount = -1;
        private int attrIndex = -1;
        private int uniformIndex = -1;
        private int unitIndex = -1;
        private boolean texturing = false;

        private int nextUniformLocation() {
            return program.getUniformLocation(++uniformIndex);
        }

        private int nextAttrIndex() {
            return ++attrIndex;
        }

        private int nextUnitIndex() { return ++unitIndex; }

        public Binder uniform(float x) {
            gl.glUniform1f(nextUniformLocation(), x);
            return this;
        }

        public Binder uniform(float x, float y) {
            gl.glUniform2f(nextUniformLocation(), x, y);
            return this;
        }

        public Binder uniform(float x, float y, float z) {
            gl.glUniform3f(nextUniformLocation(), x, y, z);
            return this;
        }

        public Binder uniform(float x, float y, float z, float w) {
            gl.glUniform4f(nextUniformLocation(), x, y, z, w);
            return this;
        }

        public Binder uniform(Texture.Definition def) {
            final Texture t = obtain(def);
            if (!texturing) {
                texturing = true;
                gl.glEnable(OpenGL.GL_TEXTURE_2D);
            }

            final int unit = nextUnitIndex();
            gl.glActiveTexture(OpenGL.GL_TEXTURE0 + unit);
            gl.glBindTexture(OpenGL.GL_TEXTURE_2D, t.getId());
            gl.glUniform1i(nextUniformLocation(), unit);
            return this;
        }

        public Binder uniform(Point2F point) {
            gl.glUniform2f(nextUniformLocation(), point.x, point.y);
            return this;
        }

        public Binder uniform(Matrix matrix) {
            gl.glUniformMatrix4fv(nextUniformLocation(), 1, matrix.m, 0);
            return this;
        }

        public Binder uniform(Color color, boolean useAlpha) {
            if (useAlpha) {
                return uniform(color);
            }
            gl.glUniform3f(nextUniformLocation(), color.r, color.g, color.b);
            return this;
        }

        public Binder uniform(Color color) {
            gl.glUniform4f(nextUniformLocation(), color.r, color.g, color.b, color.a);
            return this;
        }

        private void attrPointerBuffer(int attrIndex, int item, VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            gl.glVertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vb.prepareBuffer(item));
            gl.glEnableVertexAttribArray(attrIndex);
        }

        private void attrPointerOffset(int attrIndex, int item, VertexBufferLayout vbl) {
            gl.glVertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vbl.getOffset(item));
            gl.glEnableVertexAttribArray(attrIndex);
        }

        public Binder attr(float x) {
            gl.glVertexAttrib1f(nextAttrIndex(), x);
            return this;
        }

        public Binder attr(float x, float y) {
            gl.glVertexAttrib2f(nextAttrIndex(), x, y);
            return this;
        }

        public Binder attr(float x, float y, float z) {
            gl.glVertexAttrib3f(nextAttrIndex(), x, y, z);
            return this;
        }

        public Binder attr(float x, float y, float z, float w) {
            gl.glVertexAttrib4f(nextAttrIndex(), x, y, z, w);
            return this;
        }

        public Binder attr(float[] v, int offset, int count) {
            switch (count) {
                case 1:
                    gl.glVertexAttrib1fv(nextAttrIndex(), v, offset);
                    break;
                case 2:
                    gl.glVertexAttrib2fv(nextAttrIndex(), v, offset);
                    break;
                case 3:
                    gl.glVertexAttrib3fv(nextAttrIndex(), v, offset);
                    break;
                case 4:
                    gl.glVertexAttrib4fv(nextAttrIndex(), v, offset);
                    break;
                default:
                    throw new IllegalArgumentException("Count must have values from 1 to 4");
            }
            return this;
        }

        public Binder attr(FloatBuffer fb, int count) {
            switch (count) {
                case 1:
                    gl.glVertexAttrib1fv(nextAttrIndex(), fb);
                    break;
                case 2:
                    gl.glVertexAttrib2fv(nextAttrIndex(), fb);
                    break;
                case 3:
                    gl.glVertexAttrib3fv(nextAttrIndex(), fb);
                    break;
                case 4:
                    gl.glVertexAttrib4fv(nextAttrIndex(), fb);
                    break;
                default:
                    throw new IllegalArgumentException("Count must have values from 1 to 4");
            }
            return this;
        }

        public Binder attr(VertexBufferObject.Definition definition, int item) {
            final VertexBufferObject vbo = ensureVBOUploaded(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.glBindBuffer(definition.getBufferType(), vbo.getId());
            attrPointerOffset(nextAttrIndex(), item, vbl);
            gl.glBindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl);
            return this;
        }

        private VertexBufferObject ensureVBOUploaded(VertexBufferObject.Definition def) {
            final VertexBufferObject vbo = obtain(def);
            if (vbo.getSize() < 0) {
                throw new IllegalArgumentException("VBO isn't uploaded. You should upload VertexBufferObject data before use");
            }
            return vbo;
        }

        public Binder attrs(VertexBufferObject.Definition definition) {
            final VertexBufferObject vbo = ensureVBOUploaded(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.glBindBuffer(definition.getBufferType(), vbo.getId());
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerOffset(nextAttrIndex(), i, vbl);
            }
            gl.glBindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl);
            return this;
        }

        public Binder attr(VertexBuffer buffer, int item) {
            final VertexBufferLayout vbl = buffer.getLayout();
            attrPointerBuffer(nextAttrIndex(), item, buffer);
            adjustVertexCount(buffer.getSize(), vbl);
            return this;
        }

        public Binder attrs(VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerBuffer(nextAttrIndex(), i, vb);
            }
            adjustVertexCount(vb.getSize(), vbl);
            return this;
        }

        public void draw(int type) {
            draw(type, vertexCount);
        }

        public void draw(int type, int count) {
            draw(type, 0, count);
        }

        public void draw(int type, int start, int count) {
            gl.glDrawArrays(type, start, count);
            reset();
        }

        private void reset() {
            if (texturing) {
                gl.glDisable(OpenGL.GL_TEXTURE_2D);
                texturing = false;
            }
            for (int i=0; i<attrCount; i++) {
                gl.glDisableVertexAttribArray(i);
            }
            this.vertexCount = -1;
            this.attrIndex = -1;
            this.uniformIndex = -1;
            this.unitIndex = -1;
            this.attrCount = -1;
            this.program = null;
        }

        private void adjustVertexCount(int bufferSize, VertexBufferLayout vbl) {
            final int count = vbl.computeVertexCount(bufferSize);
            vertexCount = (vertexCount < 0 ? count : IntMathUtils.min(vertexCount, count));
        }

        private Binder use(ShaderProgram.Definition definition) {
            this.attrCount = definition.getAttributeCount();
            final ShaderProgram p = obtain(definition);
            if (this.program == p) {
                return binder;
            }
            gl.glUseProgram(p.getId());
            this.program = p;
            return binder;
        }
    }
}
