package ru.spacearena.engine.util;

import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class ResourceUtils {

    public static URL getUrl(Class<?> clazz, String name) {
        final URL url = clazz.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Resource \"" + name + "\" doesn't exists in classpath of " + clazz);
        }
        return url;
    }

}
