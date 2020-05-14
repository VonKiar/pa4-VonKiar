package tracker;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;

/**
 * Reader class, read data from files.
 * @author Potchara J.
 * @version FINAL
 */
public class Read {

    /** Get file's path. */
    private String path;

    /** 2 Dimensions data. */
    List<String[]> data;

    /**
     * Constructor.
     * @param sourcefile's path.
     */
    public Read(String file) {
        this.path = file;
        this.data = new ArrayList<>();
        read(this.path);
    }

    /**
     * Read file using csvReader.
     * @param sourcefile's path.
     */
    private void read(String sourcefile) {

        /** Arrays representing row of data. */
        String[] readLine = null;

        /** Try read csv file. */
        try (CSVReader reader = new CSVReader(new FileReader(sourcefile))) {

            /** While not EOF, Adds to dataList.*/
            while((readLine = reader.readNext()) != null) this.data.add(readLine);
        } catch (Exception e) {

            /** Something went wrong while trying to read file. */
            System.err.println("[ERROR]: Cannot read file: " + sourcefile);
        }
    }

    /**
     * Get first row of data as the header, AKA Axis / date.
     * @return first element of data.
     */
    public String[] getHeader() {
        return this.data.get(0);
    }

    /**
     * Get data of the file except first row / header.
     * @return sublist of data excluding first element.
     */
    public List<String[]> getData() {
        return this.data.subList(1, this.data.size());
    }

    /**
     * Get name of the file we are currently reading by extracting the path's name.
     * @param args from main class.
     */
    public String getName() {
        return this.path.split("[/.]")[2];
    }
}