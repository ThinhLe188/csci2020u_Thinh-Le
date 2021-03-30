package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import java.util.Map;
import java.util.TreeMap;

import static javafx.scene.paint.Color.*;

public class Controller {
    @FXML
    private Canvas mainCanvas;
    @FXML
    public GraphicsContext gc;

    private Map<String, Integer> warnings = new TreeMap<>();
    private Map<String, Color> pieColours = new TreeMap<>();
    private int warningCount = 0;


    @FXML
    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();
        FileLoader fileLoader = new FileLoader("./resources/weatherwarnings-2015.csv");
        fileLoader.loadFile();
        warnings = fileLoader.getWarnings();
        warningCount = fileLoader.getWarningCount();
        pieColours.put("FLASH FLOOD", AQUA);
        pieColours.put("SEVERE THUNDERSTORM", GOLD);
        pieColours.put("SPECIAL MARINE", DARKORANGE);
        pieColours.put("TORNADO", DARKSALMON);
        drawPieChart(warnings, warningCount, pieColours);
    }

    public void drawPieChart(Map<String, Integer> data, int total, Map<String, Color> color) {
        gc.setStroke(BLACK);
        double startAngle = 0;
        int startY = 60;
        for (String key : data.keySet()) {
            double angle = ((double) data.get(key) / (double) total) * 360;
            gc.setFill(color.get(key));
            gc.fillRect(30,startY,50,30);
            gc.strokeRect(30, startY,50,30);
            gc.fillText(key, 100, startY + 20);
            gc.strokeText(key, 100, startY + 20);
            gc.fillArc(330,20,270,270, startAngle, angle, ArcType.ROUND);
            gc.strokeArc(330,20,270,270, startAngle, angle, ArcType.ROUND);
            startAngle += angle;
            startY += 50;
        }
    }
}
