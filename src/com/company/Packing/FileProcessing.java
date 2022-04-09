package com.company.Packing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcessing {
    String temp="";
    String path = "";
    String test = "C:\\Users\\Jabagh angok\\OneDrive\\Desktop\\Senior\\Test";
    String[] fileNames;
    File dir = new File("C:\\Users\\Jabagh angok\\OneDrive\\Desktop\\Senior\\Test");
    String destDirectory = "C:\\Users\\Jabagh angok\\OneDrive\\Documents\\aDummy\\1";
    String[] children;
    Unzipping unzipping = new Unzipping();
    public FileProcessing(String path) throws IOException {
        this.path = path;
        traverse(dir);
        System.out.println("Unzipping DONE");
       // process();
    }
    void moveFiles() throws IOException {
        Path sourcePath = Paths.get(path);
        Path targetPath = Paths.get(destDirectory);
        System.out.println(path);
        System.out.println(destDirectory);
        try {
            // rename or move a file to other path
            // if target exists, throws FileAlreadyExistsException
            Files.move(sourcePath, targetPath);
            // if target exists, replace it.
            // Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // multiple CopyOption
           /* CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING,
                                StandardCopyOption.COPY_ATTRIBUTES,
                                LinkOption.NOFOLLOW_LINKS };
            Files.move(sourcePath, targetPath, options);
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void process(){
        Unzipping unzip = new Unzipping();
        try {
            unzip.unzip(path, destDirectory);
        } catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }
    }

     void traverse(File dir) throws NullPointerException, IOException {
         if (dir.isDirectory()) {
             String[] children = dir.list();
             //System.out.println(children);
             for (int i = 0; children != null && i < children.length; i++) {
                // System.out.println(children[i]);
                 traverse(new File(dir, children[i]));
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
         }
     }

     }
