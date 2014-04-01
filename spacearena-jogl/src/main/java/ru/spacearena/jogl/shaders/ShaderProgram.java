package ru.spacearena.jogl.shaders;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.math.Matrix2FGL;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class ShaderProgram {


    private final List<Shader> shaders = new ArrayList<Shader>();
    private final List<String> attributes = new ArrayList<String>();
    private final List<String> uniforms = new ArrayList<String>();

    private final IntArrayList uniformLocations = new IntArrayList();

    private int id = 0;

    public void addShader(Shader shader) {
        this.shaders.add(shader);
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }

    public void addUniform(String uniform) {
        this.uniforms.add(uniform);
    }

    public int getUniformLocation(int uniformIndex) {
        return uniformLocations.get(uniformIndex);
    }

    public boolean isLinked() {
        return id != 0;
    }

    public void make(OpenGL gl) {
        if (id != 0) {
            id = doMake(gl);
        }
    }

    public void delete(OpenGL gl) {
        if (id == 0) {
            throw new IllegalStateException("Program not linked but wants to be deleted");
        }
        gl.deleteProgram(id);
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
        if (gl.getProgram(programId, OpenGL.ProgramParam.LINK_STATUS) == 0) {
            final String log = gl.getProgramInfoLog(programId);
            doCleanup(gl, programId, shaders.size());
            throw new RuntimeException("Can't link program: " + log);
        }
        gl.validateProgram(programId);
        if (gl.getProgram(programId, OpenGL.ProgramParam.VALIDATE_STATUS) == 0) {
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

    public void bindAttr(OpenGL gl, int attr, int count, int stride, Buffer buf) {
        gl.vertexAttribPointer(attr, count, OpenGL.Type.FLOAT, false, stride, buf);
        gl.enableVertexAttribArray(attr);
    }

    public void bindAttr(OpenGL gl, int attr, int floatsPerAttr, int floatsPerVertex, int offset) {
        gl.vertexAttribPointer(attr, floatsPerAttr, OpenGL.Type.FLOAT, false, floatsPerVertex*4, offset*4);
        gl.enableVertexAttribArray(attr);
    }

    public void bindMatrix(OpenGL gl, int index, Matrix2FGL matrix) {
        gl.uniformMatrix4(uniformLocations.get(index), 1, matrix.m, 0);
    }

    public void disableVertexAttrib(OpenGL gl) {
        for (int i=0; i<attributes.size(); i++) {
            gl.disableVertexAttribArray(i);
        }
    }
    public int getId() {
        return id;
    }
}
