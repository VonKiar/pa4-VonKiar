package tracker;

import tracker.Diagram.*;

import java.io.FileNotFoundException;
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

public class GUI extends Application {

    private FlowPane root;
    private Reader reader;
    private String[] header;
    private Rectangle2D screenBounds;
    private LineChart<String, Number> chart;
    private ComboBox<String> mode;
    private ComboBox<String> box;

    private TextField display1;
    private TextField display2;
    private TextField display3;

    private DrawStrategy drawStrategy;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage prime) throws Exception {

        screenBounds = Screen.getPrimary().getBounds();

        Download.urlReader();

        this.reader = new Reader("tracker/csv/confirmed.csv");

        this.header = reader.getHeader();

        this.drawStrategy = null;

        prime.setTitle("COVID19 Data Visualization 1.0");
        
        root = initComponent();

        Scene scene = new Scene(root, screenBounds.getMaxX(), screenBounds.getMaxY());
        scene.getStylesheets().add("tracker/stylesheet/stylesheet.css");
        
        prime.setScene(scene);

        prime.setFullScreen(true);
        prime.setResizable(false);

        prime.show();
    }

    private FlowPane initComponent() throws FileNotFoundException {

        this.mode = new ComboBox<>();
        this.box = new ComboBox<>();
        this.box.setVisible(false);

        display1 = new TextField("6969");
        display2 = new TextField("6969");
        display3 = new TextField("6969");
        display1.setEditable(false);
        display2.setEditable(false);
        display3.setEditable(false);
        // display1.setVisible(false);
        // display2.setVisible(false);
        // display3.setVisible(false);

        Button exitButton = new Button("Exit");

        this.mode.setPromptText("Select mode");
        this.box.setPromptText("Select country");

        this.mode.getItems().addAll("InfectedLine", "DeathsLine", "RecoveredLine", "Show All");
        this.box.getItems().addAll(reader.getCountry());

        this.chart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        this.chart.setPrefSize(screenBounds.getMaxX(), 750);
        this.chart.getData().add(defaultChart());
        
        Text dateText = new Text("Last updated: " + this.header[ this.header.length - 1 ]);
        Text reference = new Text("Data projected using csv files obtained from The Humanitarian Data Exchange Website");

        dateText.setId("text");
        reference.setId("text");
        exitButton.setId("exit");

        this.mode.setOnAction(this::handleMode);
        this.box.setOnAction(this::handleBox);
        exitButton.setOnAction(this::handleExit);

        FlowPane root = new FlowPane();
        root.getChildren().addAll(this.mode, this.box, display1, display2, display3);
        root.setAlignment(Pos.CENTER);
        root.setHgap(25.0);
        root.setVgap(10.0);
        root.getChildren().addAll(this.chart, dateText, reference, exitButton);
        return root;
    }

    private void handleMode(ActionEvent event) {
        this.chart.getData().clear();
        this.chart.getData().add(defaultChart());
        setStrategy(this.mode.getValue());
        this.box.setVisible(true);
    }

    private void handleBox(ActionEvent event) {
        this.chart.getData().clear();
        String[] temp = this.box.getValue().split("-");
        if (!this.mode.getValue().equalsIgnoreCase("Show All")) this.chart.getData().add( this.drawStrategy.draw(temp, display1, display2, display3));
        else {
            for(int i = 0; i < this.mode.getItems().size()-1; i++) {
                setStrategy(this.mode.getItems().get(i));
                this.chart.getData().add( this.drawStrategy.draw(temp, display1, display2, display3));
            }
        }
    }

    private XYChart.Series<String, Number> defaultChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(int i = 4; i<this.header.length; i++) {series.getData().add(new XYChart.Data<>(this.header[i], 0));}
        series.setName("Select Country");
        return series;
    }

    private void setStrategy(String strategy) {
        try {
            // If this succeeds then the the strategy class we tried to load exists.
            // NOTE: Use deprecated newInstance(0 method here because it provides LESS exceptions
            ClassLoader loader = GUI.class.getClassLoader();
            drawStrategy = (DrawStrategy)loader.loadClass("tracker.Diagram." + strategy).newInstance(); 
        }
        // Any of these exeptions means BOOM! no class for you.
        catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            // error("Error: invalid input for ", "strategy", 1);
        }
    }

    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}