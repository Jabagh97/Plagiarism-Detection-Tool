package com.company.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame{
    String fileNames = " ";
   public void createWindow() {
        JFrame frame = new JFrame("Select Files");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void createUI(final JFrame frame){
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);
        JButton AddFilesButton = new JButton("Add Files");
        JButton RunMossButton  = new JButton("Run Moss");
        final JLabel label = new JLabel();
        //Moss Action
        RunMossButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MossResults mossResults = new MossResults();
            }
        });

        //Select Action
        AddFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File[] files = fileChooser.getSelectedFiles();
                    for(File file: files){
                        fileNames += file.getName() + " ";
                    }
                    label.setText("File(s) Selected: " + fileNames);
                }else{
                    label.setText("Nothing Selected");
                }
            }
        });

        panel.add(AddFilesButton);
        panel.add(RunMossButton);
        panel.add(label);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public String getFileNames(){
       return fileNames;
    }
}

//TODO: Organize, make it more Professional, and add Zip file feature