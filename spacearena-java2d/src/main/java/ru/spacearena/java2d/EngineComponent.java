package ru.spacearena.java2d;

import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.SizeEvent;
import ru.spacearena.game.GameFactory;
import ru.spacearena.java2d.engine.Java2DEngine;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class EngineComponent extends Canvas {

    public static void start(EngineFactory factory, String title) {
        final Frame frame = new Frame(title);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.setBackground(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // TODO add ability to switch between windowed and full-screen
        /*GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (d.isFullScreenSupported()) {
            frame.setUndecorated(true);
            frame.setResizable(false);
            d.setFullScreenWindow(frame);
        }*/

        final EngineComponent component = new EngineComponent();
        frame.add(component);
        frame.setVisible(true);

        final Java2DEngine engine = new Java2DEngine(factory, component);
        component.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                engine.scheduleEvent(new SizeEvent(e.getComponent().getWidth(), e.getComponent().getHeight()));
            }
        });
        component.setBackground(null);
        component.setIgnoreRepaint(true);
        component.requestFocus();
        component.createBufferStrategy(2);
        engine.gameLoop();

    }

    public static void main(String[] args) {
        start(new GameFactory(), "SpaceArena");
    }

}
