package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VBODefinition implements VertexBufferObject.Definition {

    private OpenGL.BufferUsage usage;
    private OpenGL.BufferType type;

    public VBODefinition(OpenGL.BufferType type, OpenGL.BufferUsage usage) {
        this.usage = usage;
        this.type = type;
    }

    public OpenGL.BufferType getBufferType() {
        return type;
    }

    public OpenGL.BufferUsage getBufferUsage() {
        return usage;
    }
}
