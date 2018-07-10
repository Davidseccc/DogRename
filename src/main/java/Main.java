import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.renjin.script.RenjinScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final String CONFIG_NAME = "config.conf";
    public static final String PROCESSED_FILES_DIR = "originalFiles";

    //public static String FROM = "";
    //public static String TO = "";



    public static void main(String[] args) throws IOException {
        Options options = new Options();
        Option input = new Option("s", "source", true, "input DIR path");

        input.setRequired(false);
        options.addOption(input);

        Option output = new Option("d", "dest", true, "output DIR path");
        output.setRequired(false);
        options.addOption(output);

        Option force = new Option("f", "forceCopy", true, "Strart copying ");
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

        FileRenamer.setFROM(cmd.getOptionValue("source"));
        FileRenamer.setTO(cmd.getOptionValue("dest"));
        String forceCopy = cmd.getOptionValue("forceCopy");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        if(FileRenamer.getFROM() == null || FileRenamer.getTO() == null){
            System.out.println("Zadej umistění soubrů");
            FileRenamer.setFROM(reader.readLine());
            System.out.println("Kam kopírovat?");
            FileRenamer.setTO(reader.readLine());
        }

        File[] files = new File(FileRenamer.getFROM()).listFiles();
        List<File> validatedFiles = filterFiles(files);

        if(forceCopy == null) {

            System.out.println("Zadaná složka obsahuje soubory:");
            for (int i = 0; i < 5; i++) {
                System.out.println(validatedFiles.get(i).getName());
            }
            System.out.println("... a " + (validatedFiles.size() - 5) + " dalších...");
            System.out.println("Začít kopírovat? Y/N");
            forceCopy = reader.readLine();
        }

        if (forceCopy.equalsIgnoreCase("Y")) {
            process(validatedFiles);

            /*System.out.println("Spouštím RScript...." + FileRenamer.getFROM() + "\\grafy_psi.R");

            RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
            // create a Renjin engine:
            ScriptEngine engine = factory.getScriptEngine();

            try {
                engine.eval("library('org.renjin.cran:chron')");
                engine.eval(new java.io.FileReader(FileRenamer.getTO() +"/grafy_psi.R"));
            } catch (ScriptException e) {
                e.printStackTrace();
            }*/
            System.out.println("Hotovo :-)");

        } else {
            System.out.println("Ukončuji...");
            System.exit(1);
            return;
        }
    }

    /**
     * Main pipeline
     * @param validatedFiles List of files filtered by filterFiles() method
     */
    private static void process( List<File> validatedFiles) {
        try {
            Map<String, String> config;

            config = ConfigReader.parseConfig(FileRenamer.getFROM() +"/"+ CONFIG_NAME);
            FileRenamer.setConfig(config);
            FileRenamer.mkdirIfNotExist("undefined");
            for (String folderToCreate : config.values())
            {
                FileRenamer.mkdirIfNotExist(folderToCreate);
            }

            Map<String,  Object[] > nameToNewName;
            nameToNewName = FileRenamer.getNewFileNamesWithOffsetDate(validatedFiles);
            for (String origName :nameToNewName.keySet()) {
                List <GyroData> items = CSVDataReader.processInputFile(FileRenamer.getFROM() + "/" + origName);
                List<Calendar> dates = new ArrayList<>();

                CSVDataReader.countDeltaTime(items);
                String folder = config.get(origName.substring(0, origName.lastIndexOf("-")));
                if (folder == null){
                    folder = "undefined";
                }
                String newName = (String) nameToNewName.get(origName)[0];
                Date date = (Date) nameToNewName.get(origName)[1];
                System.out.println("ukládám soubor " + newName);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                dates.add(c);
                int year = c.get(Calendar.YEAR);
                String month = String.format("%02d", c.get(Calendar.MONTH) +1);
                String day = String.format("%02d", c.get(Calendar.DAY_OF_MONTH) );
                FileRenamer.mkdirIfNotExist(folder + "/" + year + "/" + month + "/" + day );
                CSVDataWriter.saveDataWithOffset(items, FileRenamer.getTO() +"/" + folder + "/" + year + "/" + month + "/" + day + "/"  + newName, date);
                CSVDataWriter.appendTimeStamp(dates, FileRenamer.getTO() +"/" + folder+".conf" );
                File srcFile = new File(String.format("%s/%s", FileRenamer.getFROM(), origName));
                File destDir = new File(String.format("%s/%s", FileRenamer.getFROM(), PROCESSED_FILES_DIR));
                FileUtils.moveFileToDirectory(srcFile, destDir,false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * remove all nonCSV files from processing
     * @param files
     */
    private static List<File> filterFiles(File[] files) {
        List<File> validatedFiles = new ArrayList<>();
        for (File f : files) {
            String extension = f.getName().substring(f.getName().lastIndexOf('.')+1);
            if (extension.equalsIgnoreCase("CSV") && (f.length() > 0)){
                //System.out.println(f.length() + " in bytes");
                validatedFiles.add(f);
            }
            else{
                System.out.println(f.getName() + " removed from processing.");
            }
        }
        Collections.sort(validatedFiles);
        return validatedFiles;
    }
}
