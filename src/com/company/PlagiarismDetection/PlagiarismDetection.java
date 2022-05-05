package com.company.PlagiarismDetection;

import com.company.HTML.JHyperlink;
import com.company.HTML.Parser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class Pairs {
    String file1;
    String file2;
    String analysisResult;
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
public class PlagiarismDetection {
    Boolean offline = false;
    public String fileNames="";
    String url = "";
    String path = "";
    String commandOutput ="";
    String cppCommandOutput = "<html>";
    String filesPathCommand = "";
    String mossCommand = "perl moss.pl -l cc ";
    String cppComboBox[] = new String[]{ "cppcheck --bug-hunting"
                    ,"cppcheck --enable=all --inconclusive"
                    ,"cppcheck --enable=warning"
                    ,"cppcheck --enable=style"
                    ,"cppcheck --enable=performance"
                    ,"cppcheck --enable=portability"
                    ,"cppcheck --enable=information"
                    ,"cppcheck --enable=unusedFunction"
                    ,"cppcheck --verbose"};
    String cppChosen ="cppcheck --enable=all";
    String cppCheckCommandSAll;
    public Parser parser = new Parser();
    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<Pairs> filePairs = new ArrayList<>();
    ArrayList<String> htmlResultV1 = new ArrayList<>();
    ArrayList<String> linesMatched = new ArrayList<>();
    ArrayList<String> similarities = new ArrayList<>();
    List<String> filesCompare ;
    ArrayList<String> cppCommands = new ArrayList<>() ;
    String simModified ="";
    String htmlResultText="";
    String output = null;
    int temp =0 ;
    public int [] chartOutput = {0,0,0,0,0,0,0,0,0,0,0};

    //Parsing Command Output
    public void execute() throws Exception {
        //System.out.println(filesPathCommand);
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
       // System.out.println(mossCommand);
    }
    public void runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        try {
            Process process = processBuilder.start();
            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
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
        htmlResultText = parser.getText();
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
                simModified =htmlResultV1.get(i).substring(1,index)  ;
                similarities.add(simModified);
            }
        }
      //  simModified = "";
      //  htmlResultV1= new ArrayList<>();
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
              chartOutput[index+1]+=1;
            }
        for(int i = 1; i <= 10; i++){
         System.out.println("number of files : " + i*10 +" "+ chartOutput[i]);
        }
        System.out.println("Total Pairs : " + filePairs.size());

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
         // execute();
         if(!commandOutput.contains("No such file or directory")) {
             //Delete Moss scripts from folder when analysis is done
             File dirDelete = new File(path + "\\moss.pl");
             dirDelete.delete();
             handleHtml();
             generatePairs();
             organizingPairs();
             MossResults mossResults = new MossResults();
         }
         else {
             System.out.println("ADD MOSS Script to the files directory");
         }
     }
     public void runOffline(String path) throws IOException {
        this.path = path;
         handleHtml();
         generatePairs();
         organizingPairs();
         MossResults mossResults = new MossResults();
     }
    class MossResults extends JFrame {
        JButton next = new JButton("Next");
        JTextField threshHold = new JTextField("Threshold");
        JLabel options = new JLabel("Cpp Check analysis Options");
        JComboBox <String> analysis = new JComboBox<>(cppComboBox);
        int thresholdText ;
        public MossResults() {
            initUI();
            setVisible(true);
            setSize(1000, 1000);
            setLocationRelativeTo(null);
        }
        private void initUI() {
            JPanel panel = new JPanel();
            add(panel);
            CategoryDataset dataset = createDataset();
            JFreeChart chart = createChart(dataset);
            chart.setBackgroundPaint(Color.lightGray);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBackground(Color.darkGray);
            analysis.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox<String> combo = (JComboBox<String>) e.getSource();
                    String selected = (String) combo.getSelectedItem();
                    if(selected.contains("cppcheck --bug-hunting")){
                        cppChosen = cppComboBox[0];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --enable=all --inconclusive")){
                        cppChosen = cppComboBox[1];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --enable=warning")){
                        cppChosen = cppComboBox[2];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --enable=style")){

                        cppChosen = cppComboBox[3];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --enable=performance")){
                        cppChosen = cppComboBox[4] ;
                        System.out.println(cppChosen);
                    }
                   if(selected.contains("cppcheck --enable=portability")){
                        cppChosen = cppComboBox[5];
                        System.out.println(cppChosen);
                    }
                   if(selected.contains("cppcheck --enable=information")){

                        cppChosen = cppComboBox[6];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --enable=unusedFunction")){

                        cppChosen = cppComboBox[7];
                        System.out.println(cppChosen);
                    }
                    if(selected.contains("cppcheck --verbose")){

                        cppChosen = cppComboBox[8];
                        System.out.println(cppChosen);
                    }
                }
            });
            next.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Create a new window
                    thresholdText = Integer.parseInt(threshHold.getText());
                    thresholdPairs thresholdPairs = new thresholdPairs(thresholdText);
                    thresholdPairs.setSize(1000,800);
                }
            });
            panel.add(chartPanel,BoxLayout.X_AXIS);
            setTitle("MOSS chart");
            panel.add(next,BoxLayout.X_AXIS);
            panel.add(threshHold,BoxLayout.X_AXIS);
            panel.add(analysis,BoxLayout.X_AXIS);
            panel.add(options,BoxLayout.X_AXIS);
            pack();
            cppCheckCommandSAll = cppChosen;
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
    class thresholdPairs extends JFrame {
        JButton cpp = new JButton("Run Cpp Check for threshold files ");
        JTable jTable1 = new JTable();
        Object [][]data = new Object[100][1] ;
        public Vector<String> col= new Vector<>();
        public Vector<String> tableRow = new Vector<>();
        thresholdPairs(int threshold) {
            initUI(threshold);
            setVisible(true);
        }
        private void initUI(int thresholdText) {
            JScrollPane scrollPane = new JScrollPane();
            JPanel panel = new JPanel();
            add(panel);
            setTitle("Threshold pairs");
            //ADD files and descriptions here after getting the threshold.
            try {
                filesCompare = parser.getFilesLink();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(filesCompare);
            for(int i =0 ; i<filePairs.size();i++){
                temp = i;
                if(filePairs.get(i).getSimilarity1()>=thresholdText||filePairs.get(i).getSimilarity2()>=thresholdText) {
                    JHyperlink linkWebsite = new JHyperlink(filePairs.get(i).getFile1() + " &" + filePairs.get(i).getFile2());
                    linkWebsite.setURL(filesCompare.get(i));
                    cppCommands.add(cppChosen + " " + filePairs.get(i).getFile1() + " " + filePairs.get(i).getFile2());
                    linkWebsite.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CppCheckResults results = new CppCheckResults();
                            try {
                                for (String s : cppCommands) {
                                    if (s.contains(linkWebsite.getText().substring(17, linkWebsite.getText().indexOf("_"))) &&
                                            s.contains(linkWebsite.getText().
                                                    substring(linkWebsite.getText().indexOf("&") + 1, linkWebsite.getText().lastIndexOf("<") - 4))) {
                                        results.runCppCheck(s);
                                    } else {
                                        System.out.println("Error");
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                        }
                    });

                    int lines = filePairs.get(i).lineMatched;
                    String clearName = filePairs.get(i).file1;
                    clearName = clearName.replaceAll("_assignsubmission_file_", "");
                    clearName = clearName.replaceAll("assignment", "");
                    clearName = clearName.substring(0, clearName.indexOf("_"));
                    String clearName2 = filePairs.get(i).file2;
                    clearName2 = clearName2.replaceAll("_assignsubmission_file_", "");
                    clearName2 = clearName2.replaceAll("assignment", "");
                    clearName2 = clearName2.substring(0, clearName2.indexOf("_"));
                    tableRow.add(clearName + " & " + clearName2 + " has : " + lines + " lines matched");
                    panel.add(linkWebsite, BoxLayout.X_AXIS);
                    cppCheckCommandSAll = cppCheckCommandSAll + " " + filePairs.get(i).file1 + " " + filePairs.get(i).getFile2();
                }
                    // System.out.println(cppCheckCommandStyle);


            }
            cpp.addActionListener(e1 -> {
                //CPP check Related Results
                CppCheckResults results = new CppCheckResults();
                try {
                    results.runCppCheck(cppCheckCommandSAll);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            for(int i = 0 ; i <tableRow.size(); i++){
                data[i][0]= tableRow.get(i);
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, new String [] {"Pairs"} );
            jTable1.setModel(tableModel);
            scrollPane.setViewportView(jTable1);
            panel.add(cpp,BoxLayout.X_AXIS);
            panel.add(scrollPane);
            setLocationRelativeTo(null);
            pack();
        }
    }
    class CppCheckResults extends JFrame{
        //Parsing Command Output
        public void execute(String command) throws Exception {
            cppCommandOutput = "<html>";
            System.out.println(command);
            //System.out.println(filesPathCommand);
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
                builder.directory(new File(filesPathCommand));
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                    cppCommandOutput = cppCommandOutput + line + "<br/>";
                }
                cppCommandOutput += "</html>";
        }
        public void createWindow(){
            JPanel panel = new JPanel();
            JLabel analysis = new JLabel(cppCommandOutput);
            JScrollPane pane= new JScrollPane(analysis);
            add(pane);
            pack();
            setVisible(true);
            pane.updateUI();
        }
        public void runCppCheck(String command) throws Exception{
            execute(command);
            createWindow();
        }
    }
    }



