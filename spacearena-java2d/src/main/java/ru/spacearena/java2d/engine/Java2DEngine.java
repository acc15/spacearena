package ru.spacearena.java2d.engine;

import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineException;
import ru.spacearena.android.engine.EngineFactory;
import ru.spacearena.android.engine.graphics.Image;
import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
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
        this.width = component.getWidth();
        this.height = component.getHeight();
        init();
    }

    @Override
    public Matrix createMatrix() {
        return new Java2DMatrix();
    }

    public Image loadImage(String resource) {
        try {
            return new Java2DImage(ImageIO.read(factory.getClass().getResource(resource)));
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
            Thread.yield();
        }
    }

    private void addKeyEvent(ru.spacearena.android.engine.input.KeyEvent.Action action, KeyEvent keyEvent) {
        onInput(new ru.spacearena.android.engine.input.KeyEvent(action, keyEvent.getKeyCode(), keyEvent.getKeyChar()));
    }

    private void addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action action, MouseEvent mouseEvent) {
        onInput(new ru.spacearena.android.engine.input.MouseEvent(
                action, mouseEvent.getButton(), mouseEvent.getX(), mouseEvent.getY()));
    }


    public void enableInput(InputType inputType) {
        switch (inputType) {
        case KEYBOARD:
            component.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                    addKeyEvent(ru.spacearena.android.engine.input.KeyEvent.Action.TYPED, e);
                }

                public void keyPressed(KeyEvent e) {
                    addKeyEvent(ru.spacearena.android.engine.input.KeyEvent.Action.DOWN, e);
                }

                public void keyReleased(KeyEvent e) {
                    addKeyEvent(ru.spacearena.android.engine.input.KeyEvent.Action.UP, e);
                }
            });
            break;

        case MOUSE:

            component.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.CLICK, e);
                }

                public void mousePressed(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.DOWN, e);
                }

                public void mouseReleased(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.UP, e);
                }

                public void mouseEntered(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.ENTER, e);
                }

                public void mouseExited(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.LEAVE, e);
                }
            });
            component.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.DRAG, e);
                }

                public void mouseMoved(MouseEvent e) {
                    addMouseEvent(ru.spacearena.android.engine.input.MouseEvent.Action.MOVE, e);
                }
            });
            component.addMouseWheelListener(new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    // TODO add wheel support
                    //onInput(mouseEvent.init(e, ru.spacearena.android.engine.input.MouseEvent.Action.WHEEL));
                }
            });
            break;
        }
    }
}
