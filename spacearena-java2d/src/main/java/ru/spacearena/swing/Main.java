package ru.spacearena.swing;

import ru.spacearena.engine.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-30-01
 */
public class Main extends JComponent {

    private Engine engine;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }



    public static void main(String[] args) {
        final JFrame frame = new JFrame("Space Arena");
        frame.add(new Main());
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
