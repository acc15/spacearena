package ru.spacearena.engine.graphics.fbo;

import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-04
 */
public class FrameBufferObject {

    public static interface Definition {

        FrameBufferObject createFBO(OpenGL gl);

    }

}
