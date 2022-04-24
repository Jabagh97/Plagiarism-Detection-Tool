package com.company.GUI;

import com.company.HTML.Parser;
import com.company.Packing.FileProcessing;
import com.company.PlagiarismDetection.PlagiarismDetection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainFrame extends JFrame{
    public String fileNames = "";
    public String path ="";
    public PlagiarismDetection moss = new PlagiarismDetection();
    public Parser parser = new Parser();
    String projectPath = System.getProperty("user.dir") + "\\moss.pl";
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
        JButton TestParserButton  = new JButton("Test Parser");
        JButton ProcessFiles = new JButton("Process and Organize Files");
        JButton newLook = new JButton("Design");
        final JLabel label = new JLabel();
        TestParserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                   System.out.println(parser.getFilesLink());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //Moss Action
        RunMossButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try {
                    moss.runMoss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            moss = new PlagiarismDetection();
            }
        });
        //Select Action
        AddFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File[] files = fileChooser.getSelectedFiles();
                    java.io.File f = fileChooser.getSelectedFile();
                    path = f.getPath();
                    for(File file: files){
                        fileNames += file.getName() + " ";
                    }
                    //Coping Moss Script to the files Folder
                    File dir = new File(projectPath);
                    File dirTarget = new File(path.substring(0,path.lastIndexOf('\\')) + "\\moss.pl");
                    try {
                        Files.copy(dir.toPath(),dirTarget.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    moss.setFilesName(fileNames);
                    moss.setPath(path);
                    label.setText("File(s) Selected: " + fileNames);
                }else{
                    label.setText("Nothing Selected");
                }
                fileNames = "";
            }

        });
        ProcessFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File[] files = fileChooser.getSelectedFiles();
                    java.io.File f = fileChooser.getSelectedFile();
                    path = f.getPath();
                    for(File file: files){
                        fileNames += file.getName() + " ";
                    }
                    label.setText("File(s) Selected: " + fileNames);
                }else{
                    label.setText("Nothing Selected");
                }

                try {

                    FileProcessing fileProcessing = new FileProcessing(path);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                fileNames="";
            }
        });
        newLook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(AddFilesButton);
        panel.add(RunMossButton);
        panel.add(TestParserButton);
        panel.add(ProcessFiles);
        panel.add(label);
        panel.add(newLook);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }
    public String getFileNames(){
       return fileNames;
    }
    public String getPath(){return path;}
}

