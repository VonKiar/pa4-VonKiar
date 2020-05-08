package tracker.Diagram;

import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

public interface DrawStrategy {

    public XYChart.Series<String, Number> draw(String[] boxValue, TextField field1, TextField field2, TextField field3);
}