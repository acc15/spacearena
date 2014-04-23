package ru.spacearena.engine.graphics.shaders;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.GLDrawContext;
import ru.spacearena.engine.graphics.GLObjectDefinition;
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

    public static class Definition implements GLObjectDefinition<ShaderProgram> {

        private final List<Shader.Definition> shaders = new ArrayList<Shader.Definition>();
        private final List<String> attributes = new ArrayList<String>();
        private final List<String> uniforms = new ArrayList<String>();

        public Definition shader(Class<?> clazz, String resourceName) {
            final int type;
            if (resourceName.endsWith(".vert")) {
                type = OpenGL.GL_VERTEX_SHADER;
            } else if (resourceName.endsWith(".frag")) {
                type = OpenGL.GL_FRAGMENT_SHADER;
            } else {
                throw new IllegalArgumentException("Unknown shader resource (use '.vert' " +
                        "for vertex shaders and '.frag' for fragment): " + resourceName);
            }

            final InputStream i = clazz.getResourceAsStream(resourceName);
            if (i == null) {
                throw new IllegalArgumentException("Can't find shader resource: " + resourceName);
            }
            try {
                return shader(type, IOUtils.readStream(i));
            } catch (IOException e) {
                throw new RuntimeException("Can't read shader source from resource " + clazz + "." + resourceName, e);
            } finally {
                IOUtils.closeQuietly(i);
            }
        }

        public Definition shader(int type, String source) {
            return shader(new Shader.Definition(type, source));
        }

        public Definition shader(Shader.Definition shader) {
            this.shaders.add(shader);
            return this;
        }

        public Definition attribute(String name) {
            this.attributes.add(name);
            return this;
        }

        public Definition uniform(String name) {
            this.uniforms.add(name);
            return this;
        }

        private void doCleanup(GLDrawContext context, int id, int index) {
            while (index > 0) {
                --index;
                context.delete(shaders.get(index));
            }
            context.getGL().glDeleteProgram(id);
        }

        public ShaderProgram create(GLDrawContext context) {
            final OpenGL gl = context.getGL();
            final ShaderProgram p = new ShaderProgram(gl.glCreateProgram());

            int shaderIndex = 0;
            try {
                while (shaderIndex < shaders.size()) {
                    final Shader shader = context.obtain(shaders.get(shaderIndex));
                    gl.glAttachShader(p.id, shader.getId());
                    ++shaderIndex;
                }
            } catch (RuntimeException e) {
                doCleanup(context, p.id, shaderIndex);
                throw e;
            }

            // explicit binding is optional
            final int l = attributes.size();
            for (int i=0; i<l; i++) {
                final String attributeName = attributes.get(i);
                gl.glBindAttribLocation(p.id, i, attributeName);
            }
            gl.glLinkProgram(p.id);
            if (gl.getProgram(p.id, OpenGL.GL_LINK_STATUS) == 0) {
                final String log = gl.glGetProgramInfoLog(p.id);
                doCleanup(context, p.id, shaders.size());
                throw new RuntimeException("Can't link program: " + log);
            }
            gl.glValidateProgram(p.id);
            if (gl.getProgram(p.id, OpenGL.GL_VALIDATE_STATUS) == 0) {
                final String log = gl.glGetProgramInfoLog(p.id);
                doCleanup(context, p.id, shaders.size());
                throw new RuntimeException("Can't validate program: " + log);
            }
            for (String uniform: uniforms) {
                final int uniformLoc = gl.glGetUniformLocation(p.id, uniform);
                p.uniformLocations.add(uniformLoc);
            }
            return p;
        }

        public void reference(GLDrawContext context, ShaderProgram object) {
        }

        public void delete(GLDrawContext context, ShaderProgram object) {
            final OpenGL gl = context.getGL();
            gl.glDeleteProgram(object.id);
            for (Shader.Definition shader: shaders) {
                context.delete(shader);
            }
        }

        public int getAttributeCount() {
            return attributes.size();
        }
    }

    private final IntArrayList uniformLocations = new IntArrayList();
    private final int id;

    public ShaderProgram(int id) {
        this.id = id;
    }

    public int getUniformLocation(int uniformIndex) {
        return uniformLocations.get(uniformIndex);
    }

    public int getId() {
        return id;
    }
}
