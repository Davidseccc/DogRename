
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVDataReader {

    public static final String INPUT_DELIMITER = ";";
    public static final String INPUT_DATE_FORMAT = "yyyyMMddHHmmssSSS";


    public static List<GyroData> processInputFile(String inputFilePath) {
        System.out.println("Parsing file: " + inputFilePath);
        List<GyroData> inputList = new ArrayList<>();
        try {
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            System.out.println("Reading File " + inputFilePath);
            inputList = br.lines().map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<GyroData> inputList2 = new ArrayList<>();
        for (GyroData g: inputList) {
            if (g.getDate() == null){
                inputList2.add(g);
            }
        }
        inputList.removeAll(inputList2);
        return inputList;
    }

    private static Function<String, GyroData> mapToItem = (line) -> {
        GyroData item;
        String[] p = line.split(INPUT_DELIMITER);
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(INPUT_DATE_FORMAT);
        String g = "0";
        String x = "0";
        String y = "0";
        String z = "0";
        try {
            //System.out.println("p[1] :  " + p[1]);
            date = df.parse(p[1].substring(0, 17));
             g = p[0];
             date = new Date();
             x = (p[2]);
             y = (p[3]);
             z = (p[4]);
        } catch (IndexOutOfBoundsException ex) {

        } catch (ParseException e) {
            System.out.println("Could not parse " + p[1].substring(0, p[1].length() - 4) + "into date");
            e.printStackTrace();
        }


        item = new GyroData(g, date, x, y, z);
        item.setOriginalDate(p[1]);


        return item;
    };

    public static void countDeltaTime(List<GyroData> items) {
        if (items.size() > 0) {
            int i = items.size() - 1;
            Date prewTimestamp = items.get(i).getDate();
            double delta = 0;
            while (i >= 0) {
                Date curTimestamp = items.get(i).date;
                delta += (prewTimestamp.getTime() - curTimestamp.getTime());
                items.get(i).setDelta(delta);
                prewTimestamp = items.get(i).getDate();
                i--;
            }

        } else {
            System.out.println("FILE IS EMPTY");
        }
    }
}
