package ru.spacearena.jogl;

import com.jogamp.opengl.util.texture.TextureIO;
import org.junit.Test;
import ru.spacearena.game.GameFactory;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-05-04
 */
public class JoglWindowTest {


    @Test
    public void testName() throws Exception {
        final com.jogamp.opengl.util.texture.Texture t = TextureIO.newTexture(GameFactory.class.getResource("ship.png"), false, null);
        System.out.println(t.getWidth());
        System.out.println(t.getHeight());
    }
}
