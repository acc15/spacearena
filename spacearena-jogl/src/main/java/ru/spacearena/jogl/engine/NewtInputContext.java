package ru.spacearena.jogl.engine;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.events.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class NewtInputContext implements InputContext {

    private final Window window;

    public NewtInputContext(Window window) {
        this.window = window;
    }

    private void addKeyEvent(Engine engine,
                             ru.spacearena.engine.events.KeyEvent.Action action, KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        engine.scheduleEvent(
                new ru.spacearena.engine.events.KeyEvent(action, keyEvent.getKeyCode(), keyEvent.getKeyChar()));
    }

    private void addMouseEvent(Engine engine,
                               ru.spacearena.engine.events.MouseEvent.Action action, MouseEvent mouseEvent) {
        if (mouseEvent.isAutoRepeat()) {
            return;
        }
        engine.scheduleEvent(new ru.spacearena.engine.events.MouseEvent(
                action, mouseEvent.getButton(), mouseEvent.getX(), mouseEvent.getY()));
    }

    public boolean enableInput(final Engine engine, final InputType inputType) {
        switch (inputType) {
        case KEYBOARD:
            window.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    addKeyEvent(engine, ru.spacearena.engine.events.KeyEvent.Action.DOWN, e);
                }
                public void keyReleased(KeyEvent e) {
                    addKeyEvent(engine, ru.spacearena.engine.events.KeyEvent.Action.UP, e);
                }
            });
            return true;
        case MOUSE:
            window.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.CLICK, e);
                }

                public void mousePressed(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.DOWN, e);
                }

                public void mouseReleased(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.UP, e);
                }

                public void mouseEntered(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.ENTER, e);
                }

                public void mouseExited(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.LEAVE, e);
                }

                public void mouseDragged(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.DRAG, e);
                }

                public void mouseMoved(MouseEvent e) {
                    addMouseEvent(engine, ru.spacearena.engine.events.MouseEvent.Action.MOVE, e);
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

}
