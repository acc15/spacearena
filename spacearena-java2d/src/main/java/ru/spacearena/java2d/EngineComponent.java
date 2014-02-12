package ru.spacearena.java2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.game.GameFactory;
import ru.spacearena.java2d.engine.Java2DDrawContext;
import ru.spacearena.java2d.engine.Java2DEngineFactory;

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

    private Java2DDrawContext drawContext = new Java2DDrawContext();
    private Engine engine;

    public EngineComponent(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void paint(Graphics g) {
        final Graphics2D graphics2D = (Graphics2D) g;
        engine.onDraw(drawContext.wrap(graphics2D, getWidth(), getHeight()));
    }

    void gameLoop() {
        final BufferStrategy strategy = getBufferStrategy();
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

        Engine.init(new Java2DEngineFactory());

        final Engine engine = GameFactory.createEngine();
        final EngineComponent component = new EngineComponent(engine);

        final JFrame frame = new JFrame("SpaceArena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(component);

        component.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                engine.onSize(component.getWidth(), component.getHeight());
            }
        });
        component.setBackground(null);
        component.setIgnoreRepaint(true);

        frame.setVisible(true);

        component.createBufferStrategy(2);
        component.gameLoop();

    }

}
