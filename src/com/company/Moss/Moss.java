package com.company.Moss;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Pairs {
    String file1;
    String file2;
    int sim;
    void setFile1(String input){
        file1 = input;
    }
    void setFile2(String input){
        file2 = input;
    }
    void setSimilarity(int input){
        sim = input;
    }
    String getFile1(){
        return file1;
    }
    String getFile2(){
        return file2;
    }
    int getSimilarity(){
        return sim;
    }
}

public class Moss  {

    public String fileNames="";
    String file = "";
    String path = "";
    String filesPathCommand = "";
    String mossCommand = "perl moss.pl -l cc ";

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
        }
    }
    //Get Result URL
     void getUrl(String cmdOutput)
    {
        int index = cmdOutput.indexOf(":")-4; //this finds the first occurrence of "."
        String url = cmdOutput.substring(index);
    }
    //Get Similarity Percentage
     void getPercentage(String input)
    {
        int index = input.indexOf(" ");
        String simPercent = input.substring(index);
        simPercent = simPercent.substring(2,simPercent.length()-4);
        int output = Integer.parseInt(simPercent);
    }
    //Get File Names
     String getFileName(String input)
    {
        int pos;
        pos = input.indexOf(" ");
        String output = input.substring(0, pos);
        return output;
    }
    public void setFilesName(String filesName){
         fileNames=filesName;
    }
    public void setPath(String path){
        this.path=path;
    }

     public void runMoss() throws Exception {
         //cd to files path
         ArrayList<String> names = new ArrayList<>();
         int index = path.lastIndexOf('\\');
         path = path.substring(0,index);
         //Getting the files path
         filesPathCommand += path;
         //Completing Moss command with file names
         mossCommand += fileNames;
         //Storing file names in a list
         for(String s :fileNames.split(" ")){
             names.add(s);
             System.out.println(s);
         }
         execute();
     }
}
