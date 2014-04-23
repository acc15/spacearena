package ru.spacearena.engine.graphics.fbo;

import ru.spacearena.engine.graphics.GLDrawContext;
import ru.spacearena.engine.graphics.GLObjectDefinition;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.Texture;
import ru.spacearena.engine.util.IntMathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-04
 */
public class FrameBufferObject {

    public int getId() {
        return id;
    }

    public static class Definition implements GLObjectDefinition<FrameBufferObject> {

        private final List<Attachment> attachments = new ArrayList<Attachment>();

        public static interface Attachment {
            void attach(GLDrawContext dc, FrameBufferObject fbo);
        }

        public static class TextureAttachment implements Attachment {
            private final int attachment;
            private final int target;
            private final int level;
            private Texture.Definition texture;

            public TextureAttachment(Texture.Definition texture) {
                this(texture, OpenGL.GL_COLOR_ATTACHMENT0);
            }

            public TextureAttachment(Texture.Definition texture, int attachment) {
                this(texture, attachment, OpenGL.GL_TEXTURE_2D);
            }

            public TextureAttachment(Texture.Definition texture, int attachment, int target) {
                this(texture, attachment, target, 0);
            }

            public TextureAttachment(Texture.Definition texture, int attachment, int target, int level) {
                this.texture = texture;
                this.attachment = attachment;
                this.target = target;
                this.level = level;
            }

            public void attach(GLDrawContext dc, FrameBufferObject fbo) {
                final Texture t = dc.obtain(texture);
                dc.getGL().glFramebufferTexture2D(attachment, target, t.getId(), level);
                fbo.onAttachment(t.getWidth(), t.getHeight());
            }
        }

        public Definition attach(Texture.Definition texture) {
            return attach(new TextureAttachment(texture));
        }

        public Definition attach(Attachment attachment) {
            attachments.add(attachment);
            return this;
        }

        public FrameBufferObject create(GLDrawContext context) {
            final FrameBufferObject fbo = new FrameBufferObject(context.getGL().glGenFrameBuffer());
            final OpenGL gl = context.getGL();
            gl.glBindFramebuffer(fbo.id);
            for (Attachment attachment: attachments) {
                attachment.attach(context, fbo);
            }
            final int completeness = context.getGL().glCheckFramebufferStatus();
            gl.glBindFramebuffer(0);
            if (completeness != OpenGL.GL_FRAMEBUFFER_COMPLETE) {
                gl.glDeleteFrameBuffer(fbo.id);
                switch (completeness) {
                    case OpenGL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                        throw new IllegalStateException("Not all framebuffer attachment points are framebuffer attachment complete. " +
                                "This means that at least one attachment point with a renderbuffer or texture attached" +
                                " has its attached object no longer in existence or has an attached image with a " +
                                "width or height of zero, or the color attachment point has a non-color-renderable " +
                                "image attached, or the depth attachment point has a non-depth-renderable image " +
                                "attached, or the stencil attachment point has a non-stencil-renderable image " +
                                "attached. Color-renderable formats include GL_RGBA4, GL_RGB5_A1, and GL_RGB565. " +
                                "GL_DEPTH_COMPONENT16 is the only depth-renderable format. " +
                                "GL_STENCIL_INDEX8 is the only stencil-renderable format.");

                    case OpenGL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
                        throw new IllegalStateException("Not all attached images have the same width and height.");

                    case OpenGL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                        throw new IllegalStateException("No images are attached to the framebuffer.");

                    case OpenGL.GL_FRAMEBUFFER_UNSUPPORTED:
                        throw new IllegalStateException("The combination of internal formats of the attached images " +
                                "violates an implementation-dependent set of restrictions.");

                    default:
                        throw new IllegalStateException("Framebuffer not complete. Unknown error code");
                }
            }
            return fbo;
        }

        public void reference(GLDrawContext context, FrameBufferObject object) {
        }

        public void delete(GLDrawContext context, FrameBufferObject object) {
            context.getGL().glDeleteFrameBuffer(object.id);
        }
    }

    private void onAttachment(int width, int height) {
        this.width = IntMathUtils.min(this.width, width);
        this.height = IntMathUtils.min(this.height, height);
    }

    private final int id;
    private int width = Integer.MAX_VALUE, height = Integer.MAX_VALUE;

    public FrameBufferObject(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
