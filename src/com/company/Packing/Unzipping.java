package com.company.Packing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipping {
    private static final int BUFFER_SIZE = 8192;
    public void unzip(String zipFilePath, String destDirectory) throws IOException{
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            //create dir
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                if((entry.getName().contains("._"))||entry.getName().contains("x64")||entry.getName().contains("hw")||entry.getName().contains("github")||entry.getName().contains("debug")||entry.getName().contains("HW")||entry.getName().contains("CS")||entry.getName().contains("cs")){
                    System.out.println("Bad Submission :  " + filePath.substring(0,filePath.indexOf('_')));
                    break;
                }
                extractFile(zipIn, filePath);
            } else  {
                // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
             //   extractFile(zipIn, filePath);
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
            continue;
        }
        zipIn.close();
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}