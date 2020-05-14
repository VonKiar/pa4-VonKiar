package tracker;

import tracker.model.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Controller class, constructing GUI components and setup the application.
 * @author Potchara J.
 * @version FINAL
 */
public class Controller extends Application {

    /** Initiate all necessary components. */
    private Rectangle2D screenBounds;
    private FlowPane root;
    private List<DataSet> dataSets;
    private DataSet defaultSet;
    private ComboBox<String> mode;
    private ComboBox<String> box;
    private String standard;
    private boolean standardMode;
    private List<TextField> display;
    private LineChart<String, Number> chart;
    private List<Text> textList;
    private Button exitButton;

    /**
     * Main method to launch the application.
     * @param args from main class.
     */
    public static void main(String[] args) {launch(args);}

    /**
     * Start method automatically runs when the main method is called, set stage.
     * @param prime stage came with override method.
     */
    @Override
    public void start(Stage prime) throws Exception {
        Download.urlReader();
        prime.setTitle("COVID19 Data Visualization 1.0");
        this.root = initComponent();
        Scene scene = new Scene(root, screenBounds.getMaxX(), screenBounds.getMaxY());
        scene.getStylesheets().add("tracker/stylesheet/stylesheet.css");
        prime.setScene(scene);
        prime.setFullScreen(true);
        prime.show();
    }

    /**
     * Early instantiation, set data classes ready to use.
     */
    private void setDataSets() {

        /** Singleton, calling properties for setting DataSets values. */
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.confirmed.data"))));
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.deaths.data"))));
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.recovered.data"))));
    }

    /**
     * Initialize components values, etc.
     * @return FlowPane components of the stage.
     */
    private FlowPane initComponent() throws FileNotFoundException {

        /**
         * IMPORTANT: All of .setId() methods are used to set id tags for the components,
         * so that we can use CSS stylesheet to freely customize them without touching
         * source code to prevent possible bug creation.
         */
        
        /** Initializing attributes, etc. */
        this.dataSets = new ArrayList<>();
        this.setDataSets();
        this.defaultSet = dataSets.get(0);

        /** Set ComboBox selection menu. */
        this.mode = new ComboBox<>();
        this.standard = "DEFAULT";
        this.standardMode = true;
        this.mode.getItems().addAll(this.standard, "Differentiate");
        this.mode.getSelectionModel().selectFirst();
        this.mode.setOnAction(this::handleMode);
        this.mode.setId("box");
        this.box = new ComboBox<>();
        this.box.setPromptText("Select Country");
        this.box.getItems().addAll(defaultSet.getCountry());
        this.box.setOnAction(this::handleBox);
        this.box.setId("box");

        /** Create default chart. */
        this.chart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        this.screenBounds = Screen.getPrimary().getBounds();
        this.chart.setPrefSize(screenBounds.getMaxX(), screenBounds.getMaxY()*0.8);
        this.chart.getData().add(defaultChart());

        /** Create display boxes. */
        this.display = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            TextField textField = new TextField("0");
            textField.setId("box");
            textField.setPrefWidth(80);
            textField.setEditable(false);
            textField.setAlignment(Pos.CENTER);
            this.display.add(textField);
        }

        /** Create exit button. */
        this.exitButton = new Button("Exit");
        this.exitButton.setOnAction(this::handleExit);
        this.exitButton.setId("exit");

        /** Create texts & set text values. */
        this.textList = new ArrayList<>();
        this.textList.add(new Text("COVID19 GLOBAL STATUS TRACKING SYSTEM"));
        this.textList.add(new Text("INFECTED"));
        this.textList.add(new Text("DEATHS"));
        this.textList.add(new Text("RECOVERED"));
        this.textList.add(new Text("LAST UPDATED: " + defaultSet.getlastUpdate()));
        this.textList.add(new Text("Data projected using csv files obtained from The Humanitarian Data Exchange Website"));
        for(Text text: this.textList) text.setId("text");

        /** Create FlowPane and add all components into root then return it to place in Scene. */
        FlowPane root = new FlowPane();
        root.getChildren().addAll(
            this.textList.get(0), this.mode, this.box,
            this.textList.get(1), display.get(0),
            this.textList.get(2), display.get(1),
            this.textList.get(3), display.get(2)
            );
        root.getChildren().addAll(
            this.chart,
            this.textList.get(4),
            this.textList.get(5),
            this.exitButton
            );
        root.setAlignment(Pos.CENTER);
        root.setHgap(25.0);
        root.setVgap(10.0);
        return root;
    }

    /**
     * [On action] Set mode of the apllication using value from comboBox.
     * @param event on Action method.
     */
    private void handleMode(ActionEvent event) {
        this.standardMode = this.mode.getValue().equalsIgnoreCase(this.standard);
    }

    /**
     * [On action] Start the graphing sequences and display data with drawGraph() method.
     * @param event on Action method.
     */
    private void handleBox(ActionEvent event) {

        /** Clear previous chart data. */
        this.chart.getData().clear();

        /** Draw new ones based on values within the comboBox. */
        this.drawGraph();
    }

    /**
     * Graph plotting method, get values of country from the comboBox.
     * Verify if the mode is set to standard or not then plot the values using the dataset of that specific country.
     */
    private void drawGraph() {

        /** Get country from comboBox [Select coutnry] */
        String value = this.box.getValue();
        int index = 0;

        /** To be able to plot all three types of graphs we need to run through the dataSets list. */
        for(DataSet dataSerie : this.dataSets) {
            try {

                /** Values for each day. */
                List<Integer> data = dataSerie.getDataSeries(value).getData();

                /** Date, AKA Axis. */
                List<String> date = dataSerie.getXAxis();

                /** New series instance. */
                XYChart.Series<String, Number> series = new XYChart.Series<>();

                /** Depends on the selected mode,
                 * the values will be different so we will find out and assign them to this variable. */
                int dataValue = 0;

                /** Loop through data values. */
                for(int i = 0; i < data.size(); i++) {

                    /** Standard mode. */
                    if (this.standardMode) dataValue = data.get(i);

                    /** Differentiate mode. */
                    else {

                        /** Differentiate mode is the day compared to previous day,
                         * so we do not need to calculate if we are on the first day.
                         * [Nothing to compare to]
                         */
                        if (i != 0) dataValue = data.get(i) - data.get(i - 1);
                    }

                    /** Plotting. */
                    XYChart.Data<String, Number> values = new XYChart.Data<>(date.get(i), dataValue);
                    series.getData().add(values);
                }

                /** Set name of the current graph type. */
                series.setName(dataSerie.getName());

                /** Add the series to the chart. */
                this.chart.getData().add(series);

                /** Set last updated values to the display boxes. */
                this.display.get(index).setText(String.valueOf(dataValue));

                /** Index is used for identifying which display box we are setting. */
                index++;

            } catch (NullPointerException e) {

                /** Some countries do not contains values for type recovered,
                 * so we want to set the display to null. */
                this.display.get(index).setText("N/A");
            }
        }
    }

    /**
     * Create default chart with all values set to 0,
     * 
     * IMPORTANT: If I do not set an empty graph with XAxis pre-entering country values
     * the date on the XAxis will sometimes mixed up in 1 spot due to javafx currupted during the XAxis plotting.
     * So making pre-data XAxis will cover this problem without taking too much unnecessary actions.
     * 
     * @return series ready to be added to the chart.
     */
    private XYChart.Series<String, Number> defaultChart() {

        /** Create new series. */
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        /** Add default values. (0, date) */
        for(String date: defaultSet.getXAxis()) series.getData().add(new XYChart.Data<>(date, 0));

        /** Default chart-legend. */
        series.setName("Select Country");
        return series;
    }

    /**
     * [On action] Terminate stage and exit the application.
     * @param event on Action method.
     */
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}