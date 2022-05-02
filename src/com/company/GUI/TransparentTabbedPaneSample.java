package com.company.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Michael Hagen
 */
public class TransparentTabbedPaneSample extends BaseSampleFrame {

    private JCheckBox opaqueCheck = null;
    private JCheckBox contentOpaqueCheck = null;
    private JCheckBox tabsOpaqueCheck = null;
    private JPanel tabPanel = null;
    private JTabbedPane tabbedPane = null;
    private JPanel backgroundPanel = null;

    public TransparentTabbedPaneSample() {
        super("TransparentTabbedPaneSample");

        // Create some controls to test different state of tabbed panes
        opaqueCheck = new JCheckBox("TabbedPane.opaque", false);
        opaqueCheck.setOpaque(false);
        opaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setOpaque(!tabbedPane.isOpaque());
                contentOpaqueCheck.setEnabled(!opaqueCheck.isSelected());
                tabsOpaqueCheck.setEnabled(!opaqueCheck.isSelected());
                updateTabbedPane();
            }
        });
        contentOpaqueCheck = new JCheckBox("TabbedPane.contentOpaque", true);
        contentOpaqueCheck.setOpaque(false);
        contentOpaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Here we toggle the UIManager property "TabbedPane.contentOpaque"
                // This property, if set to false, in combination with the opaque flag, if also set to false, of the tabbed pane
                // causes the control to be transparent.
                boolean opaque = true;
                if (UIManager.get("TabbedPane.contentOpaque") != null) {
                    opaque = UIManager.getBoolean("TabbedPane.contentOpaque");
                }
                UIManager.put("TabbedPane.contentOpaque", !opaque);
                updateTabbedPane();
            }
        });

        tabsOpaqueCheck = new JCheckBox("TabbedPane.tabsOpaque", true);
        tabsOpaqueCheck.setOpaque(false);
        tabsOpaqueCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Here we toggle the UIManager property "TabbedPane.tabsOpaque"
                // If this property is set to false the single tabs will be transparent.
                boolean opaque = true;
                if (UIManager.get("TabbedPane.tabsOpaque") != null) {
                    opaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
                }
                UIManager.put("TabbedPane.tabsOpaque", !opaque);
                updateTabbedPane();
            }
        });
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setOpaque(false);
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
        checkBoxPanel.add(opaqueCheck);
        checkBoxPanel.add(contentOpaqueCheck);
        checkBoxPanel.add(tabsOpaqueCheck);

        JPanel optionPanel = new JPanel();
        optionPanel.setOpaque(false);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        final JRadioButton topCheck = new JRadioButton("Top", true);
        topCheck.setOpaque(false);
        final JRadioButton leftCheck = new JRadioButton("Left", false);
        leftCheck.setOpaque(false);
        final JRadioButton bottomCheck = new JRadioButton("Bottom", false);
        bottomCheck.setOpaque(false);
        final JRadioButton rightCheck = new JRadioButton("Right", false);
        rightCheck.setOpaque(false);
        final JCheckBox scrollableCheck = new JCheckBox("ScrollableTabs", false);
        scrollableCheck.setOpaque(false);
        scrollableCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (scrollableCheck.isSelected()) {
                    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                } else {
                    tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                }
                updateTabbedPane();
            }
        });
        optionPanel.add(topCheck);
        optionPanel.add(leftCheck);
        optionPanel.add(bottomCheck);
        optionPanel.add(rightCheck);
        optionPanel.add(scrollableCheck);
        ButtonGroup orientatioGroup = new ButtonGroup();
        orientatioGroup.add(topCheck);
        orientatioGroup.add(leftCheck);
        orientatioGroup.add(bottomCheck);
        orientatioGroup.add(rightCheck);
        ActionListener orientationListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (topCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.TOP);
                } else if (leftCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.LEFT);
                } else if (bottomCheck.isSelected()) {
                    tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
                } else {
                    tabbedPane.setTabPlacement(JTabbedPane.RIGHT);
                }
                updateTabbedPane();
            }
        };
        topCheck.addActionListener(orientationListener);
        leftCheck.addActionListener(orientationListener);
        bottomCheck.addActionListener(orientationListener);
        rightCheck.addActionListener(orientationListener);

        // Create the sample tabbed pane
        tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        tabbedPane = new JTabbedPane();
        // Create some tabs with different properties
        JPanel panelTransparent = new JPanel();
        panelTransparent.setOpaque(false);
        tabbedPane.add("Transparent", panelTransparent);
        JPanel yellowPanel = new JPanel();
        yellowPanel.setBackground(Color.yellow);
        tabbedPane.add("Yellow", yellowPanel);
        tabbedPane.setBackgroundAt(1, Color.yellow);
        tabbedPane.setForegroundAt(1, Color.red);
        JPanel greenPanel = new JPanel();
        greenPanel.setBackground(Color.green);
        tabbedPane.add("Green", greenPanel);
        tabbedPane.setBackgroundAt(2, Color.green);
        tabbedPane.setForegroundAt(2, Color.blue);
        JPanel bluePanel = new JPanel();
        bluePanel.setBackground(Color.blue);
        tabbedPane.add("Blue", bluePanel);
        tabbedPane.setBackgroundAt(3, Color.blue);
        tabbedPane.setForegroundAt(3, Color.magenta);
        JPanel defaultPanel = new JPanel();
        tabbedPane.add("Default", defaultPanel);

        // Put it all together and show it as content pane
        tabPanel.add(tabbedPane, BorderLayout.CENTER);
        backgroundPanel = new JPanel(new BorderLayout());
       // backgroundPanel.putClientProperty("backgroundTexture", ImageHelper.loadImage("BackgroundTexture.jpg"));
        backgroundPanel.add(checkBoxPanel, BorderLayout.NORTH);
        backgroundPanel.add(tabPanel, BorderLayout.CENTER);
        backgroundPanel.add(optionPanel, BorderLayout.SOUTH);
        JScrollPane contentScrollPane = new JScrollPane(backgroundPanel);
        contentPanel.add(contentScrollPane, BorderLayout.CENTER);
    }

    /**
     * Overriden to handle the background texture of the background panel.
     */
    @Override
    public void setLookAndFeel() {
        // The dark look and feels needs special background otherwise it looks odd
        if ((selectedLaf == Constants.LAF_HIFI) || (selectedLaf == Constants.LAF_NOIRE)) {
         //   backgroundPanel.putClientProperty("backgroundTexture", ImageHelper.loadImage("DarkBackgroundTexture.jpg"));
        } else {
          //  backgroundPanel.putClientProperty("backgroundTexture", ImageHelper.loadImage("BackgroundTexture.jpg"));
        }
        super.setLookAndFeel();
    }

    private void updateTabbedPane() {
        // to make sure that all changes on the tabbed pane are done
        // we repaint the control after all events are dispatched
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                tabPanel.validate();
                tabPanel.repaint();
            }
        });
    }


    //------------------------------------------------------------------------------
    public static void main(String[] args) {
//------------------------------------------------------------------------------
        try {
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    TransparentTabbedPaneSample app = new TransparentTabbedPaneSample();
                    app.setSize(800, 600);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main

}

