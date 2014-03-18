package ru.spacearena.android.engine;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class AndroidEngineTest {

    @Test
    public void testFormat() throws Exception {

        final String s = String.format("Unknown action code: 0x%02x", 1);
        assertThat(s).isEqualTo("Unknown action code: 0x01");

    }
}
