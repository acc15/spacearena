package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public interface EngineEntity {

    void onAttach(Engine engine);
    void onDetach(Engine engine);

    /**
     * Called after OpenGL subsystem was initialized.
     * It is a good point to load textures, {@link ru.spacearena.engine.graphics.DrawContext#upload(
     *  ru.spacearena.engine.graphics.vbo.VertexBufferObject.Definition,
     *  ru.spacearena.engine.graphics.vbo.VertexBuffer) upload VBOs} and
     * {@link ru.spacearena.engine.graphics.DrawContext#make(
     *  ru.spacearena.engine.graphics.shaders.Program.Definition) make programs}.
     *  However you can make this things later - on the fly.
     *  It's just a notification - "i'm ready to start drawing".
     * @param context an object from which you can get whatever you want.
     */
    void onInit(DrawContext context);

    /**
     * Called when OpenGL subsystem is going down.
     * @param context an object from which you can get whatever you want.
     */
    void onDispose(DrawContext context);

    /**
     * Called when rendering area was re-sized
     * @param width new width
     * @param height new height
     */
    void onSize(float width, float height);

    /**
     * Called when input event occurs. Key press, mouse click, taps and so on.
     * @param inputEvent event to process
     * @return whether event should be consumed and further processing of event stopped
     */
    boolean onInput(InputEvent inputEvent);

    /**
     * Called when another frame
     * @param seconds seconds elapsed past previous frame
     * @return whether this object is live or not. It's a hint for container to remove this object
     */
    boolean onUpdate(float seconds);

    /**
     * Called when another frame should be drawn
     * @param context context to draw graphics
     */
    void onDraw(DrawContext context);
}
