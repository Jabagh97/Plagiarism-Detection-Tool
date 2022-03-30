package com.company.Moss;

import com.company.HTML.Parser;

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

        System.out.println(namesList);
        System.out.println(similarities);
        System.out.println(linesMatched);
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
              System.out.print(filePairs.get(i).getFile1());
              System.out.println(filePairs.get(i).getSimilarity1());
              System.out.print(filePairs.get(i).getFile2());
              System.out.println(filePairs.get(i).getSimilarity2());
              System.out.println(filePairs.get(i).getLineMatched());
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







     }

}
