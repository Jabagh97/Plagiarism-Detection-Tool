package com.company.GUI;

import javax.swing.*;
import java.awt.*;


public class MainFrameV2 extends BaseSampleFrame {

    public MainFrameV2() {
        super("Plagiarism Detection Tool");
        // Setup the controls
        ScrollPaneSamplePanel samplePanel = new ScrollPaneSamplePanel();
        // Add the textarea panel to a scrollpane
        JScrollPane scrollPane = new JScrollPane(samplePanel);
        // Set samplePanel, scrollPane and the associated viewport to none opaque to avoid painting problems.
        samplePanel.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

    }

    //------------------------------------------------------------------------------
    public static void main(String[] args) {
//------------------------------------------------------------------------------
        try {
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // Start the application
                    MainFrameV2 app = new MainFrameV2();
                    app.setSize(1000, 800);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main

}

