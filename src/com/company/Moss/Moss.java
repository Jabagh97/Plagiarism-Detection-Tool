package com.company.Moss;

import com.company.HTML.Parser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
class Pairs {
    String file1;
    String file2;
    int sim1;
    int sim2;
    int lineMatched;
    void setFile1(String input){
        file1 = input;
    }
    void setFile2(String input){
        file2 = input;
    }
    void setSimilarity1(int input){
        sim1 = input;
    }
    void setSimilarity2(int input){
        sim2 = input;
    }
    void setLineMatched(int input){lineMatched = input;}
    String getFile1(){
        return file1;
    }
    String getFile2(){
        return file2;
    }
    int getSimilarity1(){
        return sim1;
    }
    int getSimilarity2(){
        return sim2;
    }
    int getLineMatched(){return lineMatched;}
}
public class Moss  {
    public String fileNames="";
    String url = "";
    String path = "";
    String commandOutput ="";
    String filesPathCommand = "";
    String mossCommand = "perl moss.pl -l cc ";
    public Parser parser = new Parser();
    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<Pairs> filePairs = new ArrayList<>();
    ArrayList<String> htmlResultV1 = new ArrayList<>();
    ArrayList<String> linesMatched = new ArrayList<>();
    ArrayList<String> similarities = new ArrayList<>();
    public int [] chartOutput = {0,0,0,0,0,0,0,0,0,0,0};


    //Parsing Command Output
    public void execute() throws Exception {
        //System.out.println(filesPathCommand);
        //System.out.println(mossCommand);
        ProcessBuilder builder = new ProcessBuilder( "cmd.exe", "/c", mossCommand);
        builder.directory(new File(filesPathCommand));
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
            commandOutput +=line;
        }
        int index = commandOutput.lastIndexOf('h');
        url = commandOutput.substring(index);
    }
    public void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();
            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
            }
            //wait for the process to complete
            process.waitFor();
            //close the resources
            bufferedReader.close();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setFilesName(String filesName){
         fileNames=filesName;
    }
    public void setPath(String path){
        this.path=path;
    }
    public void handleHtml(){
        //Saving XML file
        String curl = "curl.exe --output result.html -L ";
        curl += url;
        System.out.println(curl);
        runCommand("cmd", "/C",curl);

        //Clean XML file from aligns
        String cleanCommand1 = "perl -i -pe \"s|^ *<TD ALIGN=right>||\" \"result.xml\"";
        runCommand("cmd", "/C",cleanCommand1);
    }
    public void generatePairs() throws IOException {
        //Get File Percentage and store them in pairs
        String htmlResultText = parser.run();
        //Adding elements one by one to a list
        for(String s : htmlResultText.split(" ")){
            htmlResultV1.add(s);
        }
        //Getting lines matched and removing it from html result
        for (int i =4 ;i<htmlResultV1.size();i=i+4){
            linesMatched.add(htmlResultV1.get(i));
            htmlResultV1.remove(i);
        }
        //Getting File names & Similarities
        for(int i=0;i<htmlResultV1.size();i++){
            if(i%2==0){
                namesList.add(htmlResultV1.get(i));
            }
            if(i%2!=0){
                int index = htmlResultV1.get(i).lastIndexOf("%");
                String simModified =htmlResultV1.get(i).substring(1,index)  ;
                similarities.add(simModified);
            }
        }
    }
    public void organizingPairs(){
        for(int i = 0 ; i<linesMatched.size() ; i++){
            Pairs pair = new Pairs();
            pair.setLineMatched(Integer.parseInt(linesMatched.get(i)));
            pair.setFile1(namesList.get(i));
            pair.setFile2(namesList.get(i+1));
            namesList.remove(i);
            pair.setSimilarity1(Integer.parseInt(similarities.get(i)));
            pair.setSimilarity2(Integer.parseInt(similarities.get(i+1)));
            similarities.remove(i);

            filePairs.add(pair);
        }
            for(int i = 0 ; i<filePairs.size() ; i++){
              int index ;
              if(filePairs.get(i).getSimilarity1()>filePairs.get(i).getSimilarity2()){
                  index = (filePairs.get(i).getSimilarity1()/10)-1;
              }
              else {
                  index = (filePairs.get(i).getSimilarity2()/10)-1;
              }
              chartOutput[index+1]=1;
            }
        for(int i = 1; i <= 10; i++){
         System.out.println("number of files : " + i*10 +" "+ chartOutput[i]);
        }
    }
    public void runMoss() throws Exception {
         //cd to files path
         int index = path.lastIndexOf('\\');
         path = path.substring(0, index);
         //Getting the files path
         filesPathCommand += path;
         //Completing Moss command with file names
         mossCommand += fileNames;

         //running MOSS
       //  execute();
         handleHtml();
         generatePairs();
         organizingPairs();
         MossResults mossResults = new MossResults();
     }

    class MossResults extends JFrame {
        JButton next = new JButton("Next");
        JTextField threshHold = new JTextField("Enter Threshold");
        int thresholdText ;
        public MossResults() {
            initUI();
            setVisible(true);
        }
        private void initUI() {
            JPanel panel = new JPanel();
            add(panel);
            CategoryDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            chartPanel.setBackground(Color.white);
            panel.add(chartPanel);
            setTitle("MOSS chart");
            setLocationRelativeTo(null);
            panel.add(next);
            panel.add(threshHold);
            pack();
            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    thresholdText = Integer.parseInt(threshHold.getText());
                    JFrame frame = new JFrame("Pairs with specified threshold");
                    JPanel panel = new JPanel();
                    LayoutManager layout = new FlowLayout();
                    panel.setLayout(layout);

                }
            });
        }

        private CategoryDataset createDataset() {
            var dataset = new DefaultCategoryDataset();
            dataset.setValue(chartOutput[0], "", "10");
            dataset.setValue(chartOutput[1], "", "20");
            dataset.setValue(chartOutput[2], "", "30");
            dataset.setValue(chartOutput[3], "", "40");
            dataset.setValue(chartOutput[4], "", "50");
            dataset.setValue(chartOutput[5], "", "60");
            dataset.setValue(chartOutput[6], "", "70");
            dataset.setValue(chartOutput[7], "", "80");
            dataset.setValue(chartOutput[8], "", "90");
            dataset.setValue(chartOutput[9], "", "100");
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
}


//TODO:Apply Pairs thing to the GUI