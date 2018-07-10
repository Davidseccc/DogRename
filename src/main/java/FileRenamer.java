import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileRenamer {
    public static  String FROM ="";
    public static  String TO ="";

    private static Map<String, String> config;

    /**
     *
     * @param files list of Files loaded from specified folder
     * @return Map<String,String> where key is original name and value is a new name
     * @throws IOException
     */
    public static  Map<String, String> getNewFileNames(List<File> files) throws IOException {
        Map<String, String> nameToNewName = new HashMap<>();

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isFile())  {
                Path p = Paths.get(files.get(i).getPath());
                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(attr.lastAccessTime().toMillis());
                String sep = "";
                String formatedDateString;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                formatedDateString = df.format(cal.getTime());
                String newName = formatName(files.get(i).getName(), formatedDateString);
                nameToNewName.put(files.get(i).getName(), newName);
                //System.out.println("Kopíruji [" + (i + 1) + "/" + files.size() + "]  " + files.get(i).getName() +" jako " + newName);
                //CSVDataWriter.saveDataWithOffset(files.get(i).getName(), newName);
            }
        }
        return nameToNewName;
    }

    public static  Map<String, Object[] > getNewFileNamesWithOffsetDate(List<File> files) throws IOException {
        Map<String, Object[]> nameToNewName = new HashMap<>();

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isFile())  {
                Path p = Paths.get(files.get(i).getPath());
                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(attr.lastAccessTime().toMillis());
                String sep = "";
                String formatedDateString;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
                formatedDateString = df.format(cal.getTimeInMillis());
                String newName = formatName(files.get(i).getName(), formatedDateString);
                Object[] newVal = new Object[2];
                newVal[0] = (newName) ;
                newVal[1] = new Date(cal.getTimeInMillis());
                nameToNewName.put(files.get(i).getName(), newVal);
                //System.out.println("Kopíruji [" + (i + 1) + "/" + files.size() + "]  " + files.get(i).getName() +" jako " + newName);
                //CSVDataWriter.saveDataWithOffset(files.get(i).getName(), newName);
            }
        }
        return nameToNewName;
    }

    /**
     *
     * @param name
     * @param formatedDateString
     * @return
     */
    private static String formatName(String name, String formatedDateString) {
        int endofFileNameIndex = name.lastIndexOf('.');
        String fileName = name.substring(0, endofFileNameIndex);
        String extension = name.substring(endofFileNameIndex + 1);
        String fn;
        try {
            fn = fileName.substring(0, fileName.lastIndexOf("-"));
        } catch (Exception e) {
            fn = fileName;
        }
        return fn +"-"+ formatedDateString + "." + extension;
    }

    /**
     *
     * @param name
     * @param newName
     */
    public static void copyFile(String name, String newName) {
        File source = new File(FROM + "/" + name);
        String folder = config.get(name.substring(0, name.lastIndexOf("-")));
        if (folder == null){
            folder = "undefined";
        }

        File dest = new File(FROM + "/" + folder + "/" + newName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new directory in FROM path
     */
    public static void mkdirIfNotExist(String name) throws IOException {
        Files.createDirectories(Paths.get(FROM + "/" + name));
    }


    public static String getFROM() {
        return FROM;
    }

    public static void setFROM(String FROM) {
        FileRenamer.FROM = FROM;
    }

    public static String getTO() {
        return TO;
    }

    public static void setTO(String TO) {
        FileRenamer.TO = TO;
    }

    public static void setConfig(Map<String, String> config) {
        FileRenamer.config = config;
    }
}
