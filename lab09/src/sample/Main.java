package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {
    private String sURL1, sURL2;
    private ArrayList<Double> data1, data2;
    private double xWindow = 825, yWindow = 605;
    double xCanvas = xWindow - 100;
    double yCanvas = yWindow - 100;
    double axisWidth = 5;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Lab 09: Stock Performance");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(50,50,50,50));
        grid.setHgap(10);
        grid.setVgap(10);

        Label stockSymbol = new Label("Stock symbols: ");
        Label startPeriodLb = new Label("Start period: ");
        Label endPeriodLb = new Label("End period: ");
        Label intervalLb = new Label("Interval: ");
        Button submitBtn = new Button("Submit");
        Label invalidInput = new Label();
        invalidInput.setTextFill(Color.RED);

        Button backBtn = new Button("Back to Main");

        TextField stockSymbolTx1 = new TextField();
        TextField stockSymbolTx2 = new TextField();
        DatePicker startDPr = new DatePicker();
        DatePicker endDPr = new DatePicker();
        ComboBox intervalCBx = new ComboBox();
        intervalCBx.getItems().addAll("Daily", "Weekly", "Monthly");

        grid.add(stockSymbol,0,0);
        grid.add(stockSymbolTx1,1,0);
        grid.add(stockSymbolTx2,1,1);
        grid.add(startPeriodLb,0,2);
        grid.add(startDPr,1,2);
        grid.add(endPeriodLb,0,3);
        grid.add(endDPr,1,3);
        grid.add(intervalLb,0,4);
        grid.add(intervalCBx,1,4);
        grid.add(invalidInput,1,5);
        grid.add(submitBtn,1,6);

        Scene mainScene = new Scene(grid, xWindow, yWindow);

        submitBtn.setOnAction(actionEvent -> {
            try {
                invalidInput.setText("");
                String stockSymbol1 = stockSymbolTx1.getText();
                String stockSymbol2 = stockSymbolTx2.getText();

                String interval = "";
                switch (intervalCBx.getValue().toString()) {
                    case "Daily":
                        interval = "interval=1d";
                        break;
                    case "Weekly":
                        interval = "interval=1wk";
                        break;
                    case "Monthly":
                        interval = "interval=1mo";
                        break;
                    default:
                        interval = "interval=";
                }

                LocalDate startPeriod = startDPr.getValue();
                LocalDate endPeriod = endDPr.getValue();
                long startPeriodEpoch = startPeriod.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.ofHours(-5));
                long endPeriodEpoch = endPeriod.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.ofHours(-5));
                String period1 = "period1=" + String.valueOf(startPeriodEpoch);
                String period2 = "period2=" + String.valueOf(endPeriodEpoch);

                sURL1 = "https://query1.finance.yahoo.com/v7/finance/download/"
                        + stockSymbol1 + "?" + period1 + "&" + period2 + "&" + interval
                        + "&events=history&includeAdjustedClose=true";
                sURL2 = "https://query1.finance.yahoo.com/v7/finance/download/"
                        + stockSymbol2 + "?" + period1 + "&" + period2 + "&" + interval
                        + "&events=history&includeAdjustedClose=true";

                data1 = new ArrayList<>();
                data2 = new ArrayList<>();
                downloadStockPrices(sURL1, data1);
                downloadStockPrices(sURL2, data2);
                System.out.println(sURL1);
                System.out.println(sURL2);

                Canvas canvas = new Canvas(xCanvas, yCanvas);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawLinePlot(gc);

                Text tx1 = new Text(stockSymbolTx1.getText());
                tx1.setFill(Color.RED);
                tx1.setFont(Font.font("Arial", 20));
                Text tx2 = new Text("   -   ");
                tx2.setFont(Font.font("Arial", 20));
                Text tx3 = new Text(stockSymbolTx2.getText());
                tx3.setFill(Color.BLUE);
                tx3.setFont(Font.font("Arial", 20));
                TextFlow legend = new TextFlow();
                legend.setPrefHeight(20);
                legend.getChildren().addAll(tx1,tx2,tx3);

                GridPane grid2 = new GridPane();
                grid2.setAlignment(Pos.TOP_CENTER);
                HBox hBoxTop = new HBox(backBtn);
                hBoxTop.setAlignment(Pos.CENTER_LEFT);
                hBoxTop.setPrefHeight(50);
                HBox hBoxBot = new HBox(legend);
                HBox.setMargin(legend, new Insets(15,15,15,15));
                hBoxBot.setAlignment(Pos.CENTER);
                hBoxBot.setPrefHeight(50);

                grid2.add(hBoxTop,0,0);
                grid2.add(canvas,0,1);
                grid2.add(hBoxBot,0,2);
                primaryStage.setScene(new Scene(grid2,xWindow,yWindow));

            } catch (IOException | NullPointerException | NumberFormatException e) {
                // e.printStackTrace();
                invalidInput.setText("Invalid Input");
                stockSymbolTx1.clear();
                stockSymbolTx2.clear();
                startDPr.setValue(null);
                endDPr.setValue(null);
                intervalCBx.valueProperty().set(null);
            }
        });

        backBtn.setOnAction(actionEvent -> {
            primaryStage.setScene(mainScene);
        });

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void downloadStockPrices(String sURL, ArrayList<Double> data) throws IOException {
        URL netURL = new URL(sURL);
        URLConnection conn = netURL.openConnection();
        conn.setDoOutput(false);
        conn.setDoInput(true);
        InputStreamReader inputCSV = new InputStreamReader(((URLConnection) conn).getInputStream());
        BufferedReader br = new BufferedReader(inputCSV);

        br.readLine();
        String line = "";
        while ((line = br.readLine())!=null) {
            String[] columns = line.split(",");
            data.add(Double.valueOf(columns[4]));
        }
    }

    public void plotLine(GraphicsContext gc, ArrayList<Double> data, Color color, double xInc, double yInc) {
        gc.setStroke(color);
        gc.setLineWidth(1);
        double xInit = 0;
        double yInit = yCanvas - (data.get(0) * yInc);
        double x,y;
        for (double value : data) {
            x = xInit + xInc;
            y = yCanvas - (value * yInc);
            gc.strokeLine(xInit, yInit, x, y);
            xInit = x;
            yInit = y;
        }
    }

    public void drawLinePlot(GraphicsContext gc) {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(axisWidth);
        gc.strokeLine(0,0,0,yCanvas);
        gc.strokeLine(0,yCanvas,xCanvas,yCanvas);

        double max = (Collections.max(data1) > Collections.max(data2)) ? Collections.max(data1) : Collections.max(data2);
        double yInc = yCanvas / max;
        double xInc = xCanvas / data1.size();
        plotLine(gc, data1, Color.RED, xInc, yInc);
        plotLine(gc, data2, Color.BLUE, xInc, yInc);
    }
}
