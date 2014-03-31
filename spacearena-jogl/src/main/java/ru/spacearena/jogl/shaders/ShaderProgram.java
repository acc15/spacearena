package ru.spacearena.jogl.shaders;

import cern.colt.list.IntArrayList;

import javax.media.opengl.GL2ES2;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class ShaderProgram extends CompilableObject {

    private final List<Shader> shaders = new ArrayList<Shader>();
    private final List<String> attributes = new ArrayList<String>();
    private final List<String> uniforms = new ArrayList<String>();

    private final IntArrayList attributeLocations = new IntArrayList();
    private final IntArrayList uniformLocations = new IntArrayList();

    public void addShader(Shader shader) {
        this.shaders.add(shader);
    }

    public void addAttribute(String attribute) {
        this.attributes.add(attribute);
    }

    public void addUniform(String uniform) {
        this.uniforms.add(uniform);
    }

    public int getAttributeLocation(int attributeIndex) {
        return attributeLocations.get(attributeIndex);
    }

    public int getUniformLocation(int uniformIndex) {
        return uniformLocations.get(uniformIndex);
    }

    public int doCompile(GL2ES2 gl) {
        for (final Shader shader: shaders) {
            shader.compile(gl);
        }

        final int programId = gl.glCreateProgram();
        for (final Shader shader: shaders) {
            gl.glAttachShader(programId, shader.getId());
        }

        // explicit binding is optional
//            final int l = attributes.size();
//            for (int i=0; i<l; i++) {
//                final String attributeName = attributes.get(i);
//                gl.glBindAttribLocation(programId, i, attributeName);
//            }
        gl.glLinkProgram(programId);
        // todo check link status
        gl.glValidateProgram(programId);

        for (String attribute: attributes) {
            final int attrLoc = gl.glGetAttribLocation(programId, attribute);
            attributeLocations.add(attrLoc);
        }
        for (String uniform: uniforms) {
            final int uniformLoc = gl.glGetUniformLocation(programId, uniform);
            uniformLocations.add(uniformLoc);
        }
        return programId;
    }

    public void bindAttr(GL2ES2 gl, int attr, int floatsPerAttr, int floatsPerVertex, int offset) {
        final int location = attributeLocations.get(attr);
        gl.glVertexAttribPointer(location, floatsPerAttr, GL2ES2.GL_FLOAT, false, floatsPerVertex*4, offset*4);
        gl.glEnableVertexAttribArray(location);
    }

    public void bindMatrix(GL2ES2 gl, int index, float[] mat4) {
        gl.glUniformMatrix4fv(uniformLocations.get(index), 1, false, mat4, 0);
    }

    public void bindMatrix(GL2ES2 gl, int index, FloatBuffer buf) {
        gl.glUniformMatrix4fv(uniformLocations.get(index), 1, false, buf);
    }

    public void disableVertexAttrib(GL2ES2 gl) {
        for (int i=0; i<attributeLocations.size(); i++) {
            final int attr = attributeLocations.get(0);
            gl.glDisableVertexAttribArray(attr);
        }
    }
}
