package tracker.model;

import tracker.Read;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSet { // box of all series for type of chart.

    private String name;
    private String lastUpdate;
    private String[] header;
    private List<String> XAxis; // or make this a DataSeries
    private List<String[]> dataMatrix;
    private Map<String, DataSeries> dataMap;

    public DataSet(Read reader) {
        this.dataMatrix = reader.getData();
        this.name = reader.getName();
        this.header = reader.getHeader();
        this.lastUpdate = header[header.length - 1];
        this.XAxis = new ArrayList<>();
        this.dataMap = new TreeMap<>();
        this.setDate();
        this.setData();
    }

    private void setDate() {
        for (int i = 4; i < this.header.length; i++) {
            this.XAxis.add(header[i]);
        }
    }

    private void setData() {
        for (int i = 0; i < dataMatrix.size(); i++) {
            DataSeries dataSeries = new DataSeries(this.dataMatrix.get(i));
            if (!dataSeries.region.equalsIgnoreCase("Recovered")) {
                if (dataSeries.region.isBlank()) this.dataMap.put(dataSeries.country, dataSeries);
                else this.dataMap.put(dataSeries.country + " - " + dataSeries.region, dataSeries);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Set<String> getCountry() {
        return this.dataMap.keySet();
    }

    public String getlastUpdate() {
        return this.lastUpdate;
    }

    public List<String> getXAxis() {
        return this.XAxis;
    }

    // the application wants to get one data series at a time (e.g. country or region)
    // so as a convenience:
    // and maybe, if it makes the code simpler.  Get the country-level data series.
    public DataSeries getDataSeries(String comboBoxValue) {
        return this.dataMap.get(comboBoxValue);
    };

    // ------------------------------------------------------------------------------------------------------------------
    
    public class DataSeries {
        private String country;
        private String region;
        private List<Integer> data;
    
        public DataSeries(String[] array) {
            this.country = array[1];
            this.region = array[0];
            this.data = new ArrayList<>();
            for(int i = 4; i < array.length; i++) this.data.add(Integer.parseInt(array[i]));
        }
    
        public List<Integer> getData() {
            return this.data;
        }
    }
}