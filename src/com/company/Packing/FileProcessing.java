package com.company.Packing;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/*
Processing Steps:

* Unzip assignments
* Change cpp file names ,so they include students number
* Move similar files to new folder, so we can send them to MOSS

*/
public class FileProcessing {
    Vector<String> bad = new Vector<>();
    String temp="";
    String path ;
    String modifiedPath;
    String destDirectory = "C:\\Users\\Jabagh angok\\OneDrive\\Documents\\aDummy\\";
    Unzipping unzipping = new Unzipping();
    UnzipModified unzip = new UnzipModified();
    public FileProcessing(String path) throws IOException {
        this.path = path;
        modifiedPath = path.substring(0,path.lastIndexOf('\\')) + "\\test";
        unzip.unzip(path, modifiedPath);
        File dir = new File(modifiedPath);
        traverseUnzip(dir);
        changNames(dir);
        bad = unzipping.getBadSubmission();
        System.out.println("Unzipping DONE");
    }
     void traverseUnzip(File dir) throws NullPointerException, IOException {
         if (dir.isDirectory()) {
             String[] children = dir.list();
             for (int i = 0; children != null && i < children.length; i++) {
                 traverseUnzip(new File(dir, children[i]));
             }
         }
         if (dir.isFile()) {
             if (dir.getName().endsWith(".zip"))
             {
                 temp = dir.getAbsolutePath();
                 int index = 0;
                 index = temp.lastIndexOf('\\');
                 temp = temp.substring(0,index);
                 unzipping.unzip(dir.getPath(),temp);
             }
             if (dir.getName().endsWith(".cpp")||dir.getName().endsWith(".h")){
               //  dir.delete();
             }
         }
     }
     void changNames(File dir) throws IOException {
         if (dir.isDirectory()) {
             String[] children = dir.list();
             for (int i = 0; children != null && i < children.length; i++) {
                 //System.out.println(children[i]);
                // int index = temp.lastIndexOf('_');
                 temp = dir.getName().replaceAll(" ","_") + ".cpp";
                 changNames(new File(dir, children[i]));
                // System.out.println(temp);
             }
         }
         if (dir.isFile()) {
             if (dir.getName().endsWith(".cpp")){
                 //Creating Folders
                 File dirFolder = new File(destDirectory + dir.getName().substring(0,dir.getName().indexOf('.')));
                 dirFolder.mkdirs();

                 //  Renaming and Moving files
                 int index = dir.getName().lastIndexOf('.');
                 String modifiedName = dir.getName().substring(0,index);
                 String finalName = destDirectory + dirFolder.getName()+"\\"+modifiedName+temp;
                 dir.renameTo(new File(finalName));
             }
         }
    }
    public Vector<String> getBadSubmissions(){
        return bad;
    }
}
