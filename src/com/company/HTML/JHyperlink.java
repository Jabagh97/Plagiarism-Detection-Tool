package com.company.HTML;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class JHyperlink extends JLabel {
    private String url;
    private String html = "<html><a href=''>%s</a></html>";

    public JHyperlink(String text) {
        this(text, null, null);
    }

    public JHyperlink(String text, String url) {
        this(text, url, null);
    }

    public void setURL(String url) {
        this.url = url;
    }

    public JHyperlink(String text, String url, String tooltip) {
        super(text);
        this.url = url;

        setForeground(Color.red.darker());

        setToolTipText(tooltip);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setText(String.format(html, text));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setText(text);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                JFrame f = new JFrame("Moss Page");
                JEditorPane jep = new JEditorPane();
                jep.setEditable(false);
                try {
                    jep.setPage(JHyperlink.this.url);
                  //  Desktop.getDesktop().browse(new URI(JHyperlink.this.url));
                } catch (IOException  e1) {
                    JOptionPane.showMessageDialog(JHyperlink.this,
                            "Could not open the hyperlink. Error: " + e1.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                JScrollPane scrollPane = new JScrollPane(jep);
                f.getContentPane().add(scrollPane);
                f.setPreferredSize(new Dimension(1000,800));
                f.setVisible(true);
                f.pack();
                f.setLocationRelativeTo(null);
            }

        });

    }
}