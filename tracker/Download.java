package tracker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Download {

    public static void urlReader() throws MalformedURLException, IOException {
        Map<String, String> pathMap = new HashMap<>();
        pathMap.put(Config.getInstance().getProperty("covid.confirmed.url"), Config.getInstance().getProperty("covid.confirmed.data"));
        pathMap.put(Config.getInstance().getProperty("covid.deaths.url"), Config.getInstance().getProperty("covid.deaths.data"));
        pathMap.put(Config.getInstance().getProperty("covid.recovered.url"), Config.getInstance().getProperty("covid.recovered.data"));
        for(String key : pathMap.keySet()) {
            File file = new File(pathMap.get(key));
            if ( !file.exists() || (dateValidate(file) == false)) {
                file.delete();
                InputStream input =  new URL(key).openStream();
                Files.copy(input, Paths.get(pathMap.get(key)));
            }
        }
    }

    private static boolean dateValidate(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");  
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).equalsIgnoreCase(sdf.format(file.lastModified()));
    }
}