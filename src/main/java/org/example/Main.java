package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Main extends JFrame {
    private boolean initialized = false;
    private Actions actions = new Actions();

    public void initialize() {
        initializeGui();
        initializeEvents();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeGui() {
        if (initialized)
            return;
        initialized = true;
        this.setSize(500, 400);
        Dimension windowSize = this.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2);
        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        // TODO: Add UI widgets
    }

    private void initializeEvents() {
        // TODO: Add action listeners, etc
    }

    public class Actions implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            command = command == null ? "" : command;
            // TODO: add if...if else... for action commands

        }
    }

    public void dispose() {
        // TODO: Save settings
        //super.dispose();
        System.exit(0);
    }

    public void setVisible(boolean b) {
        initialize();
        super.setVisible(b);
    }


    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}