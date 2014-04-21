package ru.spacearena.engine.graphics.texture;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.util.ResourceUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class TextureDefinition implements Texture.Definition {

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

    public TextureDefinition minFilter(int minFilter) {
        this.minFilter = minFilter;
        return this;
    }

    public TextureDefinition magFilter(int magFilter) {
        this.magFilter = magFilter;
        return this;
    }

    public TextureDefinition wrapS(int wrapS) {
        this.wrapS = wrapS;
        return this;
    }

    public TextureDefinition wrapT(int wrapT) {
        this.wrapT = wrapT;
        return this;
    }

    public TextureDefinition empty(int width, int height, int format, int type) {
        this.loaders.add(new EmptyTextureLoader(width, height, format, type));
        return this;
    }

    public TextureDefinition url(URL url) {
        this.loaders.add(new URLTextureLoader(url));
        return this;
    }

    public TextureDefinition urls(URL... textureUrls) {
        for (URL url: textureUrls) {
            this.loaders.add(new URLTextureLoader(url));
        }
        return this;
    }

    public TextureDefinition url(Class<?> baseClass, String resourceName) {
        return url(ResourceUtils.getUrl(baseClass, resourceName));
    }

    public TextureDefinition urls(Class<?> baseClass, String prefix, String suffix, int count) {
        for (int i=0; i<count; i++) {
            final String resourceName = prefix + "_" + i + "." + suffix;
            this.loaders.add(new URLTextureLoader(ResourceUtils.getUrl(baseClass, resourceName)));
        }
        return this;
    }

    public Texture createTexture(OpenGL gl) {
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
}
