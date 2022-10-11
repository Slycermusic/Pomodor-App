package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class Main extends JFrame{

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

    protected JButton Commencer, Arreter, Ajouter, ajouterTask, retirerTask;
    public void initialize() {
        initializeGui();
        initializeEvents();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void RegisterTask() {
        task = taskList.getText();
        System.out.println(task);
        listTask.add(task);
        System.out.print(listTask);

        listcheckBox.add(new JCheckBox(listTask.get(listTask.size() - 1)));
        currentCheckbox = listcheckBox.get(listcheckBox.size() - 1);
        this.add(currentCheckbox);
        currentCheckbox.setBounds(100, y,200,20);
        currentCheckbox.setBackground(Color.black);
        currentCheckbox.setOpaque(false);
        taskList.setText("");
        y += 20;
    }

    public void RemoveTask() {
        this.remove(1);
    }

    public void initializeGui() {
        if (initialized)
            return;
        initialized = true;
        this.setSize(1080, 720);
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

        //Créer le boutton d'ajout des tâches
        ajouterTask = new JButton("Ajouter Tâche");
        ajouterTask.setBounds(150,280,200,40);
        ajouterTask.addActionListener(actions);
        this.add(ajouterTask);
        this.setLayout(null);

        //Créer le boutton d'ajout des tâches
        retirerTask = new JButton("Retirer Tâche");
        retirerTask.setBounds(260,280,200,40);
        retirerTask.addActionListener(actions);
        this.add(retirerTask);
        this.setLayout(null);

        taskList = new JTextField(128);
        taskList.setBounds(100,200,200,40);
        taskList.setHorizontalAlignment(JLabel.CENTER);
        this.add(taskList);

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
                        try {
                            String[] commandcmd = {"cmd.exe", "/C", "Start", "src\\main\\resources\\commandworkblock.bat"};
                            Process p =  Runtime.getRuntime().exec(commandcmd);
                        } catch (IOException ex) {
                        }
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