package ru.spacearena.engine.graphics.font;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontIOTest {
    @Test
    public void testLoad() throws Exception {

        final FontData f = new FontData();
        f.setImageWidth(1);
        f.setImageHeight(2);
        f.setLineHeight(3);
        f.setFontSize(4);
        //f.setCharMetrics('a', 5, 6, 7, 8, 9);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FontIO.store(f, baos);

        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final FontData f2 = FontIO.load(bais);

        assertThat(f2.getImageWidth()).isEqualTo(f.getImageWidth());
        assertThat(f2.getImageHeight()).isEqualTo(f.getImageHeight());
        assertThat(f2.getLineHeight()).isEqualTo(f.getLineHeight());
        assertThat(f2.getFontSize()).isEqualTo(f.getFontSize());

        final CharData aChar = f.getCharInfo('a');
        final CharData a2Char = f2.getCharInfo('a');
        assertThat(a2Char.getX()).isEqualTo(aChar.getX());
        assertThat(a2Char.getY()).isEqualTo(aChar.getY());
        assertThat(a2Char.getWidth()).isEqualTo(aChar.getWidth());
        assertThat(a2Char.getOffsetX()).isEqualTo(aChar.getOffsetX());
        assertThat(a2Char.getAdvance()).isEqualTo(aChar.getAdvance());

    }
}
