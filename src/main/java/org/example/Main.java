package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.Image;
import java.awt.Toolkit;

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
    boolean isWork = true;

    JLayeredPane layeredPane = new JLayeredPane();
    protected JButton Commencer, Arreter, ajouterTask, retirerTask, ajoutTemps;

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
        ajoutTemps = new JButton("Ajouter deux minutes");
        ajoutTemps.setBounds(230,470,160,40);
        ajoutTemps.addActionListener(actions);
        ajoutTemps.setEnabled(false);
        this.add(ajoutTemps);


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

        JPanel addtaskPanel = new JPanel(new GridBagLayout());
        addtaskPanel.setSize(640,81);
        addtaskPanel.setLocation(640, 600);
        addtaskPanel.setBackground(Color.decode("#1C1920"));
        this.add(addtaskPanel);

        ImageIcon imgtask = new ImageIcon("C:\\Users\\Slycer\\Documents\\Pomodor'App\\untitled\\src\\main\\resources\\images\\TaskPanel.png");
        JLabel backgroundtask;
        backgroundtask = new JLabel("", imgtask, JLabel.CENTER);
        backgroundtask.setBounds(320,-59,1280,720);
        this.add(backgroundtask);

        ImageIcon img = new ImageIcon("C:\\Users\\Slycer\\Documents\\Pomodor'App\\untitled\\src\\main\\resources\\images\\Background.png");
        JLabel background;
        background = new JLabel("", img, JLabel.CENTER);
        background.setBounds(0,0,1280,720);
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
        second = 0;
        minute = 5;
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
            if (e.getActionCommand().equals("Ajouter deux minutes")) {
                    minute += 2;

            }

        }
    }

    public void RegisterTask() {
        task = taskList.getText();
        System.out.println(task);
        listTask.add(task);
        System.out.print(listTask);

        listcheckBox.add(new JCheckBox(listTask.get(listTask.size() - 1)));
        currentCheckbox = listcheckBox.get(listcheckBox.size() - 1);
        this.add(currentCheckbox);
        currentCheckbox.setBounds(1000, y,200,20);
        currentCheckbox.setBackground(Color.black);
        currentCheckbox.setOpaque(false);
        taskList.setText("");
        y += 50;

        layeredPane.add(currentCheckbox, JLayeredPane.DRAG_LAYER);
    }

    public void RemoveTask() {
        if(currentCheckbox.isSelected()) {
            this.remove(currentCheckbox);
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

    public static void main(String[] args) {
        new Main().setVisible(true);

    }
}