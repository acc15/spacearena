package ru.spacearena.jogl.shaders;

import javax.media.opengl.GL2ES2;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public abstract class CompilableObject {

    private int id;

    public boolean isCompiled() {
        return id != 0;
    }

    public int getId() {
        return id;
    }

    public void compile(GL2ES2 gl) {
        if (isCompiled()) {
            return;
        }
        this.id = doCompile(gl);
    }

    protected abstract int doCompile(GL2ES2 gl);

}
