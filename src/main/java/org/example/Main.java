package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.Timer;
import java.awt.Image;
import java.awt.Toolkit;

import static javax.swing.JLayeredPane.*;

public class Main extends JFrame {
    private boolean initialized = false;
    private Actions actions = new Actions();

    ArrayList<String> listTask = new ArrayList<String>();
    ArrayList<JCheckBox> listcheckBox = new ArrayList<JCheckBox>();
    JCheckBox currentCheckbox;

    String task;
    JLabel counterLabel;
    JTextField taskList;
    Timer timer;
    int second, minute, y = 50;
    String ddSecond, ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");
    Font Timer = new Font("Serif", Font.PLAIN, 175);
    Font Trash = new Font("Serif", Font.PLAIN, 22);
    Font Task = new Font("Serif", Font.PLAIN, 18);
    boolean isWork = true;
    boolean addedTime = false;
    int nbBoucles = 1;

    protected JButton Commencer, Arreter, Ajouter, ajouterTask, retirerTask, ajouterTemps;
    protected JLabel messageErreur;

    JLayeredPane layeredPane = new JLayeredPane();

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

        this.setSize(1280, 720);
        Dimension windowSize = this.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2);
        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        setResizable(false);
        pane.setBackground(Color.decode("#1C1920"));

        JPanel middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setSize(20,720);
        middlePanel.setLocation(630, 0);
        middlePanel.setBackground(Color.decode("#230804"));
        this.add(middlePanel);

        //Créer le boutton Commencer
        Commencer = new JButton("Commencer");
        Commencer.setBounds(150,400,125,40);
        Commencer.addActionListener(actions);
        this.add(Commencer);

        //Créer le boutton Arreter
        Arreter = new JButton("Arrêter");
        Arreter.setBounds(350,400,125,40);
        Arreter.addActionListener(actions);
        Arreter.setEnabled(false);
        this.add(Arreter);

        //Créer le boutton Ajout de deux minutes
        ajouterTemps = new JButton("Ajouter deux minutes");
        ajouterTemps.setBounds(230,470,160,40);
        ajouterTemps.addActionListener(actions);
        ajouterTemps.setEnabled(false);
        this.add(ajouterTemps);


        //Créer le boutton d'ajout des tâches
        ajouterTask = new JButton("Ajouter Tâche");
        ajouterTask.setBounds(1075,620,115,40);
        ajouterTask.addActionListener(actions);
        this.add(ajouterTask);

        //Créer le boutton d'ajout des tâches
        retirerTask = new JButton("\uD83D\uDDD1");
        retirerTask.setBounds(1200,620,50,40);
        retirerTask.setFont(Trash);
        retirerTask.addActionListener(actions);
        this.add(retirerTask);

        //Créer le boutton d'ajout des tâches
        messageErreur = new JLabel("Cette tâche existe déjà.");
        messageErreur.setBounds(690,590,150,40);
        messageErreur.setForeground(Color.red);
        messageErreur.setVisible(false);
        this.add(messageErreur);

        taskList = new JTextField(32);
        taskList.setBounds(665,620,400,40);
        taskList.setHorizontalAlignment(JLabel.CENTER);
        this.add(taskList);

        counterLabel = new JLabel("this is sample");
        counterLabel.setBounds(85,200,450,200);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        counterLabel.setForeground(Color.WHITE);
        counterLabel.setFont(Timer);
        this.add(counterLabel);

        counterLabel.setText("25:00");
        second = 0;
        minute = 25;

        this.setLayout(null);

        ImageIcon img = new ImageIcon("src\\main\\resources\\images\\Background.png");
        JLabel background;
        background = new JLabel("", img, JLabel.CENTER);
        background.setBounds(0,0,640,720);
        this.add(background);
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
                    addedTime = true;
                    if (isWork) {
                        pauseTimer();
                    } else {
                        resetTimer();
                    }
                 }
                if(minute == 2 & second == 0 && isWork) {
                    Arreter.setEnabled(true);
                    if(!addedTime){
                        ajouterTemps.setEnabled(true);
                    }

                    Thread newThread = new Thread(() -> {
                        try {
                            playSound();
                            Thread.sleep(200);
                            displayNotif(addedTime ? "Deux minutes restantes" : "Deux minutes restantes, Clickez pour ajouter deux minutes");
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
        if(nbBoucles % 4 == 0){
            minute = 30;
            second = 0;
            counterLabel.setText("30:00");
        }else{
            minute = 5;
            second = 0;
            counterLabel.setText("05:00");
        }
        ajouterTemps.setEnabled(false);
        Arreter.setEnabled(false);
        isWork = false;
    }

    public void displayNotif(String texte) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            //If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("src\\main\\resources\\images\\pomodorapp_logo.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
            TrayIcon trayIcon = new TrayIcon(image, "Pomodorapp logo");
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);

            trayIcon.displayMessage("Pomodor'App", texte, TrayIcon.MessageType.INFO);
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!addedTime) {
                        minute += 2;
                        ajouterTemps.setEnabled(false);
                        addedTime = true;
                    }
                }
            });
        } else {
            System.err.println("System tray not supported!");
        }
    }
    public void resetTimer() {
        minute = 25;
        second = 00;
        counterLabel.setText("25:00");
        Commencer.setEnabled(true);
        Arreter.setEnabled(false);
        timer.stop();
        isWork = true;
        addedTime = false;
        Thread newThread = new Thread(() -> {
            try {
                playSound();
                Thread.sleep(200);
                displayNotif("Fin du Pomodoro n°" + nbBoucles + ". Vous pouvez relancer le suivant après avoir ajouté vos nouvelles tâches.");
                nbBoucles++;
            } catch (InterruptedException | AWTException f) {
                throw new RuntimeException(f);
            }
        });
        newThread.start();
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
                try {
                    String[] commandcmd = {"cmd.exe", "/C", "Start", "src\\main\\resources\\commandbreakblock.bat"};
                    Process p =  Runtime.getRuntime().exec(commandcmd);
                } catch (IOException ex) {
                }
            }
            if (e.getActionCommand().equals("Arrêter")) {
                pauseTimer();
            }
            if (e.getActionCommand().equals("Ajouter Tâche")) {
                if(taskList.getText() == null) {
                    ajouterTask.setEnabled(false);
                } else if (taskList.getText() != null){
                    ajouterTask.setEnabled(true);
                }
                RegisterTask();
            }
            if (e.getActionCommand().equals("Retirer Tâche")) {
                if (currentCheckbox.isSelected()) {
                    RemoveTask();
                }

            }
            if (e.getActionCommand().equals("Ajouter deux minutes") && !addedTime) {
                    minute += 2;
                    ajouterTemps.setEnabled(false);
                    addedTime = true;
            }

        }
    }

    public void RegisterTask() {
        task = taskList.getText();
        if(!Objects.equals(task, "") && !listTask.contains(task)){
            messageErreur.setVisible(false);

            System.out.println(task);
            listTask.add(task);
            System.out.print(listTask);

            listcheckBox.add(new JCheckBox(listTask.get(listTask.size() - 1)));
            currentCheckbox = listcheckBox.get(listcheckBox.size() - 1);
            this.add(currentCheckbox);
            currentCheckbox.setBounds(675, 25,400,40);
            currentCheckbox.setForeground(Color.WHITE);
            currentCheckbox.setOpaque(false);
            currentCheckbox.setFont(Task);
            currentCheckbox.setIcon(new ImageIcon("C:\\Users\\Slycer\\Documents\\Pomodor'App\\untitled\\src\\main\\resources\\images\\isNotSelectedIcon.png"));
            currentCheckbox.setSelectedIcon(new ImageIcon("C:\\Users\\Slycer\\Documents\\Pomodor'App\\untitled\\src\\main\\resources\\images\\isSelectedIcon.png"));
            taskList.setText("");
            y += 50;
        }else{
            messageErreur.setVisible(true);
        }
        taskList.setText("");

    }

    public void RemoveTask() {
            this.remove(currentCheckbox);
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

    public static void main(String[] args) {
        new Main().setVisible(true);

    }
}