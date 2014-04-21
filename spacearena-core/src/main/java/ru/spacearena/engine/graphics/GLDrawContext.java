package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.vbo.VertexBuffer;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;
import ru.spacearena.engine.graphics.vbo.VertexBufferObject;
import ru.spacearena.engine.util.IntMathUtils;

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

    public Binder use(ShaderProgram.Definition definition) {
        return binder.use(obtain(definition));
    }

    public VertexBufferObject upload(VertexBufferObject.Definition definition, VertexBuffer buffer) {
        final VertexBufferObject vbo = obtain(definition);
        vbo.upload(gl, definition, buffer);
        return vbo;
    }

    public VertexBuffer getSharedBuffer() {
        return sharedBuffer;
    }

    public class Binder {

        private ShaderProgram program;
        private int vertexCount = -1;
        private int attrIndex = -1;
        private int uniformIndex = -1;
        private boolean texturing = false;

        private int nextUniformLocation() {
            return program.getUniformLocation(++uniformIndex);
        }

        private int nextAttrIndex() {
            return ++attrIndex;
        }

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

        public Binder uniform(Texture.Definition def, int unit) {
            final Texture t = obtain(def);
            if (!texturing) {
                texturing = true;
                gl.glEnable(OpenGL.GL_TEXTURE_2D);
            }
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

        public Binder attr(VertexBufferObject.Definition definition, int item) {
            final VertexBufferObject vbo = obtain(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.glBindBuffer(definition.getBufferType(), vbo.getId());
            attrPointerOffset(nextAttrIndex(), item, vbl);
            gl.glBindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
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
            final VertexBufferObject vbo = obtain(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.glBindBuffer(definition.getBufferType(), vbo.getId());
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerOffset(nextAttrIndex(), i, vbl);
            }
            gl.glBindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
            return this;
        }

        public Binder attr(VertexBuffer buffer, int item) {
            final VertexBufferLayout vbl = buffer.getLayout();
            attrPointerBuffer(nextAttrIndex(), item, buffer);
            adjustVertexCount(buffer.getSize(), vbl.getStride());
            return this;
        }

        public Binder attrs(VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerBuffer(nextAttrIndex(), i, vb);
            }
            adjustVertexCount(vb.getSize(), vbl.getStride());
            return this;
        }

        public void draw(int type) {
            draw(type, 0, vertexCount);
        }

        public void draw(int type, int count) {
            draw(type, 0, count);
        }

        public void draw(int type, int start, int count) {
            gl.glDrawArrays(type, start, count);
            if (texturing) {
                gl.glDisable(OpenGL.GL_TEXTURE_2D);
                texturing = false;
            }
        }

        private void adjustVertexCount(int bufferSize, int stride) {
            final int count = bufferSize / stride;
            vertexCount = (vertexCount < 0 ? count : IntMathUtils.min(vertexCount, count));
        }

        public Binder use(ShaderProgram program) {
            this.vertexCount = -1;
            this.attrIndex = -1;
            this.uniformIndex = -1;
            if (this.program == program) {
                return binder;
            }
            gl.glUseProgram(program.getId());
            this.program = program;
            return binder;
        }
    }
}
