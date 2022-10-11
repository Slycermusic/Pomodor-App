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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;

public class Main extends JFrame {
    private boolean initialized = false;
    private Actions actions = new Actions();

    JLabel counterLabel;
    Timer timer;
    int second, minute;
    String ddSecond, ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");
    Font font = new Font("Arial", Font.PLAIN, 70);

    boolean isWork = true;

    protected JButton Commencer, Arreter, Ajouter;
    public void initialize() {
        initializeGui();
        initializeEvents();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void initializeGui() {
        if (initialized)
            return;
        initialized = true;
        this.setSize(500, 400);
        Dimension windowSize = this.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2);
        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());

        //Créer le boutton Commencer
        Commencer = new JButton("Commencer");
        Commencer.setBounds(100,100,200,40);
        Commencer.addActionListener(actions);
        this.add(Commencer);

        //Créer le boutton Commencer
        Arreter = new JButton("Arrêter");
        Arreter.setBounds(100,180,200,40);
        Arreter.addActionListener(actions);
        Arreter.setEnabled(false);
        this.add(Arreter);
        this.setLayout(null);

        counterLabel = new JLabel("this is sample");
        counterLabel.setBounds(300,230,200,100);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        counterLabel.setFont(font);

        this.add(counterLabel);

        counterLabel.setText("2:10");
        second = 10;
        minute = 2;



    }

    public void countdownTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                second--;
                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);
                counterLabel.setText(ddMinute + ":" + ddSecond);

                if(second==-1) {
                    second = 59;
                    minute--;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    counterLabel.setText(ddMinute + ":" + ddSecond);
                }
                if(minute==0 && second==0) {
                    if (isWork) {
                        pauseTimer();

                    } else {
                        resetTimer();

                    }
                 }
                if(minute == 2 & second == 0) {
                    Arreter.setEnabled(true);
                }
            }
        });
    }

    public void pauseTimer() {
        minute = 0;
        second = 10;
        counterLabel.setText("05:00");
        isWork = false;
    }
    public void resetTimer() {
        minute = 25;
        second = 0;
        counterLabel.setText("25:00");
        Commencer.setEnabled(true);
        Arreter.setEnabled(false);
        timer.stop();
        isWork = true;
    }

    private void initializeEvents() {
        // TODO: Add action listeners, etc
    }

    public class Actions implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(e.getActionCommand().equals("Commencer")) {
                countdownTimer();
                timer.start();
                Commencer.setEnabled(false);
            }
            if (e.getActionCommand().equals("Arrêter")) {
                pauseTimer();
            }
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