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

public class Controller extends Application {

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

    public static void main(String[] args) {launch(args);}

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

    private void setDataSets() {
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.confirmed.data"))));
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.deaths.data"))));
        this.dataSets.add(new DataSet(new Read(Config.getInstance().getProperty("covid.recovered.data"))));
    }

    private FlowPane initComponent() throws FileNotFoundException {
        
        this.dataSets = new ArrayList<>();
        this.setDataSets();
        this.defaultSet = dataSets.get(0);
        this.mode = new ComboBox<>();
        this.standard = "DEFAULT";
        this.mode.getItems().addAll(this.standard, "Differentiate");
        this.mode.getSelectionModel().selectFirst();
        this.standardMode = true;
        this.mode.setOnAction(this::handleMode);
        this.mode.setId("box");
        this.box = new ComboBox<>();
        this.box.setPromptText("Select Country");
        this.box.getItems().addAll(defaultSet.getCountry());
        this.box.setOnAction(this::handleBox);
        this.box.setId("box");
        this.chart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        this.screenBounds = Screen.getPrimary().getBounds();
        this.chart.setPrefSize(screenBounds.getMaxX(), screenBounds.getMaxY()*0.8);
        this.chart.getData().add(defaultChart());
        this.display = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            TextField textField = new TextField("0");
            textField.setId("box");
            textField.setPrefWidth(80);
            textField.setEditable(false);
            textField.setAlignment(Pos.CENTER);
            this.display.add(textField);

        }
        this.exitButton = new Button("Exit");
        this.exitButton.setOnAction(this::handleExit);
        this.exitButton.setId("exit");
        this.textList = new ArrayList<>();
        this.textList.add(new Text("COVID19 GLOBAL STATUS TRACKING SYSTEM"));
        this.textList.add(new Text("INFECTED"));
        this.textList.add(new Text("DEATHS"));
        this.textList.add(new Text("RECOVERED"));
        this.textList.add(new Text("LAST UPDATED: " + defaultSet.getlastUpdate()));
        this.textList.add(new Text("Data projected using csv files obtained from The Humanitarian Data Exchange Website"));
        for(Text text: this.textList) text.setId("text");
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

    private void handleMode(ActionEvent event) {
        this.standardMode = this.mode.getValue().equalsIgnoreCase(this.standard);
    }

    private void handleBox(ActionEvent event) {
        this.chart.getData().clear();
        this.drawGraph();
    }

    private void drawGraph() {
        String value = this.box.getValue();
        int index = 0;
        for(DataSet dataSerie : this.dataSets) {
            try {
                List<Integer> data = dataSerie.getDataSeries(value).getData();
                List<String> date = dataSerie.getXAxis();
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                int dataValue = 0;
                for(int i = 0; i < data.size(); i++) {
                    if (this.standardMode) dataValue = data.get(i);
                    else {
                        if (i != 0) dataValue = data.get(i) - data.get(i - 1);
                    }
                    XYChart.Data<String, Number> values = new XYChart.Data<>(date.get(i), dataValue);
                    series.getData().add(values);
                }
                series.setName(dataSerie.getName());
                this.chart.getData().add(series);
                this.display.get(index).setText(String.valueOf(dataValue));
                index++;
            } catch (NullPointerException e) {this.display.get(index).setText("N/A");}
        }
    }

    private XYChart.Series<String, Number> defaultChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(String date: defaultSet.getXAxis()) {series.getData().add(new XYChart.Data<>(date, 0));}
        series.setName("Select Country");
        return series;
    }

    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}