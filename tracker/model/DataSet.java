package tracker.model;

import tracker.Read;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * DataSet class, Box of all series for type of chart.
 * @author Potchara J.
 * @version FINAL
 */
public class DataSet {

    /** Attributes */
    private String name;
    private String lastUpdate;
    private String[] header;
    private List<String> XAxis;
    private List<String[]> dataMatrix;
    private Map<String, DataSeries> dataMap;

    /**
     * Contructor setting up attribute's value.
     * @param Read class instance.
     */
    public DataSet(Read reader) {
        this.dataMatrix = reader.getData();
        this.name = reader.getName();
        this.header = reader.getHeader();

        /** Get last updated from last element from header. (date) */
        this.lastUpdate = header[header.length - 1];

        this.XAxis = new ArrayList<>();
        this.dataMap = new TreeMap<>();
        this.setDate();
        this.setData();
    }

    /**
     * Set XAxis via header's date data.
     */
    private void setDate() {
        for (int i = 4; i < this.header.length; i++) {
            this.XAxis.add(header[i]);
        }
    }

    /**
     * Set data using data withing matrix.
     */
    private void setData() {
        for (int i = 0; i < dataMatrix.size(); i++) {
            DataSeries dataSeries = new DataSeries(this.dataMatrix.get(i));

            /** Due to corrupted Canadian recovered data, we excluded for the sake of chart's beauty. */
            if (!dataSeries.region.equalsIgnoreCase("Recovered")) {

                /** Region is blank, set key to only country. */
                if (dataSeries.region.isBlank()) this.dataMap.put(dataSeries.country, dataSeries);

                /** Region isn't blankm set key to country + region with "-" sign to visually separate them. */
                else this.dataMap.put(dataSeries.country + " - " + dataSeries.region, dataSeries);
            }
        }
    }

    /**
     * Get the name of this dataSet.
     * @return String dataSet name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the name of the targeted coutnry.
     * @return String country name.
     */
    public Set<String> getCountry() {
        return this.dataMap.keySet();
    }

    /**
     * Get last updated date.
     * @return String last updated date.
     */
    public String getlastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Get XAxis for our chart.
     * @return list of dates in form of List<String>.
     */
    public List<String> getXAxis() {
        return this.XAxis;
    }

    /** the application wants to get one data series at a time (e.g. country or region)
     * so as a convenience:
     * and maybe, if it makes the code simpler.  Get the country-level data series.
     * @param comboBox's value indicating key of our dataMap.
     * @return DataSeries class of a specific country.
     */
    public DataSeries getDataSeries(String comboBoxValue) {
        return this.dataMap.get(comboBoxValue);
    };

    // ------------------------------------------------------------------------------------------------------------------
    
    /**
     * DataSeries class containing data for each country.
     */
    public class DataSeries {

        /** Attributes. */
        private String country;
        private String region;
        private List<Integer> data;
    
        /**
         * Constructor, set values using array from get method [TreeMap].
         * @param array data.
         */
        public DataSeries(String[] array) {

            /** Set values based on csv format. */
            this.country = array[1];
            this.region = array[0];
            this.data = new ArrayList<>();

            /** Forth index onwards only contains integer values for our plotting process. */
            for(int i = 4; i < array.length; i++) this.data.add(Integer.parseInt(array[i]));
        }
    
        /**
         * Get data, Note: data in this place means only integer values for plotting graph.
         * @return List<Integer> values.
         */
        public List<Integer> getData() {
            return this.data;
        }
    }
}