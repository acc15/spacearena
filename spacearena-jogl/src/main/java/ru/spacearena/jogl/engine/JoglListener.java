package ru.spacearena.jogl.engine;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-01-04
*/
public interface JoglListener {
    public void init(OpenGL gl);
    public void dispose(OpenGL gl);
    public void display(OpenGL gl);
    public void reshape(OpenGL gl, int x, int y, int width, int height);
}
