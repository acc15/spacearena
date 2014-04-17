package ru.spacearena.engine.graphics.font;

import org.junit.Test;
import ru.spacearena.game.GameFactory;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontDefinitionTest {

    @Test
    public void testFontDef() throws Exception {
        final FontDefinition fd = new FontDefinition(GameFactory.class, "test");
        System.out.println(fd.getFontUrl());

    }
}
