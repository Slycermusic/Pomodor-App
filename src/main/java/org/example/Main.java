package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

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

    static Main window;

    boolean isWork = true;

    protected JButton Commencer, Arreter, Ajouter;
    public void initialize() {
        initializeGui();
        initializeEvents();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                String ObjButtons[] = {"Oui","Non"};
                int PromptResult = JOptionPane.showOptionDialog(null,"Attention, fermer l'application n'enregistrera pas le chronomètre. Voulez vous toujours fermer l'application ?","Pomorod'App",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });    }

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

                    Thread newThread = new Thread(() -> {
                        try {
                            playSound();
                            Thread.sleep(200);
                            displayNotif();
                        } catch (InterruptedException | AWTException f) {
                            throw new RuntimeException(f);
                        }
                    });
                    newThread.start();


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

    public void displayNotif() throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            //If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);

            trayIcon.displayMessage("Pomodor'App", "Deux minutes restantes, Clickez pour ajouter deux minutes", TrayIcon.MessageType.INFO);
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    minute += 2;
                }
            });
        } else {
            System.err.println("System tray not supported!");
        }
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
                Arreter.setEnabled(false);
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
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src\\main\\resources\\audio\\Notif.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, AWTException {
        new Main().setVisible(true);
    }
}