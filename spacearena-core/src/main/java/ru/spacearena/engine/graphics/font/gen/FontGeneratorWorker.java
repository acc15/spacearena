package ru.spacearena.engine.graphics.font.gen;

import ru.spacearena.engine.graphics.font.FontData;
import ru.spacearena.engine.graphics.font.gen.df.DeadReckoning;
import ru.spacearena.engine.graphics.font.gen.df.DistanceField;
import ru.spacearena.engine.util.FloatMathUtils;

import javax.swing.*;
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

    @Override
    protected FontGeneratorResult doInBackground() throws Exception {
        firePropertyChange("what", null, "Computing font layout...");
        final FontData fd = FontGenerator.computeFontData(
                input.getFont(), input.getPad(), input.getImageScale(), input.getAlphabet());

        firePropertyChange("what", null, "Generating font image...");
        final BufferedImage fi = FontGenerator.generateFontImage(input.getFont(), fd, input.isHq());

        final float d = 1;
        firePropertyChange("what", null, "Computing distance field...");
        final DistanceField df = DeadReckoning.computeDistanceField(fi, d, FloatMathUtils.length(d,d));

        firePropertyChange("what", null, "Mapping distance field to image...");
        final BufferedImage dfi = DeadReckoning.toImage(df,
                new DeadReckoning.ManualMap(input.getDistanceFieldOffset(), input.getDistanceFieldScale()));

        firePropertyChange("what", null, "Resizing distance field...");
        final BufferedImage df2 = DeadReckoning.scaleDownByPow2(dfi, input.getImageScale());

        return new FontGeneratorResult(fd, fi, df2);
    }
}
