package com.company.Moss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

public class Moss {
    //Parsing Command Output
     void exec(String command) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(command);
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

}
    //Get Result URL
     void getUrl(String cmdOutput)
    {

    }
    //Get Similarity Percentage
     void getPercentage(String input)
    {

    }
    //Get File Names
     String getFileName(String input)
    {
        int pos;
        pos = input.indexOf(" ");
        String output = input.substring(0, pos);
        return output;
    }
     void runMoss(){
        String filesPathCommand = "cd ";
        String mossCommand = "perl moss.pl -l cc ";


    }




}
