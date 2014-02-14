package ru.spacearena.java2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.game.GameFactory;
import ru.spacearena.java2d.engine.Java2DDrawContext;
import ru.spacearena.java2d.engine.Java2DEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class EngineComponent extends Canvas {

    private static final Logger logger = LoggerFactory.getLogger(EngineComponent.class);

    void gameLoop(final Engine engine) {

        final BufferStrategy strategy = getBufferStrategy();
        final Java2DDrawContext drawContext = new Java2DDrawContext();
        while (true) {
            if (!engine.onUpdate()) {
                return;
            }
            final Graphics2D graphics2D = (Graphics2D)strategy.getDrawGraphics();
            try {
                engine.onDraw(drawContext.wrap(graphics2D, getWidth(), getHeight()));
            } finally {
                graphics2D.dispose();
            }
            strategy.show();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error("Engine thread was interrupted", e);
            }
        }
    }

    public static void main(String[] args) {

        final EngineComponent component = new EngineComponent();
        final JFrame frame = new JFrame("SpaceArena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(component);
        frame.setVisible(true);

        final EngineFactory factory = new GameFactory();
        final Engine engine = new Java2DEngine(factory, component);
        component.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                engine.onSize(component.getWidth(), component.getHeight());
            }
        });
        component.setBackground(null);
        component.setIgnoreRepaint(true);
        component.requestFocus();
        component.createBufferStrategy(2);
        component.gameLoop(engine);

    }

}
