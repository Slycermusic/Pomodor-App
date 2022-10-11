package org.example;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Position;
import java.applet.*;
import java.io.File;


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

    public static void playSound() throws InterruptedException {
        Thread.sleep(200);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\maxen\\IdeaProjects\\Pomodor-App\\src\\main\\resources\\audio\\Notif.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Main().setVisible(true);

        Thread newThread = new Thread(() -> {
            try {
                playSound();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        newThread.start();

        Notify.create()
                .title("POMODOR'APP")
                .text("Votre temps est presque écoulé, il ne vous reste que 2 minutes !")
                .hideAfter(10000)
                .position(Pos.CENTER)
                .show();


    }
}