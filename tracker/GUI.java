package tracker;

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
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUI extends Application {

    private FlowPane root;
    private String[] header;
    private Rectangle2D screenBounds;
    private LineChart<String, Number> chart;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage prime) throws Exception {
        screenBounds = Screen.getPrimary().getBounds();
        Download.urlReader();
        Reader reader = new Reader("tracker/csv/confirmed.csv");
        header = reader.getHeader();
        prime.setTitle("COVID19 Data Visualization 1.0");
        root = initComponent();
        Scene scene = new Scene(root, screenBounds.getMaxX(), screenBounds.getMaxY());
        // scene.getStylesheets().add("tracker/stylesheet/stylesheet.css");
        prime.setScene(scene);
        prime.setFullScreen(true);
        prime.setResizable(false);
        prime.show();
    }

    private FlowPane initComponent() throws FileNotFoundException {
        ComboBox<String> mode = new ComboBox<>();
        ComboBox<String> box1 = new ComboBox<>();
        ComboBox<String> box2 = new ComboBox<>();
        Button exitButton = new Button("Exit");

        

        mode.setPromptText("Select mode");
        box1.setPromptText("Select country");
        box2.setPromptText("Select region");
        chart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        chart.setPrefSize(screenBounds.getMaxX(), 750);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(int i = 4; i<this.header.length; i++) {series.getData().add(new XYChart.Data<>(this.header[i], 0));}
        chart.getData().add(series);

        
        Text dateText = new Text("Last updated");
        Text reference = new Text("Data projected using csv files obtained from The Humanitarian Data Exchange Website");
        

        dateText.setId("text");
        reference.setId("text");
        exitButton.setId("exit");

        mode.setOnAction(this::handleMode);
        box1.setOnAction(this::handleBox1);
        box2.setOnAction(this::handleBox2);
        exitButton.setOnAction(this::handleExit);



        FlowPane root = new FlowPane();
        root.getChildren().addAll(mode, box1, box2);
        root.setAlignment(Pos.CENTER);
        root.setHgap(25.0);
        root.setVgap(10.0);
        root.getChildren().addAll(chart, dateText, reference, exitButton);
        return root;
    }

    public LineChart<String, Number> getChart() {
        return this.chart;
    }

    private void handleMode(ActionEvent event) {

    }

    private void handleBox1(ActionEvent event) {
        
    }

    private void handleBox2(ActionEvent event) {
        
    }

    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    public static void error(String msg) {
        Popup window = new Popup();
        Label label = new Label("This is a Popup"); 
        label.setStyle(" -fx-background-color: white;"); 
        window.getContent().add(label);
    }
}