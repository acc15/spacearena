package ru.spacearena.java2d.engine;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineException;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.input.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class Java2DEngine extends Engine {

    private Component component;

    public Java2DEngine(EngineFactory factory, Component component) {
        super(factory);
        this.component = component;
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

    @Override
    public float getWidth() {
        return component.getWidth();
    }

    @Override
    public float getHeight() {
        return component.getHeight();
    }

    public void enableInput(InputType inputType) {
        switch (inputType) {
        case KEYBOARD:

            component.addKeyListener(new KeyListener() {
                final Java2DKeyEvent keyEvent = new Java2DKeyEvent();

                public void keyTyped(KeyEvent e) {
                    onInput(keyEvent.init(e, ru.spacearena.engine.input.KeyEvent.Action.TYPED));
                }

                public void keyPressed(KeyEvent e) {
                    onInput(keyEvent.init(e, ru.spacearena.engine.input.KeyEvent.Action.DOWN));
                }

                public void keyReleased(KeyEvent e) {
                    onInput(keyEvent.init(e, ru.spacearena.engine.input.KeyEvent.Action.UP));
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
}
