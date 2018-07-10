import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigReader {

    static Map<String, String> config = new HashMap<>();

    public static Map<String, String> parseConfig(String configPath) {
        List<Object> config2;
        try {
            File inputF = new File(configPath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            config2 = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static Function<String, Object> mapToItem = (line) -> {
        Object item = null;
        String[] p = line.split(";");
        String metawear = p[0];
        String path = (p[1]);

        config.put(metawear,path);

        return item;
    };

}
