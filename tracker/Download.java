package tracker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Download class, loading data files if they aren't the latest one.
 * @author Potchara J.
 * @version FINAL
 */
public class Download {

    /**
     * Read URL and download data files.
     */
    public static void urlReader() {

        /** Create HashMap to store values from properties for easier access. */
        Map<String, String> pathMap = new HashMap<>();

        /** Put values in map. */
        pathMap.put(Config.getInstance().getProperty("covid.confirmed.url"), Config.getInstance().getProperty("covid.confirmed.data"));
        pathMap.put(Config.getInstance().getProperty("covid.deaths.url"), Config.getInstance().getProperty("covid.deaths.data"));
        pathMap.put(Config.getInstance().getProperty("covid.recovered.url"), Config.getInstance().getProperty("covid.recovered.data"));

        for(String key : pathMap.keySet()) {

            /** Make temp file. */
            File file = new File(pathMap.get(key));

            /** Check file's exitence / validate file's date. */
            if ( !file.exists() || (dateValidate(file) == false)) {

                /** If file doesn't exist or it is outdated, download new files. */
                file.delete();
                try {

                    /** Download. */
                    InputStream input =  new URL(key).openStream();
                    Files.copy(input, Paths.get(pathMap.get(key)));

                } catch (IOException e) {

                    /** URL error hence website has updated new url links. */
                    System.err.println("[ERROR]: Update URL.");
                }
            }
        }
    }

    /**
     * Validating file's date.
     * @param file to validate.
     */
    private static boolean dateValidate(File file) {

        /** Format for our files. */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

        /** Format for current date. */
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");

        /** Get current date. */
        LocalDateTime now = LocalDateTime.now();

        /** Validation. */
        return dtf.format(now).equalsIgnoreCase(sdf.format(file.lastModified()));
    }
}