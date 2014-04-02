package ru.spacearena.jogl.engine;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class JoglEngine extends Engine {

    private final GLWindow window;

    public JoglEngine(EngineFactory factory, GLWindow window) {
        super(factory);
        this.window = window;
        init();
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
            window.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action.DOWN, e);
                }
                public void keyReleased(KeyEvent e) {
                    addKeyEvent(ru.spacearena.engine.events.KeyEvent.Action.UP, e);
                }
            });
            return true;
        case MOUSE:
            window.addMouseListener(new MouseListener() {
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

                public void mouseDragged(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.DRAG, e);
                }

                public void mouseMoved(MouseEvent e) {
                    addMouseEvent(ru.spacearena.engine.events.MouseEvent.Action.MOVE, e);
                }

                public void mouseWheelMoved(MouseEvent e) {
                    // TODO add wheel support
                    //onInput(mouseEvent.init(e, MouseEvent.Action.WHEEL));
                }
            });
            return true;
        }
        return false;
    }

    public GLWindow getWindow() {
        return window;
    }
}
