package com.company.Packing;

import java.io.IOException;
import java.nio.file.*;

public class FileProcessing {
    String destDirectory = "C:\\Users\\Jabagh angok\\OneDrive\\Documents\\aDummy\\1";
    String path = "";
    public FileProcessing(String path) throws IOException {
        this.path = path;
        moveFiles();
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
}
