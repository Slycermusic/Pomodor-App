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
    Font font = new Font("Serif", Font.PLAIN, 70);
    Font Trash = new Font("Serif", Font.PLAIN, 40);
    boolean isWork = true;
    boolean addedTime = false;
    int nbBoucles = 1;

    protected JButton Commencer, Arreter, Ajouter, ajouterTask, retirerTask, ajouterTemps;
    protected JLabel messageErreur;
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

        ImageIcon icon = new ImageIcon("src/main/resources/images/pomodorapp_logo.png");
        this.setIconImage(icon.getImage());

        this.setSize(1080, 720);
        Dimension windowSize = this.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2);
        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());

        JPanel TaskPanel = new JPanel();
        TaskPanel.setBounds(780,0,300,200);
        TaskPanel.setBackground(Color.decode("#3d4042"));
        this.add(TaskPanel);

        //Créer le boutton Commencer
        Commencer = new JButton("Commencer");
        Commencer.setBounds(175,350,200,40);
        Commencer.addActionListener(actions);
        this.add(Commencer);

        //Créer le boutton Arreter
        Arreter = new JButton("Arrêter");
        Arreter.setBounds(300,400,200,40);
        Arreter.addActionListener(actions);
        Arreter.setEnabled(false);
        this.add(Arreter);
        this.setLayout(null);

        //Créer le boutton Ajout de deux minutes
        ajouterTemps = new JButton("Ajouter deux minutes");
        ajouterTemps.setBounds(50,400,200,40);
        ajouterTemps.addActionListener(actions);
        ajouterTemps.setEnabled(false);
        this.add(ajouterTemps);
        this.setLayout(null);


        //Créer le boutton d'ajout des tâches
        ajouterTask = new JButton("Ajouter Tâche");
        ajouterTask.setBounds(900,500,150,40);
        ajouterTask.addActionListener(actions);
        this.add(ajouterTask);
        this.setLayout(null);

        //Créer le boutton d'ajout des tâches
        retirerTask = new JButton("Retirer Tâche");
        retirerTask.setBounds(900,650,150,40);
        retirerTask.addActionListener(actions);
        this.add(retirerTask);
        this.setLayout(null);

        messageErreur = new JLabel("Cette tâche existe déjà.");
        messageErreur.setBounds(715,450,150,40);
        messageErreur.setVisible(false);
        messageErreur.setForeground(Color.RED);
        this.add(messageErreur);
        this.setLayout(null);

        taskList = new JTextField(128);
        taskList.setBounds(600,500,200,40);
        taskList.setHorizontalAlignment(JLabel.CENTER);
        this.add(taskList);

        counterLabel = new JLabel("this is sample");
        counterLabel.setBounds(175,200,200,100);
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
            minute = 2;
            second = 30;
            counterLabel.setText("30:00");
        }else{
            minute = 2;
            second = 10;
            counterLabel.setText("05:00");
        }
        ajouterTemps.setEnabled(false);
        Arreter.setEnabled(false);
        isWork = false;
        addedTime = true;
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
        minute = 2;
        second = 10;
        counterLabel.setText("02:10");
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
            currentCheckbox.setBounds(100, y,200,20);
            currentCheckbox.setBackground(Color.black);
            currentCheckbox.setOpaque(false);
            y += 20;

        }else{
            messageErreur.setVisible(true);
        }
        taskList.setText("");
    }

    public void RemoveTask() {
        this.remove(1);
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