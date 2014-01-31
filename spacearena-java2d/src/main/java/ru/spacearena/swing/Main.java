package ru.spacearena.swing;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.PlatformManager;
import ru.spacearena.engine.primitives.RectI;
import ru.spacearena.game.GameFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Main extends JComponent {

    private Engine engine;

    public Main(final Engine engine) {
        this.engine = engine;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                engine.resize(new RectI(0,0,getWidth(),getHeight()));
            }
        });

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }



    public static void main(String[] args) {

        final PlatformManager platformManager = new Java2DPlatformManager(Main.class.getPackage().getName());
        final Engine engine = GameFactory.createEngine(platformManager);

        final JFrame frame = new JFrame("Space Arena");
        frame.add(new Main(engine));
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
