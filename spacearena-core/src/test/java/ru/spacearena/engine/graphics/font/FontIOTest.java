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

        final Font f = new Font();
        f.setFontMetrics(1, 2, 3, 4);
        f.setCharMetrics('a', 5, 6, 7, 8, 9);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FontIO.store(f, baos);

        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final Font f2 = FontIO.load(bais);

        assertThat(f2.getImageWidth()).isEqualTo(f.getImageWidth());
        assertThat(f2.getImageHeight()).isEqualTo(f.getImageHeight());
        assertThat(f2.getLineHeight()).isEqualTo(f.getLineHeight());
        assertThat(f2.getOriginalSize()).isEqualTo(f.getOriginalSize());

        final CharGlyph aChar = f.getCharInfo('a');
        final CharGlyph a2Char = f2.getCharInfo('a');
        assertThat(a2Char.getX()).isEqualTo(aChar.getX());
        assertThat(a2Char.getY()).isEqualTo(aChar.getY());
        assertThat(a2Char.getWidth()).isEqualTo(aChar.getWidth());
        assertThat(a2Char.getOffset()).isEqualTo(aChar.getOffset());
        assertThat(a2Char.getAdvance()).isEqualTo(aChar.getAdvance());

    }
}
