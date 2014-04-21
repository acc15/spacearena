package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.GLDrawContext;
import ru.spacearena.engine.graphics.GLObjectDefinition;
import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBufferObject {

    public static class Definition implements GLObjectDefinition<VertexBufferObject> {
        private final int bufferType;
        private final int bufferUsage;

        public int getBufferType() {
            return bufferType;
        }

        public int getBufferUsage() {
            return bufferUsage;
        }

        public Definition(int bufferType) {
            this(bufferType, OpenGL.GL_STATIC_DRAW);
        }

        public Definition(int bufferType, int bufferUsage) {
            this.bufferType = bufferType;
            this.bufferUsage = bufferUsage;
        }

        public VertexBufferObject create(GLDrawContext context) {
            final VertexBufferObject vbo = new VertexBufferObject(context.getGL().glGenBuffer());
            init(context, vbo);
            return vbo;
        }

        public void reference(GLDrawContext context, VertexBufferObject vbo) {
        }

        public void delete(GLDrawContext context, VertexBufferObject vbo) {
            context.getGL().glDeleteBuffer(vbo.id);
        }

        public void init(GLDrawContext context, VertexBufferObject vbo) {
        }
    }

    private final int id;
    private VertexBufferLayout layout = null;
    private int size = -1;

    public VertexBufferObject(int id) {
        this.id = id;
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public int getSize() {
        return size;
    }

    public void upload(OpenGL gl, VertexBufferObject.Definition definition, VertexBuffer buffer) {
        this.layout = buffer.getLayout();
        this.size = buffer.getSize();
        gl.glBindBuffer(definition.getBufferType(), id);
        gl.glBufferData(definition.getBufferType(), size, buffer.prepareBuffer(), definition.getBufferUsage());
        gl.glBindBuffer(definition.getBufferType(), 0);
    }

    public int getId() {
        return id;
    }
}
