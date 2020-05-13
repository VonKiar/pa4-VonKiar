package tracker;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;

public class Read {

    private String path;
    List<String[]> data;

    public Read(String file) {
        this.path = file;
        this.data = new ArrayList<>();
        read(this.path);
    }

    private void read(String sourcefile) {
        String[] readLine = null;
        try (CSVReader reader = new CSVReader(new FileReader(sourcefile))) {
            while((readLine = reader.readNext()) != null) this.data.add(readLine);
        } catch (Exception e) {System.out.println("Cannot read file: " + sourcefile);}
    }

    public String[] getHeader() {return this.data.get(0);}

    public List<String[]> getData() {return this.data.subList(1, this.data.size());}

    public String getName() {return this.path.split("[/.]")[2];}
}