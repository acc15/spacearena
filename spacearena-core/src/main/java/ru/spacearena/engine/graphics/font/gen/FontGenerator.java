package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.util.ResourceUtils;
import ru.spacearena.game.GameFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class FontGenerator {


    public static final int DEFAULT_COMPONENT_HEIGHT = 24;

    public static final String DEFAULT_ALPHABET = "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//
//    public static class Args {
//        public String fontName;
//        public int fontSize;
//        public int fontStyle;
//        public int width;
//        public int pad;
//        public boolean mipmap;
//        public boolean hq;
//    }
//
//    public static int parseFontStyle(String n) {
//        if (n.isEmpty() || "p".equals(n) || "plain".equals(n)) {
//            return java.awt.Font.PLAIN;
//        }
//        if ("b".equals(n) || "bold".equals(n)) {
//            return java.awt.Font.BOLD;
//        }
//        if ("i".equals(n) || "italic".equals(n)) {
//            return java.awt.Font.ITALIC;
//        }
//        throw new IllegalArgumentException("Unknown font style: " + n);
//    }
//
//    public static Args parseArgs(String[] args) {
//        final Arguments a = Arguments.parse(args);
//        final Args p = new Args();
//        p.fontName = a.getValue("font");
//        p.fontSize = a.getInt("size", 24);
//        p.fontStyle = parseFontStyle(a.getValue("style"));
//        p.pad = a.getInt("pad", 2);
//        p.width = a.getInt("width", 512);
//        p.hq = a.has("hq");
//        p.mipmap = a.has("mipmap");
//        return p;
//    }


    public static ru.spacearena.engine.graphics.font.Font computeFontInfo(java.awt.Font font, int maxWidth, int pad, char[] alphabet) {

        final AffineTransform t = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(t, true, true);
        final ru.spacearena.engine.graphics.font.Font fi = new ru.spacearena.engine.graphics.font.Font();

        final LineMetrics lm = font.getLineMetrics(alphabet, 0, alphabet.length, frc);
        final int lineHeight = getAsInt(lm.getHeight());

        fi.setSpaceAdvance(getAsInt(font.getStringBounds(" ", frc).getWidth()));
        fi.setTabAdvance(fi.getSpaceAdvance() * 4);

        int x = 0, y = 0;
        for (int i=0; i<alphabet.length; i++) {
            final TextLayout textLayout = new TextLayout(Character.toString(alphabet[i]), font, frc);

            final Rectangle2D tr = textLayout.getBounds();
            final Rectangle2D fr = font.getStringBounds(alphabet, i, i + 1, frc);
            final int advance = getAsInt(fr.getWidth());
            final int offset = (int)Math.round(tr.getX()) - pad;
            final int width = getAsInt(tr.getWidth()) + pad * 2;

            if (x + width > maxWidth) {
                x = 0;
                y += lineHeight;
            }

            fi.setCharMetrics(alphabet[i], x, y, offset, advance, width);
            x += width;

        }
        fi.setFontMetrics(y == 0 ? x : maxWidth, pow2RoundUp(y+lineHeight), lineHeight, font.getSize());
        return fi;
    }

    public static int pow2RoundUp (int x) {
        if (x < 0) {
            return 0;
        }
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x+1;
    }

    private static String suggestFileName(java.awt.Font font) {
        final String name = font.getFontName();
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.length(); i++) {
            final char ch = name.charAt(i);
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                sb.append(Character.toLowerCase(ch));
            }
        }
        switch (font.getStyle()) {
        case java.awt.Font.BOLD: sb.append("_b"); break;
        case java.awt.Font.ITALIC: sb.append("_i"); break;
        }
        return sb.toString();
    }

//    public static int storeWithMipMaps(BufferedImage image, Args args, int level, String format) {
//        try {
//            ImageIO.write(image, "png", new File(String.format(format,level)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (!args.mipmap) {
//            return level;
//        }
//
//        final int iw = image.getWidth(), ih = image.getHeight();
//        int w = iw >> 1, h = ih >> 1;
//        if (w < 1 && h < 1) {
//            return level;
//        }
//
//        w = Math.max(w,1);
//        h = Math.max(h,1);
//
//        final BufferedImage mipmap = new BufferedImage(w,h,image.getType());
//        final Graphics2D g = getGraphics(mipmap, args.hq);
//        g.drawImage(image, 0, 0, w, h, 0, 0, iw, ih, null);
//        g.dispose();
//        return storeWithMipMaps(mipmap, args, level+1, format);
//
//    }

    public static Graphics2D getGraphics(Graphics g, boolean hq) {
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (hq) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        return g2d;
    }

    public static Graphics2D getGraphics(BufferedImage img, boolean hq) {
        return getGraphics(img.getGraphics(), hq);
    }

    private static JFormattedTextField createIntegerTextField() {
        final NumberFormatter fmt = new NumberFormatter(new DecimalFormat("#"));
        fmt.setCommitsOnValidEdit(true);
        fmt.setValueClass(Integer.class);

        final JFormattedTextField tf = new JFormattedTextField(fmt);
        tf.setHorizontalAlignment(JTextField.RIGHT);
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        return tf;
    }

    private static JComboBox createFontComboBox(Font defaultFont) {

        final AffineTransform at = new AffineTransform();
        final FontRenderContext frc = new FontRenderContext(at, true, true);
        final String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        final Font[] fonts = new Font[fontNames.length];

        double maxX = 0, maxY = 0;

        int fontIndex = 0;
        for (int i=0; i<fontNames.length; i++) {
            final String fontName = fontNames[i];
            if (fontName.equals(defaultFont.getName())) {
                fontIndex = i;
            }

            Font font = new Font(fontName, Font.PLAIN, 15);
            if (font.canDisplayUpTo(fontName) >= 0) {
                font = defaultFont;
            }
            final Rectangle2D r = font.getStringBounds(fontName, frc);
            if (r.getWidth() > maxX) {
                maxX = r.getWidth();
            }
            if (r.getHeight() > maxY) {
                maxY = r.getHeight();
            }
            fonts[i] = font;
        }

        final int lWidth = (int)maxX, lHeight = (int)maxY;
        final JComboBox fontFaceCombobox = new JComboBox(fontNames);
        fontFaceCombobox.setPreferredSize(new Dimension(lWidth, lHeight));
        fontFaceCombobox.setMaximumSize(new Dimension(Short.MAX_VALUE, lHeight));
        fontFaceCombobox.setBackground(Color.WHITE);
        fontFaceCombobox.setEditable(false);
        fontFaceCombobox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fontFaceCombobox.setFont(fonts[fontFaceCombobox.getSelectedIndex()]);
                }
            }
        });
        fontFaceCombobox.setRenderer(new ListCellRenderer() {
            final JLabel label = new JLabel();
            {
                label.setOpaque(true);
            }

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (index < 0){
                    index = list.getSelectedIndex();
                }
                final String name = (String)value;
                final Font font = fonts[index];
                label.setBackground(cellHasFocus || isSelected ? Color.LIGHT_GRAY : Color.WHITE);
                label.setFont(font);
                label.setText(name);
                label.setVerticalTextPosition(JLabel.CENTER);
                label.setPreferredSize(new Dimension(lWidth, lHeight));
                return label;
            }
        });
        fontFaceCombobox.setSelectedIndex(fontIndex);

        return fontFaceCombobox;
    }

    public static class ImagePanel extends JPanel {

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
            this.image = image;
        }

        public float getAlignX() {
            return alignX;
        }

        public void setAlignX(float alignX) {
            this.alignX = alignX;
        }

        public float getAlignY() {
            return alignY;
        }

        public void setAlignY(float alignY) {
            this.alignY = alignY;
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

    public static void main(String[] args) {

        final JFrame frame = new JFrame("FontGenerator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);

        final JPanel rootPane = new JPanel(new BorderLayout(10, 10));

        final JPanel alphabetPane = new JPanel(new BorderLayout(10, 10));
        alphabetPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel alphabetLabel = new JLabel("Alphabet: ");
        alphabetPane.add(alphabetLabel, BorderLayout.WEST);

        final JTextField alphabetText = new JTextField(DEFAULT_ALPHABET);
        alphabetPane.add(alphabetText, BorderLayout.CENTER);
        rootPane.add(alphabetPane, BorderLayout.NORTH);

        final JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
        controlPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        controlPane.add(new JLabel("Font face: "));

        controlPane.add(createFontComboBox(controlPane.getFont()));
        controlPane.add(new JLabel("Font style: "));

        final JComboBox fontStyleCombobox = new JComboBox(new String[] {"Normal", "Bold", "Italic"});
        fontStyleCombobox.setBackground(Color.WHITE);
        fontStyleCombobox.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        fontStyleCombobox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fontStyleCombobox.setFont(
                            fontStyleCombobox.getFont().deriveFont(fontStyleCombobox.getSelectedIndex()));
                }
            }
        });
        fontStyleCombobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(c.getFont().deriveFont(index));
                return c;
            }
        });
        controlPane.add(fontStyleCombobox);

        controlPane.add(new JLabel("Font size: "));
        controlPane.add(createIntegerTextField());

        controlPane.add(new JLabel("Char pad: "));
        controlPane.add(createIntegerTextField());

        controlPane.add(new JCheckBox("HQ"));

        for (int i=0; i<controlPane.getComponentCount(); i++) {
            final JComponent jc = (JComponent)controlPane.getComponent(i);
            jc.setAlignmentX(0f);
        }

        rootPane.add(controlPane, BorderLayout.EAST);

        final JPanel imagesPane = new JPanel();
        imagesPane.setLayout(new BoxLayout(imagesPane, BoxLayout.Y_AXIS));

        final JPanel fontPane = new JPanel();
        fontPane.setLayout(new BoxLayout(fontPane, BoxLayout.Y_AXIS));
        fontPane.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(10,10,10,10),
                new LineBorder(Color.DARK_GRAY, 2)),
                new EmptyBorder(10, 10, 10, 10)));
        final ImagePanel fontImagePane = new ImagePanel();
        fontImagePane.setAlignmentX(0.5f);
        fontPane.add(fontImagePane);
        fontPane.add(Box.createVerticalStrut(10));

        final JButton fontSaveButton = new JButton("Save!");
        fontSaveButton.setAlignmentX(0.5f);
        fontPane.add(fontSaveButton);
        imagesPane.add(fontPane);

        final JPanel dfPane = new JPanel();
        dfPane.setLayout(new BoxLayout(dfPane, BoxLayout.Y_AXIS));
        dfPane.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(0,10,10,10),
                new LineBorder(Color.DARK_GRAY, 2)),
                new EmptyBorder(10, 10, 10, 10)));
        final ImagePanel dfImagePane = new ImagePanel(ImageUtils.loadImage(ResourceUtils.getUrl(GameFactory.class, "df-original.png")));
        dfImagePane.setAlignmentX(0.5f);
        dfPane.add(dfImagePane);
        dfPane.add(Box.createVerticalStrut(10));

        final JButton dfSaveButton = new JButton("Save!");
        dfSaveButton.setAlignmentX(0.5f);
        dfPane.add(dfSaveButton);
        imagesPane.add(dfPane);

        rootPane.add(new JScrollPane(imagesPane), BorderLayout.CENTER);

        frame.setContentPane(rootPane);
        frame.setVisible(true);
        /*
        final Args parsedArgs = parseArgs(args);

        final String fontName = parsedArgs.fontName;
        final int style = parsedArgs.fontStyle;
        final int size = parsedArgs.fontSize;

        final char[] alphabet = "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final int imageWidth = pow2RoundUp(parsedArgs.width);

        final java.awt.Font font = new java.awt.Font(fontName, style, size);
        final ru.spacearena.engine.graphics.font.Font fi = computeFontInfo(font, imageWidth, parsedArgs.pad, alphabet);

        final BufferedImage img = new BufferedImage(fi.getImageWidth(), fi.getImageHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = getGraphics(img, parsedArgs.hq);
        g.setFont(font);
        g.setColor(Color.WHITE);

        final int ascent = g.getFontMetrics().getAscent();
        for (int i = 0; i < alphabet.length; i++) {
            final char ch = alphabet[i];
            final CharGlyph ci = fi.getCharInfo(ch);
            g.drawChars(alphabet, i, 1, ci.getX() - ci.getOffset(), ci.getY() + ascent);
        }
        g.dispose();

        final String filePrefix = suggestFileName(font);
        final int maxMipMap = storeWithMipMaps(img, parsedArgs, 0, filePrefix + "_%d.png");
        fi.setMaxMipMap(maxMipMap);
        FontIO.store(fi, new File(filePrefix + ".fnt"));
        */
    }

    public static int getAsInt(double w) {
        return (int)Math.ceil(w);
    }

}
