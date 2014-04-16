package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class ImageUtils {


    public static void storeImage(BufferedImage image, File file) {
        try {
            final String ext = IOUtils.getExtension(file.getName());
            ImageIO.write(image, ext, file);
        } catch (IOException e) {
            throw new RuntimeException("Can't store image to file: " + file, e);
        }
    }

    public static BufferedImage loadImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Can't load image from url: " + url, e);
        }

    }

    public static int getImageTypeBits(int type) {
        switch (type) {
        case BufferedImage.TYPE_CUSTOM:
            return 0;
        case BufferedImage.TYPE_INT_RGB:
            return 32;
        case BufferedImage.TYPE_INT_ARGB:
            return 32;
        case BufferedImage.TYPE_INT_ARGB_PRE:
            return 32;
        case BufferedImage.TYPE_INT_BGR:
            return 32;
        case BufferedImage.TYPE_3BYTE_BGR:
            return 24;
        case BufferedImage.TYPE_4BYTE_ABGR:
            return 32;
        case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            return 32;
        case BufferedImage.TYPE_USHORT_565_RGB:
            return 16;
        case BufferedImage.TYPE_USHORT_555_RGB:
            return 16;
        case BufferedImage.TYPE_BYTE_GRAY:
            return 8;
        case BufferedImage.TYPE_USHORT_GRAY:
            return 8;
        case BufferedImage.TYPE_BYTE_BINARY:
            return 8;
        case BufferedImage.TYPE_BYTE_INDEXED:
            return 8;
        default:
            throw new IllegalArgumentException("Unknown buffered image type: " + type);
        }
    }

    public static String getImageTypeFormat(int type) {
        switch (type) {
        case BufferedImage.TYPE_CUSTOM:
            return "custom";
        case BufferedImage.TYPE_INT_RGB:
            return "rgb";
        case BufferedImage.TYPE_INT_ARGB:
            return "argb";
        case BufferedImage.TYPE_INT_ARGB_PRE:
            return "argb-pre";
        case BufferedImage.TYPE_INT_BGR:
            return "bgr";
        case BufferedImage.TYPE_3BYTE_BGR:
            return "bgr";
        case BufferedImage.TYPE_4BYTE_ABGR:
            return "abgr";
        case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            return "abgr-pre";
        case BufferedImage.TYPE_USHORT_565_RGB:
            return "rgb";
        case BufferedImage.TYPE_USHORT_555_RGB:
            return "rgb";
        case BufferedImage.TYPE_BYTE_GRAY:
            return "gray";
        case BufferedImage.TYPE_USHORT_GRAY:
            return "gray";
        case BufferedImage.TYPE_BYTE_BINARY:
            return "binary";
        case BufferedImage.TYPE_BYTE_INDEXED:
            return "indexed";
        default:
            throw new IllegalArgumentException("Unknown buffered image type: " + type);
        }

    }
}
