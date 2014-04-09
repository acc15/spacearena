package ru.spacearena.engine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            logger.error("Can't close object", e);
        }
    }

    public static String readStream(InputStream i) throws IOException {
        final Reader r = new InputStreamReader(i);
        final StringBuilder sb = new StringBuilder(100);
        final char[] ch = new char[64];
        int readed;
        while ((readed = r.read(ch)) >= 0) {
            sb.append(ch, 0, readed);
        }
        return sb.toString();
    }
}
