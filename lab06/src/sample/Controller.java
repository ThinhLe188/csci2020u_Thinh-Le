package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import javax.swing.plaf.synth.SynthTextAreaUI;

public class Controller {
    @FXML
    private Canvas barCanvas, pieCanvas;
    @FXML
    public GraphicsContext gc;

    private static double[] avgHousingPricesByYear = {
            247381.0,264171.4,287715.3,294736.1,
            308431.4,322635.9,340253.0,363153.7
    };
    private static double[] avgCommercialPricesByYear = {
            1121585.3,1219479.5,1246354.2,1295364.8,
            1335932.6,1472362.0,1583521.9,1613246.3
    };

    private static String[] ageGroups = {
            "18-25", "26-35", "36-45", "46-55", "56-65", "65+"
    };
    private static int[] purchasesByAgeGroup = {
            648, 1021, 2453, 3173, 1868, 2247
    };
    private static Color[] pieColours = {
            Color. AQUA , Color. GOLD , Color. DARKORANGE ,
            Color. DARKSALMON , Color. LAWNGREEN , Color. PLUM
    };

    @FXML
    public void initialize() {
        gc = barCanvas.getGraphicsContext2D();
        drawBarChart(100, 300, avgHousingPricesByYear, Color.RED, 0, 4.4);
        // 4.4 = avgCommercialPricesByYear.max / avgHousingPricesByYear.max
        drawBarChart(100, 300, avgCommercialPricesByYear, Color.BLUE, (100/avgHousingPricesByYear.length), 1);
        gc = pieCanvas.getGraphicsContext2D();
        drawPieChart(purchasesByAgeGroup, pieColours);
    }

    public void drawBarChart(int w, int h, double[] data, Color color, int xStart, double scale) {
        gc.setFill(color);
        double maxVal = Double.NEGATIVE_INFINITY;
        for (double val : data) {
            if (val > maxVal)
                maxVal = val;
        }
        double barWidth = w / data.length;
        double x = xStart;
        for (double val : data) {
            double barHeight = (val / maxVal) * (h / scale);
            gc.fillRect(x, (h - barHeight), barWidth, barHeight);
            x += 3 * barWidth;
        }
    }

    public void drawPieChart(int[] data, Color[] color) {
        int sum = 0;
        for (int val : data) {
            sum += val;
        }
        gc.setStroke(Color.BLACK);
        double startAngle = 0;
        for (int i = 0; i < data.length; i++) {
            double angle = ((double) data[i] / (double) sum) * 360;
            gc.setFill(color[i]);
            gc.fillArc(30,20,270,270, startAngle, angle, ArcType.ROUND);
            gc.strokeArc(30,20,270,270, startAngle, angle, ArcType.ROUND);
            startAngle += angle;
        }
    }
}
