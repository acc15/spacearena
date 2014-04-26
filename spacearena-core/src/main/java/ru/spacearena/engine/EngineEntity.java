package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.graphics.DrawContext2f;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public interface EngineEntity {

    void onAttach(Engine engine);
    void onDetach(Engine engine);

    /**
     * Called after OpenGL subsystem was initialized.
     * It is a good point to load textures, {@link ru.spacearena.engine.graphics.GLDrawContext#upload(
     *  ru.spacearena.engine.graphics.vbo.VertexBufferObject.Definition,
     *  ru.spacearena.engine.graphics.vbo.VertexBuffer) upload VBOs} and
     * {@link ru.spacearena.engine.graphics.GLDrawContext#obtain(ru.spacearena.engine.graphics.GLObjectDefinition) make programs}.
     *  However you can make this things later - on the fly.
     *  It's just a notification - "i'm ready to start drawing".
     * @param context draw context to pre-load some resources
     */
    void onInit(DrawContext2f context);

    /**
     * Called when OpenGL subsystem is going down.
     * @param context draw context to .. hmm use cases isn't known (yet?)
     *                since DrawContext clears all resources automatically
     */
    void onDispose(DrawContext2f context);

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
     */
    void onUpdate(float seconds);

    /**
     * Called when another frame should be drawn
     * @param context context to draw graphics
     */
    void onDraw(DrawContext2f context);

    /**
     * Checks whether this object is visible or not
     * It's a hint for container to skip draw method for child entity
     * @return whether this object is visible or not
     */
    boolean isHidden();

    /**
     * Checks whether this object is live or not.
     * It's a hint for container to remove this object and detach it from engine
     * @return whether this object is live or not
     */
    boolean isDeleted();

}
