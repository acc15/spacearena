package ru.spacearena.engine.graphics.texture;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.util.ResourceUtils;

import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class TextureDefinition implements Texture.Definition {

    private int wrapS = 0, wrapT = 0, minFilter = OpenGL.LINEAR, magFilter = OpenGL.LINEAR;
    private URL[] textureUrls;

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

    public TextureDefinition urls(URL... textureUrls) {
        this.textureUrls = textureUrls;
        return this;
    }

    public TextureDefinition url(Class<?> baseClass, String resourceName) {
        this.textureUrls = new URL[] {ResourceUtils.getUrl(baseClass, resourceName)};
        return this;
    }

    public TextureDefinition urls(Class<?> baseClass, String prefix, String suffix, int count) {
        final URL[] urls = new URL[count];
        for (int i=0; i<count; i++) {
            final String resourceName = prefix + "_" + i + "." + suffix;
            final URL url = ResourceUtils.getUrl(baseClass, resourceName);
            urls[i] = url;
        }
        this.textureUrls = urls;
        return this;
    }


    public int getWrapS() {
        return wrapS;
    }

    public int getWrapT() {
        return wrapT;
    }

    public int getMinFilter() {
        return minFilter;
    }

    public int getMagFilter() {
        return magFilter;
    }

    public URL[] getTextureUrls() { return textureUrls; }

    public Texture createTexture(OpenGL gl) {
        final Texture t = new Texture();
        t.setId(gl.genTexture());

        final int type = OpenGL.TEXTURE_2D; // only 2D textures are supported
        gl.bindTexture(OpenGL.TEXTURE_2D, t.getId());
        if (minFilter != 0) {
            gl.texParameter(type, OpenGL.TEXTURE_MIN_FILTER, minFilter);
        }
        if (magFilter != 0) {
            gl.texParameter(type, OpenGL.TEXTURE_MAG_FILTER, magFilter);
        }
        if (wrapS != 0) {
            gl.texParameter(type, OpenGL.TEXTURE_WRAP_S, wrapS);
        }
        if (wrapT != 0) {
            gl.texParameter(type, OpenGL.TEXTURE_WRAP_T, wrapT);
        }
        for (int level = 0; level < textureUrls.length; level++) {
            final URL url = textureUrls[level];
            gl.texImage2D(t, level, url);
        }
        return t;
    }
}
