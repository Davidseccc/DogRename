import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Main {
    public static String FROM = "";
    public static String TO = "";



    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Zadej umistění soubrů");
        FROM = reader.readLine();
        System.out.println("Kam kopírovat?");
        TO = reader.readLine();

        File[] files = new File(FROM).listFiles();
        Arrays.sort(files);

        System.out.println("Zadaná složka obsahuje soubory:");
        for (int i=0;i<5;i++) {
            System.out.println(files[i].getName());
        }
        System.out.println("... a " + (files.length - 5) + " dalších...");
        System.out.println("Začít kopírovat? YES/NO");
        String acc = reader.readLine();

        if (acc.equalsIgnoreCase("YES")){
            readFiles(files);
            System.out.println("Hotovo :-)");

        }
        else {
            System.out.println("Ukončuji...");
            return;
        }
    }

    private static void readFiles(File[] files) throws IOException {
        for (int i= 0; i< files.length;i++) {
            if (files[i].isFile()) {
                Path p = Paths.get(files[i].getPath());
                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
                String date = (new Date(attr.creationTime().toMillis()).toLocaleString());
                String name = files[i].getName();
                String out = date + "-" + name;
                System.out.println("Kopíruji ["+ (i+1) +"/"+ files.length+"]  " + out);
                copyFile(files[i].getName(), out);
            }
        }
    }

    public static void copyFile(String name, String newName){
        File source = new File(FROM + "/" + name);
        File dest = new File(TO + "/" + newName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
