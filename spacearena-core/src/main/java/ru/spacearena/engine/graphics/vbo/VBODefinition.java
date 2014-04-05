package ru.spacearena.engine.graphics.vbo;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VBODefinition implements VertexBufferObject.Definition {

    private int usage;
    private int type;

    public VBODefinition(int type, int usage) {
        this.usage = usage;
        this.type = type;
    }

    public int getBufferType() {
        return type;
    }

    public int getBufferUsage() {
        return usage;
    }
}
