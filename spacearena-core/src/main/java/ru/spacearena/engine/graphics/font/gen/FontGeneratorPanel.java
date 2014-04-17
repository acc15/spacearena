package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.FontIO;

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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-09-04
 */
public class FontGeneratorPanel extends JPanel {

    public static final int GENERATE_TIMEOUT = 500;

    public static final int DEFAULT_COMPONENT_HEIGHT = 24;

    public static final String DEFAULT_ALPHABET =
            "!@#$%^&*()_+0123456789-=/|\\?.,:;[]`~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final JTextField alphabetText;
    private final JProgressBar progressBar;
    private final JComboBox fontFaceComboBox;
    private final JComboBox fontStyleCombobox;
    private final JFormattedTextField fontSizeField;
    private final JFormattedTextField charPadField;
    private final JFormattedTextField imageScale;

    //private final JFormattedTextField spreadField;
    private final JSlider distanceFieldOffsetSlider;
    private final JSlider distanceFieldScaleSlider;
    private final JCheckBox hqCheckBox;
    private final JLabel statusLabel;
    private final ImagePanel fontImagePane;
    private final ImagePanel dfImagePane;
    private final JButton saveButton;

    private final Timer timer = new Timer(GENERATE_TIMEOUT, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            startGenerationWorker();
        }
    });

    private FontGeneratorResult lastResult = null;


    // Swing Worker implementation is bound to use progress value from 0 to 100. After call setProgress(100)
    // task will be reported as DONE - EVEN if task is still running... looks very bad
    private SwingWorker<?,?> currentWorker;

    private static String suggestFileName(FontData fd) {
        final String name = fd.getFontName();
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.length(); i++) {
            final char ch = name.charAt(i);
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                sb.append(Character.toLowerCase(ch));
            }
        }
        if ((fd.getFontStyle() & FontData.BOLD) != 0) {
            sb.append("_b");
        }
        if ((fd.getFontStyle() & FontData.ITALIC) != 0) {
            sb.append("_i");
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public FontGeneratorPanel() {

        setLayout(new BorderLayout());

        timer.setRepeats(false);

        final JPanel alphabetPane = new JPanel(new BorderLayout(10, 5));
        alphabetPane.setBorder(new EmptyBorder(10, 10, 0, 10));

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

        alphabetPane.add(statusLabel = new JLabel(), BorderLayout.SOUTH);
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(0, DEFAULT_COMPONENT_HEIGHT));
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
        fontStyleCombobox.setFont(fontStyleCombobox.getFont().deriveFont(0));
        fontStyleCombobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //noinspection MagicConstant
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
        controlPane.add(fontSizeField = createIntegerTextField(135));
        controlPane.add(new JLabel("Char pad: "));
        controlPane.add(charPadField = createIntegerTextField(15));

        controlPane.add(new JLabel("Image scale (pow 2): "));
        controlPane.add(imageScale = createIntegerTextField(3));

        controlPane.add(new JLabel("Distance field offset: "));
        controlPane.add(distanceFieldOffsetSlider = new JSlider(-100, 100, 0));
        distanceFieldOffsetSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onParameterChange();
            }
        });

        controlPane.add(new JLabel("Distance field scale: "));
        controlPane.add(distanceFieldScaleSlider = new JSlider(1, 100, 20));
        distanceFieldScaleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onParameterChange();
            }
        });

        controlPane.add(hqCheckBox = new JCheckBox("HQ", true));
        hqCheckBox.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        hqCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParameterChange();
            }
        });

        saveButton = new JButton("Save!");
        saveButton.setEnabled(false);
        saveButton.setMaximumSize(new Dimension(Short.MAX_VALUE, DEFAULT_COMPONENT_HEIGHT));
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String userDir = System.getProperty("user.dir");
                final JFileChooser fileChooser = new JFileChooser(userDir);
                fileChooser.setApproveButtonText("Save");
                fileChooser.setDialogTitle("Save font texture and date (choose prefix)");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                final String initialFileName = suggestFileName(lastResult.getFontData());
                fileChooser.setSelectedFile(new File(initialFileName));

                if (fileChooser.showSaveDialog(FontGeneratorPanel.this) == JFileChooser.APPROVE_OPTION) {
                    final File baseFile = fileChooser.getSelectedFile();
                    final String prefixPath = baseFile.getPath();
                    FontIO.store(lastResult.getFontData(), new File(prefixPath + ".fnt"));
                    ImageUtils.storeImage(lastResult.getDistanceFieldTexture(), new File(prefixPath + ".png"));
                }

            }
        });

        controlPane.add(saveButton);

        for (int i=0; i<controlPane.getComponentCount(); i++) {
            final JComponent jc = (JComponent)controlPane.getComponent(i);
            jc.setAlignmentX(0f);
        }
        add(controlPane, BorderLayout.EAST);

        final JPanel imagesPane = new JPanel();
        imagesPane.setLayout(new GridBagLayout());

        imagesPane.add(createImagePane(fontImagePane = new ImagePanel(), "Font"), createGBC(0,0,5,0.5,0.9,GridBagConstraints.BOTH));
        imagesPane.add(createImagePane(dfImagePane = new ImagePanel(), "DF"), createGBC(1,0,5,0.5,0.9,GridBagConstraints.BOTH));
        add(imagesPane, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(0, DEFAULT_COMPONENT_HEIGHT));
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        startGenerationWorker();
    }

    private GridBagConstraints createGBC(int x, int y, int inset, double wx, double wy, int fill) {
        final GridBagConstraints gbcDF = new GridBagConstraints();
        gbcDF.gridx = x;
        gbcDF.gridy = y;
        gbcDF.insets = new Insets(inset,inset,inset,inset);
        gbcDF.weightx = wx;
        gbcDF.weighty = wy;
        gbcDF.fill = fill;
        return gbcDF;
    }

    private JPanel createImagePane(final ImagePanel image, final String label) {
        final JPanel imagePane = new JPanel();

        imagePane.setLayout(new BoxLayout(imagePane, BoxLayout.Y_AXIS));
        imagePane.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)
        ));


        final JLabel imageLabel = new JLabel(label);
        imageLabel.setAlignmentX(0.5f);
        imagePane.add(imageLabel);

        image.addPropertyChangeListener("image", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final BufferedImage i = (BufferedImage)evt.getNewValue();
                final int w = i.getWidth(), h = i.getHeight(), t = i.getType();
                imageLabel.setText(label + " (" + w + "x" + h + "x" +
                        ImageUtils.getImageTypeBits(t) + ", " + ImageUtils.getImageTypeFormat(t) + ")");

            }
        });
        image.setAlignmentX(0.5f);
        imagePane.add(image);
        imagePane.add(Box.createVerticalStrut(10));

        return imagePane;
    }

    private void onGenerationDone(FontGeneratorResult result) {
        this.lastResult = result;
        this.saveButton.setEnabled(true);
        this.fontImagePane.setImage(result.getFontTexture());
        this.dfImagePane.setImage(result.getDistanceFieldTexture());
    }

    private void onParameterChange() {
        timer.stop();
        if (currentWorker != null) {
            currentWorker.cancel(false);
            currentWorker = null;
        }
        progressBar.setValue(0);
        progressBar.setString("Preparing...");
        timer.start();
    }

    private void startGenerationWorker() {

        saveButton.setEnabled(false);
        lastResult = null; // reset last result to free memory

        final FontGeneratorInput input = new FontGeneratorInput();
        input.setAlphabet(alphabetText.getText());
        input.setHq(hqCheckBox.isSelected());
        input.setPad((Integer)charPadField.getValue());
        input.setImageScale((Integer)imageScale.getValue());
        input.setFontName((String)fontFaceComboBox.getSelectedItem());
        input.setFontSize((Integer)fontSizeField.getValue());
        input.setFontStyle(fontStyleCombobox.getSelectedIndex());
        input.setDistanceFieldOffset(distanceFieldOffsetSlider.getValue());
        input.setDistanceFieldScale(distanceFieldScaleSlider.getValue());

        final FontGeneratorWorker worker = new FontGeneratorWorker(input);
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (worker.isCancelled()) {
                    return;
                }
                if ("state".equals(evt.getPropertyName())) {
                    final SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
                    switch (state) {
                    case DONE:
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(progressBar.getMaximum());
                        progressBar.setString("Done");
                        try {
                            onGenerationDone(worker.get(1, TimeUnit.NANOSECONDS));
                        } catch (InterruptedException e1) {
                            throw new RuntimeException(e1);
                        } catch (ExecutionException e1) {
                            throw new RuntimeException(e1);
                        } catch (TimeoutException e1) {
                            throw new RuntimeException(e1);
                        }
                        break;

                    case STARTED:
                        progressBar.setIndeterminate(true);
                        break;
                    }
                } else if ("what".equals(evt.getPropertyName())) {
                    progressBar.setString((String) evt.getNewValue());
                }
            }
        });
        worker.execute();
        currentWorker = worker;
    }

    private JFormattedTextField createNumberTextField(NumberFormat format, Object value) {
        final NumberFormatter fmt = new NumberFormatter(format);
        fmt.setCommitsOnValidEdit(true);
        fmt.setValueClass(value.getClass());

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

    private JFormattedTextField createFloatTextField(float value) {
        return createNumberTextField(NumberFormat.getNumberInstance(), value);
    }

    private JFormattedTextField createIntegerTextField(int value) {
        return createNumberTextField(new DecimalFormat("#"), value);
    }

    @SuppressWarnings("unchecked")
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

            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
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
        fontFaceCombobox.setFont(fonts[fontIndex]);
        return fontFaceCombobox;
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
