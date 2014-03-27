package ru.spacearena.java2d.engine;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineException;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.events.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Java2DEngine extends Engine {

    private Canvas component;

    public Java2DEngine(EngineFactory factory, Canvas component) {
        super(factory);
        this.component = component;
        init(component.getWidth(), component.getHeight());
    }

    @Override
    public Matrix createMatrix() {
        return new Java2DMatrix();
    }

    @Override
    public Image createImage(int width, int height) {
        return new Java2DImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    public Image loadImage(String resource) {
        try {
            return new Java2DImage(ImageIO.read(getFactory().getClass().getResource(resource)));
        } catch (IOException e) {
            throw new EngineException("Can't read image " + resource, e);
        }
    }

    public void gameLoop() {

        final BufferStrategy strategy = component.getBufferStrategy();
        final Java2DDrawContext drawContext = new Java2DDrawContext();
        while (true) {
            if (!onUpdate()) {
                return;
            }
            final Graphics2D graphics2D = (Graphics2D)strategy.getDrawGraphics();
            try {
                onDraw(drawContext.wrap(graphics2D));
            } finally {
                graphics2D.dispose();
            }
            strategy.show();
        }
    }

    private void addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action action, KeyEvent keyEvent) {
        scheduleEvent(new ru.spacearena.engine.events.KeyEvent(action, keyEvent.getKeyCode(), keyEvent.getKeyChar()));
    }

    private void addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action action, MouseEvent mouseEvent) {
        scheduleEvent(new ru.spacearena.engine.events.MouseEvent(
                action, mouseEvent.getButton(), mouseEvent.getX(), mouseEvent.getY()));
    }


    public boolean enableInput(InputType inputType) {
        switch (inputType) {
        case KEYBOARD:
            component.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                    addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action.TYPED, e);
                }

                public void keyPressed(KeyEvent e) {
                    addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action.DOWN, e);
                }

                public void keyReleased(KeyEvent e) {
                    addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action.UP, e);
                }
            });
            return true;

        case MOUSE:

            component.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.CLICK, e);
                }

                public void mousePressed(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.DOWN, e);
                }

                public void mouseReleased(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.UP, e);
                }

                public void mouseEntered(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.ENTER, e);
                }

                public void mouseExited(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.LEAVE, e);
                }
            });
            component.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.DRAG, e);
                }

                public void mouseMoved(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.MOVE, e);
                }
            });
            component.addMouseWheelListener(new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    // TODO add wheel support
                    //onInput(mouseEvent.init(e, MouseEvent.Action.WHEEL));
                }
            });
            return true;
        }
        return false;
    }

}
