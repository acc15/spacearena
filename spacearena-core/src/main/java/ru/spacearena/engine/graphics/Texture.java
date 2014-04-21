package ru.spacearena.engine.graphics;

import ru.spacearena.engine.util.ResourceUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-05-04
 */
public class Texture {

    public static class Definition implements GLObjectDefinition<Texture> {

        private int wrapS = 0, wrapT = 0, minFilter = OpenGL.GL_LINEAR, magFilter = OpenGL.GL_LINEAR;
        private final List<TextureLoader> loaders = new ArrayList<TextureLoader>();

        public static interface TextureLoader {
            void load(OpenGL gl, Texture t, int level);
        }

        public static class EmptyTextureLoader implements TextureLoader {
            private final int width, height, format, type;

            public EmptyTextureLoader(int width, int height, int format, int type) {
                this.width = width;
                this.height = height;
                this.format = format;
                this.type = type;
            }

            public void load(OpenGL gl, Texture t, int level) {
                gl.glTexImage2D(OpenGL.GL_TEXTURE_2D, level, width, height, format, type, null);
                t.setDimension(width, height);
            }
        }

        public static class URLTextureLoader implements TextureLoader {

            private final URL url;

            public URLTextureLoader(URL url) {
                this.url = url;
            }

            public void load(OpenGL gl, Texture t, int level) {
                gl.glTexImage2D(t, level, url);
            }
        }

        public Definition minFilter(int minFilter) {
            this.minFilter = minFilter;
            return this;
        }

        public Definition magFilter(int magFilter) {
            this.magFilter = magFilter;
            return this;
        }

        public Definition wrapS(int wrapS) {
            this.wrapS = wrapS;
            return this;
        }

        public Definition wrapT(int wrapT) {
            this.wrapT = wrapT;
            return this;
        }

        public Definition empty(int width, int height, int format, int type) {
            this.loaders.add(new EmptyTextureLoader(width, height, format, type));
            return this;
        }

        public Definition url(URL url) {
            this.loaders.add(new URLTextureLoader(url));
            return this;
        }

        public Definition urls(URL... textureUrls) {
            for (URL url: textureUrls) {
                this.loaders.add(new URLTextureLoader(url));
            }
            return this;
        }

        public Definition url(Class<?> baseClass, String resourceName) {
            return url(ResourceUtils.getUrl(baseClass, resourceName));
        }

        public Definition urls(Class<?> baseClass, String prefix, String suffix, int count) {
            for (int i=0; i<count; i++) {
                final String resourceName = prefix + "_" + i + "." + suffix;
                this.loaders.add(new URLTextureLoader(ResourceUtils.getUrl(baseClass, resourceName)));
            }
            return this;
        }

        public Texture create(GLDrawContext context) {
            final OpenGL gl = context.getGL();

            final Texture t = new Texture();
            t.setId(gl.glGenTexture());

            final int type = OpenGL.GL_TEXTURE_2D; // only 2D textures are supported
            gl.glBindTexture(OpenGL.GL_TEXTURE_2D, t.getId());
            if (minFilter != 0) {
                gl.glTexParameter(type, OpenGL.GL_TEXTURE_MIN_FILTER, minFilter);
            }
            if (magFilter != 0) {
                gl.glTexParameter(type, OpenGL.GL_TEXTURE_MAG_FILTER, magFilter);
            }
            if (wrapS != 0) {
                gl.glTexParameter(type, OpenGL.GL_TEXTURE_WRAP_S, wrapS);
            }
            if (wrapT != 0) {
                gl.glTexParameter(type, OpenGL.GL_TEXTURE_WRAP_T, wrapT);
            }
            for (int level = 0; level < loaders.size(); level++) {
                loaders.get(level).load(gl, t, level);
            }
            return t;
        }

        public void reference(GLDrawContext context, Texture object) {
        }

        public void delete(GLDrawContext context, Texture object) {
            context.getGL().glDeleteTexture(object.id);
        }
    }


    private int id;
    private int width;
    private int height;
    private boolean flipY = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public float getLeft() {
        return 0;
    }

    public float getRight() {
        return 1;
    }

    public float getTop() {
        return flipY ? 1 : 0;
    }

    public float getBottom() {
        return flipY ? 0 : 1;
    }

    public float computeX(float x) {
        return x;
    }

    public float computeY(float y) {
        return flipY ? 1-y : y;
    }

    public float getAspectRatio() {
        return (float)width/height;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

}
