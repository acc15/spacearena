package ru.spacearena.engine.graphics;

import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.FontIO;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;
import ru.spacearena.engine.graphics.texture.Texture;
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
    private final HashMap<ShaderProgram.Definition, ShaderProgram> programs = new HashMap<ShaderProgram.Definition, ShaderProgram>();
    private final HashMap<VertexBufferObject.Definition, VertexBufferObject> vbos = new HashMap<VertexBufferObject.Definition, VertexBufferObject>();
    private final HashMap<Texture.Definition, Texture> textures = new HashMap<Texture.Definition, Texture>();
    private final HashMap<FontData.Definition, FontData> fonts = new HashMap<FontData.Definition, FontData>();
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
        programs.clear();
        vbos.clear();
        textures.clear();
    }

    public Binder use(ShaderProgram.Definition definition) {
        return binder.use(get(definition));
    }

    public boolean has(ShaderProgram.Definition definition) {
        return programs.containsKey(definition);
    }

    public boolean has(VertexBufferObject.Definition definition) {
        return vbos.containsKey(definition);
    }

    public boolean has(Texture.Definition definition) {
        return textures.containsKey(definition);
    }

    public VertexBufferObject upload(VertexBufferObject.Definition definition, VertexBuffer buffer) {
        VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            vbo = new VertexBufferObject();
        }
        vbo.upload(gl, definition, buffer);
        vbos.put(definition, vbo);
        return vbo;
    }

    public void delete(VertexBufferObject.Definition definition) {
        final VertexBufferObject vbo = get(definition);
        vbo.delete(gl);
        vbos.remove(definition);
    }

    public VertexBufferObject get(VertexBufferObject.Definition definition) {
        final VertexBufferObject vbo = vbos.get(definition);
        if (vbo == null) {
            throw new IllegalArgumentException("VBO with definition " + definition + " doesn't exists");
        }
        return vbo;
    }

    public ShaderProgram get(ShaderProgram.Definition def) {
        ShaderProgram p = programs.get(def);
        if (p != null) {
            return p;
        }
        p = def.createProgram();
        programs.put(def, p);
        p.make(gl);
        return p;
    }

    public Texture get(Texture.Definition definition) {
        Texture t = textures.get(definition);
        if (t == null) {
            t = definition.createTexture(gl);
            textures.put(definition, t);
        }
        return t;
    }

    public FontData get(FontData.Definition definition) {
        FontData fontData = fonts.get(definition);
        if (fontData != null) {
            return fontData;
        }
        fontData = FontIO.load(definition.getFontUrl());
        get(definition.getTexture());
        fonts.put(definition, fontData);
        return fontData;
    }

    public void delete(ShaderProgram.Definition definition) {
        final ShaderProgram p = programs.get(definition);
        if (p == null) {
            return;
        }
        p.delete(gl);
        programs.remove(definition);
    }

    public void delete(FontData.Definition definition) {
        fonts.remove(definition);
        delete(definition.getTexture());
    }

    public void delete(Texture.Definition definition) {
        final Texture t = textures.get(definition);
        if (t == null) {
            throw new IllegalArgumentException("Texture with definition " + definition + " doesn't exists");
        }
        gl.deleteTexture(t.getId());
        textures.remove(definition);
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

        public DrawContext2f.Binder uniform(float x) {
            gl.uniform(nextUniformLocation(), x);
            return this;
        }

        public DrawContext2f.Binder uniform(float x, float y) {
            gl.uniform(nextUniformLocation(), x, y);
            return this;
        }

        public DrawContext2f.Binder uniform(float x, float y, float z) {
            gl.uniform(nextUniformLocation(), x, y, z);
            return this;
        }

        public DrawContext2f.Binder uniform(float x, float y, float z, float w) {
            gl.uniform(nextUniformLocation(), x, y, z, w);
            return this;
        }

        public DrawContext2f.Binder uniform(Texture.Definition def, int unit) {
            final Texture t = get(def);
            if (!texturing) {
                texturing = true;
                gl.enable(OpenGL.TEXTURE_2D);
            }
            gl.activeTexture(OpenGL.TEXTURE0 + unit);
            gl.bindTexture(OpenGL.TEXTURE_2D, t.getId());

            gl.uniform(nextUniformLocation(), unit);
            return this;
        }

        public DrawContext2f.Binder uniform(Point2F point) {
            gl.uniform(nextUniformLocation(), point.x, point.y);
            return this;
        }

        public DrawContext2f.Binder uniform(Matrix matrix) {
            gl.uniformMatrix4(nextUniformLocation(), 1, matrix.m, 0);
            return this;
        }

        public DrawContext2f.Binder uniform(Color color, boolean useAlpha) {
            if (useAlpha) {
                return uniform(color);
            }
            gl.uniform(nextUniformLocation(), color.r, color.g, color.b);
            return this;
        }

        public DrawContext2f.Binder uniform(Color color) {
            gl.uniform(nextUniformLocation(), color.r, color.g, color.b, color.a);
            return this;
        }

        private void attrPointerBuffer(int attrIndex, int item, VertexBuffer vb) {
            final VertexBufferLayout vbl = vb.getLayout();
            gl.vertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vb.prepareBuffer(item));
            gl.enableVertexAttribArray(attrIndex);
        }

        private void attrPointerOffset(int attrIndex, int item, VertexBufferLayout vbl) {
            gl.vertexAttribPointer(attrIndex, vbl.getCount(item), vbl.getType(item),
                    false, vbl.getStride(), vbl.getOffset(item));
            gl.enableVertexAttribArray(attrIndex);
        }

        public DrawContext2f.Binder attr(VertexBufferObject.Definition definition, int item) {
            final VertexBufferObject vbo = get(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.bindBuffer(definition.getBufferType(), vbo.getId());
            attrPointerOffset(nextAttrIndex(), item, vbl);
            gl.bindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
            return this;
        }

        public DrawContext2f.Binder attrs(VertexBufferObject.Definition definition) {
            final VertexBufferObject vbo = get(definition);
            final VertexBufferLayout vbl = vbo.getLayout();
            gl.bindBuffer(definition.getBufferType(), vbo.getId());
            for (int i=0; i<vbl.getAttrCount(); i++) {
                attrPointerOffset(nextAttrIndex(), i, vbl);
            }
            gl.bindBuffer(definition.getBufferType(), 0);
            adjustVertexCount(vbo.getSize(), vbl.getStride());
            return this;
        }

        public DrawContext2f.Binder attr(VertexBuffer buffer, int item) {
            final VertexBufferLayout vbl = buffer.getLayout();
            attrPointerBuffer(nextAttrIndex(), item, buffer);
            adjustVertexCount(buffer.getSize(), vbl.getStride());
            return this;
        }

        public DrawContext2f.Binder attrs(VertexBuffer vb) {
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
            gl.drawArrays(type, start, count);
            if (texturing) {
                gl.disable(OpenGL.TEXTURE_2D);
                texturing = false;
            }
        }

        private void adjustVertexCount(int bufferSize, int stride) {
            final int count = bufferSize / stride;
            vertexCount = (vertexCount < 0 ? count : IntMathUtils.min(vertexCount, count));
        }

        public DrawContext2f.Binder use(ShaderProgram program) {
            this.vertexCount = -1;
            this.attrIndex = -1;
            this.uniformIndex = -1;
            if (this.program == program) {
                return binder;
            }
            gl.useProgram(program.getId());
            this.program = program;
            return binder;
        }


    }
}
