package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.util.IOUtils;

import java.io.*;
import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontIO {

    public static FontData load(File file) {
        try {
            final InputStream is = new FileInputStream(file);
            try {
                return load(is);
            } finally {
                IOUtils.closeQuietly(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't open URL stream", e);
        }
    }

    public static FontData load(URL url) {
        try {
            final InputStream is = url.openStream();
            try {
                return load(is);
            } finally {
                IOUtils.closeQuietly(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't open URL stream", e);
        }
    }

    public static FontData load(InputStream is) {
        try {
            return (FontData) new ObjectInputStream(is).readObject();
        } catch (IOException e) {
            throw new RuntimeException("Can't read font object from stream", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load class from stream", e);
        }
    }

    public static void store(FontData fontData, OutputStream os) {
        try {
            new ObjectOutputStream(os).writeObject(fontData);
        } catch (IOException e) {
            throw new RuntimeException("Can't write font object", e);
        }
    }

    public static void store(FontData fontData, File file) {
        try {
            final OutputStream os = new FileOutputStream(file);
            try {
                store(fontData, os);
            } finally {
                IOUtils.closeQuietly(os);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't create file " + file + " for writing font", e);
        }
    }

}
