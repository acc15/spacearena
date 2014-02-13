package ru.spacearena.java2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineFactory;
import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;
import ru.spacearena.game.GameFactory;
import ru.spacearena.java2d.engine.Java2DDrawContext;
import ru.spacearena.java2d.engine.Java2DMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        final Engine engine = GameFactory.createEngine(new EngineFactory() {
            public Matrix createMatrix() {
                return new Java2DMatrix();
            }

            public void enableInput(InputType inputType) {
                switch (inputType) {
                case KEYBOARD:
                    component.addKeyListener(new KeyListener() {
                        public void keyTyped(KeyEvent e) {
                            // TODO implement..

                        }

                        public void keyPressed(KeyEvent e) {
                            // TODO implement..

                        }

                        public void keyReleased(KeyEvent e) {
                            // TODO implement..

                        }
                    });
                    break;

                case MOUSE:
                    component.addMouseListener(new MouseListener() {
                        public void mouseClicked(MouseEvent e) {
                            // TODO implement..

                        }

                        public void mousePressed(MouseEvent e) {
                            // TODO implement..

                        }

                        public void mouseReleased(MouseEvent e) {
                            // TODO implement..

                        }

                        public void mouseEntered(MouseEvent e) {
                            // TODO implement..

                        }

                        public void mouseExited(MouseEvent e) {
                            // TODO implement..

                        }
                    });
                    component.addMouseMotionListener(new MouseMotionListener() {
                        public void mouseDragged(MouseEvent e) {
                            // TODO implement..

                        }

                        public void mouseMoved(MouseEvent e) {
                            // TODO implement..

                        }
                    });
                    component.addMouseWheelListener(new MouseWheelListener() {
                        public void mouseWheelMoved(MouseWheelEvent e) {
                            // TODO implement..

                        }
                    });
                    break;
                }
            }
        });

        final JFrame frame = new JFrame("SpaceArena");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(component);
        frame.setVisible(true);

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
