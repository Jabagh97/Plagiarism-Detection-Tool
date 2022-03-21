package com.company.GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class MossResults extends JFrame {
    JButton next = new JButton("Next");
    JTextField threshHold = new JTextField("Enter Thresh hold");
    public MossResults() {
        initUI();
        setVisible(true);
    }

    private void initUI() {
        LayoutManager layout = new FlowLayout();
        setLayout(layout);
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        pack();
        setTitle("Moss chart");
        add(chartPanel);
        add(next);
        add(threshHold);

    }

    private CategoryDataset createDataset() {
        var dataset = new DefaultCategoryDataset();
        dataset.setValue(50, "", "80");
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Moss Results",
                "Similarity",
                "Number of Pairs",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        return barChart;
    }


}

//TODO: Organize Components, get real results from MOSS, and use threshHold