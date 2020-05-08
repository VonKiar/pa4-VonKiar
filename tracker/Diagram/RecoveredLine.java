package tracker.Diagram;

import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import tracker.Reader;

public class RecoveredLine implements DrawStrategy {

    @Override
    public XYChart.Series<String, Number> draw(String[] boxValue, TextField field1, TextField field2, TextField field3) {
        Reader reader = new Reader("tracker/csv/recovered.csv");
        String region = "";
        if (boxValue.length > 1) region = boxValue[1].strip();
        String[] temp = reader.getData(boxValue[0].strip(), region);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(int i = 4; i<temp.length; i++) {
            series.getData().add(new XYChart.Data<>(reader.getHeader()[i], Integer.parseInt(temp[i])));
            field3.setText(temp[i]);
        }
        series.setName("Recovered Cases");
        return series;
    }
}