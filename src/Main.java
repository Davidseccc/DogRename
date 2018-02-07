import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static String FROM = "";
    public static String TO = "";


    public static void main(String[] args) throws IOException {
        Options options = new Options();
        Option input = new Option("s", "source", true, "input DIR path");

        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("d", "dest", true, "output DIR path");
        output.setRequired(true);
        options.addOption(output);

        Option force = new Option("f", "forceCopy", true, "Strart copying..");
        force.setRequired(false);
        options.addOption(force);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java -jar dogRename.jar", options);

            System.exit(1);
            return;
        }


        FROM = cmd.getOptionValue("source");
        TO = cmd.getOptionValue("dest");
        String forceCopy = cmd.getOptionValue("forceCopy");


        String acc = "";
/*
        System.out.println("Zadej umistění soubrů");
        FROM = reader.readLine();
        System.out.println("Kam kopírovat?");
        TO = reader.readLine();
*/
        File[] files = new File(FROM).listFiles();
        Arrays.sort(files);

        if(forceCopy == null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Zadaná složka obsahuje soubory:");
            for (int i = 0; i < 5; i++) {
                System.out.println(files[i].getName());
            }
            System.out.println("... a " + (files.length - 5) + " dalších...");
            System.out.println("Začít kopírovat? Y/N");
            forceCopy = reader.readLine();
        }

        if (forceCopy.equalsIgnoreCase("Y")) {
            readFiles(files);
            System.out.println("Hotovo :-)");

        } else {
            System.out.println("Ukončuji...");
            System.exit(1);
            return;
        }
    }

    private static void readFiles(File[] files) throws IOException {
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                Path p = Paths.get(files[i].getPath());
                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(attr.lastAccessTime().toMillis());
                String sep = "";
                String formatedDateString;
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                formatedDateString = df.format(cal.getTime());
                String newName = formatName(files[i].getName(), formatedDateString);

                System.out.println("Kopíruji [" + (i + 1) + "/" + files.length + "]  " + files[i].getName()
                        +" jako " + newName);
                copyFile(files[i].getName(), newName);
            }
        }
    }

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

    public static void copyFile(String name, String newName) {
        File source = new File(FROM + "/" + name);
        File dest = new File(TO + "/" + newName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
