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
public class ShaderProgram {

    public interface Definition {
        ShaderProgram createProgram();
    }

    private final List<Shader> shaders = new ArrayList<Shader>();
    private final List<String> attributes = new ArrayList<String>();
    private final List<String> uniforms = new ArrayList<String>();

    private final IntArrayList uniformLocations = new IntArrayList();

    private int id = 0;

    public void shader(Class<?> clazz, String resourceName) {
        final int type;
        if (resourceName.endsWith(".vert")) {
            type = OpenGL.VERTEX_SHADER;
        } else if (resourceName.endsWith(".frag")) {
            type = OpenGL.FRAGMENT_SHADER;
        } else {
            throw new IllegalArgumentException("Unknown shader resource (use '.vert' " +
                    "for vertex shaders and '.frag' for fragment): " + resourceName);
        }

        final InputStream i = clazz.getResourceAsStream(resourceName);
        if (i == null) {
            throw new IllegalArgumentException("Can't find shader resource: " + resourceName);
        }
        try {
            shader(type, IOUtils.readStream(i));
        } catch (IOException e) {
            throw new RuntimeException("Can't read shader source from resource " + clazz + "." + resourceName, e);
        } finally {
            IOUtils.closeQuietly(i);
        }
    }

    public void shader(String resourceName) {
        shader(getClass(), resourceName);
    }

    public void shader(int type, String source) {
        this.shaders.add(new Shader(type, source));
    }

    public void attribute(String name) {
        this.attributes.add(name);
    }

    public void uniform(String name) {
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
        gl.glDeleteProgram(id);
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
        gl.glDeleteProgram(id);
    }

    private int doMake(OpenGL gl) {
        final int programId = gl.glCreateProgram();

        int shaderIndex = 0;
        try {
            while (shaderIndex < shaders.size()) {
                final Shader shader = shaders.get(shaderIndex);
                shader.compile(gl);
                gl.glAttachShader(programId, shader.getId());
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
            gl.glBindAttribLocation(programId, i, attributeName);
        }
        gl.glLinkProgram(programId);
        if (gl.getProgram(programId, OpenGL.LINK_STATUS) == 0) {
            final String log = gl.glGetProgramInfoLog(programId);
            doCleanup(gl, programId, shaders.size());
            throw new RuntimeException("Can't link program: " + log);
        }
        gl.glValidateProgram(programId);
        if (gl.getProgram(programId, OpenGL.VALIDATE_STATUS) == 0) {
            final String log = gl.glGetProgramInfoLog(programId);
            doCleanup(gl, programId, shaders.size());
            throw new RuntimeException("Can't validate program: " + log);
        }
        for (String uniform: uniforms) {
            final int uniformLoc = gl.glGetUniformLocation(programId, uniform);
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
