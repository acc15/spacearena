package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.gen.df.DeadReckoning;
import ru.spacearena.engine.graphics.font.gen.df.DistanceField;
import ru.spacearena.engine.util.FloatMathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
* @author Vyacheslav Mayorov
* @since 2014-10-04
*/
class FontGeneratorWorker extends SwingWorker<FontGeneratorResult, Object> {

    private final FontGeneratorInput input;

    public FontGeneratorWorker(FontGeneratorInput input) {
        this.input = input;
    }

    private void fireAction(String action) {
        firePropertyChange("what", null, action);
    }

    @Override
    protected FontGeneratorResult doInBackground() throws Exception {

        final Font font = FontGenerator.createFont(input);

        fireAction("Computing font layout...");
        final FontData fd = FontGenerator.computeFontData(font, input);

        fireAction("Generating font image...");
        final BufferedImage fi = FontGenerator.generateFontImage(font, input, fd);

        final float d = 1;
        fireAction("Computing distance field...");
        final DistanceField df = DeadReckoning.computeDistanceField(fi, d, FloatMathUtils.length(d,d));

        fireAction("Mapping distance field to image...");
        final BufferedImage dfi = DeadReckoning.toImage(df,
                new DeadReckoning.ManualMap(input.getDistanceFieldOffset(), input.getDistanceFieldScale()));

        fireAction("Resizing distance field...");
        final BufferedImage df2 = DeadReckoning.scaleDownByPow2(dfi, input.getImageScale());

        return new FontGeneratorResult(fd, fi, df2);
    }
}
