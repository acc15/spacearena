package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.util.ResourceUtils;
import ru.spacearena.game.GameFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class FontGeneratorPanel extends JPanel {

    public static final int DEFAULT_COMPONENT_HEIGHT = 24;

    public static final String DEFAULT_ALPHABET =
            "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final JTextField alphabetText;
    private final JProgressBar progressBar;
    private final JComboBox fontFaceComboBox;
    private final JComboBox fontStyleCombobox;
    private final JFormattedTextField fontSizeField;
    private final JFormattedTextField charPadField;
    private final JSlider distanceFieldOffsetSlider;
    private final JSlider distanceFieldScaleSlider;
    private final JCheckBox hqCheckBox;

    private final Timer timer = new Timer(2000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Generate!");
        }
    });

    public FontGeneratorPanel() {
        timer.setRepeats(false);

        setLayout(new BorderLayout());

        final JPanel alphabetPane = new JPanel(new BorderLayout(10, 10));
        alphabetPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel alphabetLabel = new JLabel("Alphabet: ");
        alphabetPane.add(alphabetLabel, BorderLayout.WEST);

        alphabetPane.add(alphabetText = new JTextField(DEFAULT_ALPHABET), BorderLayout.CENTER);
        alphabetText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                onParameterChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onParameterChange();
            }

            public void changedUpdate(DocumentEvent e) {
                onParameterChange();
            }
        });
        add(alphabetPane, BorderLayout.NORTH);

        final JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
        controlPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        controlPane.add(new JLabel("Font face: "));

        controlPane.add(fontFaceComboBox = createFontComboBox(controlPane.getFont()));
        controlPane.add(new JLabel("Font style: "));

        fontStyleCombobox = new JComboBox(new String[] {"Normal", "Bold", "Italic"});
        fontStyleCombobox.setBackground(Color.WHITE);
        fontStyleCombobox.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        fontStyleCombobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fontStyleCombobox.setFont(
                        fontStyleCombobox.getFont().deriveFont(fontStyleCombobox.getSelectedIndex()));
                onParameterChange();
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
        controlPane.add(fontSizeField = createIntegerTextField(128));
        controlPane.add(new JLabel("Char pad: "));
        controlPane.add(charPadField = createIntegerTextField(4));

        controlPane.add(new JLabel("Distance field offset: "));
        controlPane.add(distanceFieldOffsetSlider = new JSlider(-100, 100, 16));
        distanceFieldOffsetSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onParameterChange();
            }
        });

        controlPane.add(new JLabel("Distance field scale: "));
        controlPane.add(distanceFieldScaleSlider = new JSlider(-100, 100, 20));
        distanceFieldScaleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onParameterChange();
            }
        });

        controlPane.add(hqCheckBox = new JCheckBox("HQ", true));
        hqCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParameterChange();
            }
        });

        for (int i=0; i<controlPane.getComponentCount(); i++) {
            final JComponent jc = (JComponent)controlPane.getComponent(i);
            jc.setAlignmentX(0f);
        }
        add(controlPane, BorderLayout.EAST);

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
        add(imagesPane, BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 5000);
        progressBar.setValue(2500);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setPreferredSize(new Dimension(0, DEFAULT_COMPONENT_HEIGHT));
        progressBar.setIndeterminate(false);
        add(progressBar, BorderLayout.SOUTH);

    }

    private JFormattedTextField createIntegerTextField(int value) {
        final NumberFormatter fmt = new NumberFormatter(new DecimalFormat("#"));
        fmt.setCommitsOnValidEdit(true);
        fmt.setValueClass(Integer.class);

        final JFormattedTextField tf = new JFormattedTextField(fmt);
        tf.setHorizontalAlignment(JTextField.RIGHT);
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        tf.setValue(value);
        tf.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                onParameterChange();
            }
        });
        return tf;
    }

    private JComboBox createFontComboBox(Font defaultFont) {

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
        fontFaceCombobox.setRenderer(new ListCellRenderer() {
            final JLabel label = new JLabel();

            {
                label.setOpaque(true);
            }

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (index < 0) {
                    index = list.getSelectedIndex();
                }
                final String name = (String) value;
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
        fontFaceCombobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fontFaceCombobox.setFont(fonts[fontFaceCombobox.getSelectedIndex()]);
                onParameterChange();
            }
        });

        return fontFaceCombobox;
    }

    public void onParameterChange() {
        timer.restart();
        System.out.println("Changed!");
    }


    public static void main(String[] args) {
        final JFrame frame = new JFrame("FontGenerator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new FontGeneratorPanel());
        frame.setVisible(true);
    }
}
