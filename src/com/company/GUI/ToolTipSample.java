package com.company.GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 *
 * @author Michael Hagen
 */
public class ToolTipSample extends BaseSampleFrame {

    private JPanel toolTipPanel = null;
    private JPanel controlsPanel = null;
    private JCheckBox castShadowCheck = null;
    private JLabel borderSizeLabel = null;
    private JSpinner borderSizeSpinner = null;
    private JLabel shadowSizeLabel = null;
    private JSpinner shadowSizeSpinner = null;
    private JLabel toolTipLabel = null;

    public ToolTipSample() {
        super("ToolTipSample");
        castShadowCheck = new JCheckBox("tooltipCastShadow", false);
        castShadowCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTipText();
                setLookAndFeel();
            }
        });
        borderSizeLabel = new JLabel("tooltipBorderSize");
        borderSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        borderSizeSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 8, 1));
        borderSizeSpinner.setMaximumSize(new Dimension(48, 24));
        borderSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                createTipText();
                setLookAndFeel();
            }
        });
        shadowSizeLabel = new JLabel("tooltipShadowSize");
        shadowSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        shadowSizeSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 8, 1));
        shadowSizeSpinner.setMaximumSize(new Dimension(48, 24));
        shadowSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                createTipText();
                setLookAndFeel();
            }
        });

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        controlsPanel.add(castShadowCheck);
        controlsPanel.add(borderSizeLabel);
        controlsPanel.add(borderSizeSpinner);
        controlsPanel.add(shadowSizeLabel);
        controlsPanel.add(shadowSizeSpinner);
        controlsPanel.add(new JPanel());

        toolTipLabel = new JLabel("Rollover me to show the tool tip!");
        toolTipLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        toolTipLabel.setHorizontalAlignment(JLabel.CENTER);
        toolTipLabel.setOpaque(true);
        toolTipLabel.setBackground(new Color(255, 255, 128));
        toolTipLabel.setForeground(Color.black);
        toolTipLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        createTipText();

        toolTipPanel = new JPanel(new BorderLayout());
        toolTipPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        toolTipPanel.add(controlsPanel, BorderLayout.NORTH);
        toolTipPanel.add(toolTipLabel, BorderLayout.CENTER);

        contentPanel.add(toolTipPanel, BorderLayout.CENTER);
    }

    @Override
    public Properties getLAFProps() {
        Properties props = super.getLAFProps();
        if (castShadowCheck.isSelected()) {
            props.put("tooltipCastShadow", "on");
        } else {
            props.put("tooltipCastShadow", "off");
        }
        props.put("tooltipBorderSize", "" + borderSizeSpinner.getValue());
        props.put("tooltipShadowSize", "" + shadowSizeSpinner.getValue());
        return props;
    }

    private void createTipText() {
        String tip = "<html>Tool tip";
        if (castShadowCheck.isSelected()) {
            tip += " with cast shadow.<br/>";
        } else {
            tip += " with default shadow.<br/>";
        }
        tip += " tooltipBorderSize = " + borderSizeSpinner.getValue() + "<br/>";
        tip += " tooltipShadowSize = " + shadowSizeSpinner.getValue() + "<br/>";
        tip += "</html>";
        toolTipLabel.setToolTipText(tip);
    }

    //------------------------------------------------------------------------------
    public static void main(String[] args) {
//------------------------------------------------------------------------------
        try {
            // Setup the ToolTipManager
            ToolTipManager.sharedInstance().setLightWeightPopupEnabled(true);
            ToolTipManager.sharedInstance().setInitialDelay(0);
            ToolTipManager.sharedInstance().setReshowDelay(0);
            ToolTipManager.sharedInstance().setDismissDelay(60000);

            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    ToolTipSample app = new ToolTipSample();
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

