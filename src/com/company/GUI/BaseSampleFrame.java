package com.company.GUI;
/*
 * JTattoo BaseSampleFrame (c) 2013 by MH Software-Entwicklung
 *
 * A base frame class for most of the sample applications.
 */

import com.company.HTML.Parser;
import com.company.Packing.FileProcessing;
import com.company.PlagiarismDetection.PlagiarismDetection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;


public class BaseSampleFrame extends JFrame {

    public String fileNames = "";
    public String path ="";
    public PlagiarismDetection moss = new PlagiarismDetection();
    public Parser parser = new Parser();
    String projectPath = System.getProperty("user.dir") + "\\moss.pl";

    // A list of possible look and feels
    protected JList lafList = null;
    protected int selectedLaf = 0;
    protected ListSelectionListener lafListener = null;

    protected JPanel contentPanel = null;
    protected JSplitPane contentSplitPane = null;
    protected JPanel contentLayoutPanel = null;

    public BaseSampleFrame(String title) {
        super(title);

        // Setup menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic('N');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...");
        menuItem.setMnemonic('O');
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (fc.showOpenDialog(BaseSampleFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File[] files = fc.getSelectedFiles();
                    java.io.File f = fc.getSelectedFile();
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
                    JOptionPane.showMessageDialog(BaseSampleFrame.this, "Your selection: " + fc.getSelectedFile().getName());
                    fileNames = "";
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic('S');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Save as");
        menuItem.setMnemonic('a');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic('x');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        // Setup widgets
        // Create a list with all look and feels we want to test
        lafList = new JList(Constants.LAF_NAMES);
        lafList.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        lafList.setSelectedIndex(0);
        lafList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lafListener = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (lafList.getSelectedIndex() != -1) {
                        if (selectedLaf != lafList.getSelectedIndex()) {
                            selectedLaf = lafList.getSelectedIndex();
                            // We change the look and feel after all pending events are dispatched,
                            // otherwise there will be some serious redrawing problems.
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setLookAndFeel();
                                }
                            });
                        }
                    } else {
                        // We don't want the list to be unselected, so if user unselects the list
                        // we do select the last selected entry
                        lafList.setSelectedIndex(selectedLaf);
                    }
                }
            }
        };
        lafList.addListSelectionListener(lafListener);
        JScrollPane lafScrollPane = new JScrollPane(lafList);
        lafScrollPane.setBorder(new TitleBorder("Projects"));
        lafScrollPane.setMinimumSize(new Dimension(120, 80));

        contentPanel = new JPanel(new BorderLayout());
        contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, lafScrollPane, contentPanel);
        contentSplitPane.setDividerLocation(140);

        contentLayoutPanel = new JPanel(new BorderLayout());
        contentLayoutPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        contentLayoutPanel.add(contentSplitPane, BorderLayout.CENTER);

        setContentPane(contentLayoutPanel);

        // Add listeners
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    } // end CTor
    public Properties getLAFProps() {
        return new Properties();
    }
    public void setLookAndFeel() {
        try {
            Properties props = getLAFProps();
            switch (selectedLaf) {
                case Constants.SELECT_FILES:
                    break;
                case Constants.ORGANIZE :

                    break;
            }
            // Tell all components that look and feel has changed.
            Window windows[] = Window.getWindows();
            for (Window window : windows) {
                if (window.isDisplayable()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
            // Maybe selected item is not visible after changing the look and feel so we correct this
            scrollSelectedToVisible(lafList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end setLookAndFeel

    public void scrollSelectedToVisible(JList list) {
        // Because of the different font size the selected item
        // maybe out of the visible area. So we correct this.
        int idx = list.getLeadSelectionIndex();
        Rectangle rect = list.getCellBounds(idx, idx);
        if (rect != null) {
            list.scrollRectToVisible(rect);
        }
    } // end scrollSelectedToVisible

    public class ScrollPaneSamplePanel extends javax.swing.JPanel {

        public ScrollPaneSamplePanel() {
            initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
         * content of this method is always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {
            languages = new JLabel();
            langSelect = new JComboBox();
            optionM = new JLabel();
            mTextField = new JTextField();
            optionN = new JLabel();
            nTextField = new JTextField();
            optionC = new JLabel();
            cTextField = new JTextField();
            pairs = new JLabel();
            // jTextField4 = new javax.swing.JTextField();
            jScrollPane1 = new JScrollPane();
            jTable1 = new JTable();
            organize = new JButton();
            runMoss = new JButton();
            clear = new JButton();
            languages.setText("Languages:");
            langSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C++", "Java"}));
            optionM.setText("Option M");
            optionN.setText("Option N");
            optionC.setText("Option C");
            pairs.setText("Results");
            jScrollPane1.setViewportView(jTable1);
            organize.setText("Process Zip File");
            organize.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setMultiSelectionEnabled(true);
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    // Create a filter so that we only see .zip files
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "zip");
                    fileChooser.setFileFilter(filter);
                    if (fileChooser.showOpenDialog(BaseSampleFrame.this) == JFileChooser.APPROVE_OPTION){
                        File[] files = fileChooser.getSelectedFiles();
                        java.io.File f = fileChooser.getSelectedFile();
                        path = f.getPath();
                        for(File file: files){
                            fileNames += file.getName() + " ";
                        }
                        try {
                            FileProcessing fileProcessing = new FileProcessing(path);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    fileNames="";
                }
            });
            runMoss.setText("Run MOSS");
            runMoss.addActionListener(new ActionListener() {
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
            clear.setText("Clear");
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(jScrollPane1,GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                            .addComponent(languages)
                                                            .addComponent(optionM)
                                                            .addComponent(optionC)
                                                            .addComponent(pairs))
                                                    .addGap(24, 24, 24)
                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(cTextField, GroupLayout.Alignment.LEADING)
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                            .addComponent(langSelect, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                            .addComponent(mTextField))
                                                                    .addGap(18, 18, 18)
                                                                    .addComponent(optionN)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(nTextField))
                                                    )
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(organize, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(runMoss, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(clear,GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))))
                                    .addContainerGap())
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(languages)
                                            .addComponent(langSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(organize))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(optionM)
                                            .addComponent(mTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(optionN)
                                            .addComponent(nTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(runMoss))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(optionC)
                                            .addComponent(cTextField, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clear))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(pairs)
                                    )
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane1,GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                    .addContainerGap())
            );
        }// </editor-fold>//GEN-END:initComponents


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private JButton organize;
        private JButton runMoss;
        private JButton clear;
        private JComboBox langSelect;
        private JLabel languages;
        private JLabel optionM;
        private JLabel optionN;
        private JLabel optionC;
        private JLabel pairs;
        private JScrollPane jScrollPane1;
        private JTable jTable1;
        private JTextField mTextField;
        private JTextField nTextField;
        private JTextField cTextField;
        private JTextField jTextField4;
        // End of variables declaration//GEN-END:variables
    }

}