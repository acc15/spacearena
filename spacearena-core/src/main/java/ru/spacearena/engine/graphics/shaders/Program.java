package ru.spacearena.engine.graphics.shaders;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class Program {

    public interface Definition {
        Program createProgram();
    }

    private final List<Shader> shaders = new ArrayList<Shader>();
    private final List<String> attributes = new ArrayList<String>();
    private final List<String> uniforms = new ArrayList<String>();

    private final IntArrayList uniformLocations = new IntArrayList();

    private int id = 0;

    protected void shader(String resourceName) {
        final int type;
        if (resourceName.endsWith(".vert")) {
            type = OpenGL.VERTEX_SHADER;
        } else if (resourceName.endsWith(".frag")) {
            type = OpenGL.FRAGMENT_SHADER;
        } else {
            throw new IllegalArgumentException("Unknown shader resource (use '.vert' " +
                    "for vertex shaders and '.frag' for fragment): " + resourceName);
        }

        final InputStream i = getClass().getResourceAsStream(resourceName);
        if (i == null) {
            throw new IllegalArgumentException("Can't find shader resource: " + resourceName);
        }
        try {
            shader(type, IOUtils.readStream(i));
        } catch (IOException e) {
            throw new RuntimeException("Can't read shader source from resource " + getClass() + "." + resourceName, e);
        } finally {
            IOUtils.closeQuietly(i);
        }
    }

    protected void shader(int type, String source) {
        this.shaders.add(new Shader(type, source));
    }

    protected void attribute(String name) {
        this.attributes.add(name);
    }

    protected void uniform(String name) {
        this.uniforms.add(name);
    }

    public int getUniformLocation(int uniformIndex) {
        return uniformLocations.get(uniformIndex);
    }

    public boolean isLinked() {
        return id != 0;
    }

    public void make(OpenGL gl) {
        if (id == 0) {
            id = doMake(gl);
        }
    }

    public void delete(OpenGL gl) {
        if (id == 0) {
            throw new IllegalStateException("Attempt to delete shader program which isn't present in GL");
        }
        gl.deleteProgram(id);
        for (Shader shader: shaders) {
            shader.delete(gl);
        }
    }

    private void doCleanup(OpenGL gl, int id, int index) {
        while (index > 0) {
            --index;
            final Shader shader = shaders.get(index);
            shader.delete(gl);
        }
        gl.deleteProgram(id);
    }

    private int doMake(OpenGL gl) {
        final int programId = gl.createProgram();

        int shaderIndex = 0;
        try {
            while (shaderIndex < shaders.size()) {
                final Shader shader = shaders.get(shaderIndex);
                shader.compile(gl);
                gl.attachShader(programId, shader.getId());
                ++shaderIndex;
            }
        } catch (RuntimeException e) {
            doCleanup(gl, programId, shaderIndex);
            throw e;
        }

        // explicit binding is optional
        final int l = attributes.size();
        for (int i=0; i<l; i++) {
            final String attributeName = attributes.get(i);
            gl.bindAttribLocation(programId, i, attributeName);
        }
        gl.linkProgram(programId);
        if (gl.getProgram(programId, OpenGL.LINK_STATUS) == 0) {
            final String log = gl.getProgramInfoLog(programId);
            doCleanup(gl, programId, shaders.size());
            throw new RuntimeException("Can't link program: " + log);
        }
        gl.validateProgram(programId);
        if (gl.getProgram(programId, OpenGL.VALIDATE_STATUS) == 0) {
            final String log = gl.getProgramInfoLog(programId);
            doCleanup(gl, programId, shaders.size());
            throw new RuntimeException("Can't validate program: " + log);
        }
        for (String uniform: uniforms) {
            final int uniformLoc = gl.getUniformLocation(programId, uniform);
            uniformLocations.add(uniformLoc);
        }
        return programId;
    }

    public void reset() {
        this.id = 0;
        for (Shader shader: shaders) {
            shader.reset();
        }
    }

    public int getId() {
        return id;
    }
}
