import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSVDataWriter {

    public static final String OUTPUT_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String COMMA_DELIMITER = ";";
    public static final String NEW_LINE_SEPARATOR = "\n";

    public static void saveData(List<GyroData> items, String outputFile) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputFile);
            SimpleDateFormat sd = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
            for (GyroData data : items) {
                fileWriter.append(data.gyroOrAccIdentificator());
                fileWriter.append(NEW_LINE_SEPARATOR);
                //System.out.println(data.getOriginalDate() +"; " + sd.format(data.getDate())+ data.getOriginalDate().substring(17, 21));
                fileWriter.append(String.valueOf(data.getDate()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(data.getX());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(data.getY());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(data.getZ());
                fileWriter.append(COMMA_DELIMITER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataWithOffset(List<GyroData> items, String outputFile, Date newDate) {
            FileWriter fileWriter = null;

            try {
                fileWriter = new FileWriter(outputFile);
                SimpleDateFormat sd = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
                for (GyroData data : items) {
                    fileWriter.append(data.gyroOrAccIdentificator());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(String.valueOf(sd.format(newDate.getTime() - data.getDelta())));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(data.getX());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(data.getY());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(data.getZ());
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void appendTimeStamp(List<Calendar> items, String outputFile) {

        FileWriter fileWriter = null;
        String DELIMITER = ",";
        try {
            fileWriter = new FileWriter(outputFile, true);
            for (Calendar c : items) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
                String date = sd.format(c.getTime());
                fileWriter.append(date.substring(0,1));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(1,2));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(2,3));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(3,4));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(4,5));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(5,6));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(6,7));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(7,8));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(8,9));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(9,10));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(10,11));
                fileWriter.append(DELIMITER);
                fileWriter.append(date.substring(11,12));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
