package tracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download {

    public static void urlReader() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tracker/references/csvPath.txt"))) {
            String[] temp = null;
            while(reader.ready()) {
                temp = reader.readLine().split(" ");
                File existence = new File(temp[1]);
                if (existence.exists()) existence.delete();
                try (InputStream input =  new URL(temp[2]).openStream()) {
                    Files.copy(input, Paths.get(temp[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Problem downloading file.");
        }
    }
}