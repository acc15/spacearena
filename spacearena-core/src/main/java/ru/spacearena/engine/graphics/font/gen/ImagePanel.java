package ru.spacearena.engine.graphics.font.gen;

import javax.swing.*;
import java.awt.*;

/**
* @author Vyacheslav Mayorov
* @since 2014-09-04
*/
public class ImagePanel extends JPanel {

    private Image image;
    private float alignX, alignY;

    public ImagePanel() {
        this(null);
    }

    public ImagePanel(Image image) {
        this(image, 0.5f, 0.5f);
    }

    public ImagePanel(Image image, float alignX, float alignY) {
        super(true);
        this.image = image;
        this.alignX = alignX;
        this.alignY = alignY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if (this.image == image) {
            return;
        }
        final Image old = this.image;
        this.image = image;
        firePropertyChange("image", old, image);
        repaint();
    }

    public float getAlignX() {
        return alignX;
    }

    public void setAlignX(float alignX) {
        this.alignX = alignX;
        repaint();
    }

    public float getAlignY() {
        return alignY;
    }

    public void setAlignY(float alignY) {
        this.alignY = alignY;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            return;
        }

        final Graphics2D g2d = FontGenerator.getGraphics(g, true);

        final int iw = image.getWidth(null), ih = image.getHeight(null), cw = getWidth(), ch = getHeight();
        int sw = iw, sh = ih;
        if (sw > cw) {
            sh = (int)((float)(sh*cw)/sw);
            sw = cw;
        }
        if (sh > ch) {
            sw = (int)((float)(sw*ch)/sh);
            sh = ch;
        }
        final int l = (int)((float)(cw - sw) * alignX);
        final int t = (int)((float)(ch - sh) * alignY);
        g2d.drawImage(image, l, t, l+sw,t+sh, 0, 0, iw, ih, null);

    }
}
