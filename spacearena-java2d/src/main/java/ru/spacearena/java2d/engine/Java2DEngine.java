package ru.spacearena.java2d.engine;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineException;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.input.*;
import ru.spacearena.java2d.engine.input.Java2DKeyEvent;
import ru.spacearena.java2d.engine.input.Java2DMouseEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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

            final Java2DKeyEvent keyEvent = new Java2DKeyEvent();
            component.addKeyListener(new KeyListener() {
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

            final Java2DMouseEvent mouseEvent = new Java2DMouseEvent();
            component.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.CLICK));
                }

                public void mousePressed(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.DOWN));
                }

                public void mouseReleased(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.UP));
                }

                public void mouseEntered(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.ENTER));
                }

                public void mouseExited(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.LEAVE));
                }
            });
            component.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.DRAG));
                }

                public void mouseMoved(MouseEvent e) {
                    onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.MOVE));
                }
            });
            component.addMouseWheelListener(new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    // TODO add wheel support
                    //onInput(mouseEvent.init(e, ru.spacearena.engine.input.MouseEvent.Action.WHEEL));
                }
            });
            break;
        }
    }
}
