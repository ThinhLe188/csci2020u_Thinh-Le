package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Server extends Application {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected MessageThread[] threads = null;
    protected int numClients = 0;
    public TextArea messages;

    public static int MAX_CLIENTS = 50;
    public static int SERVER_PORT = 6868;

    @Override
    public void start(Stage primaryStage) {
        Server server = new Server();
        primaryStage.setTitle("SimpleBBS Server v1.0");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(10);
        messages = new TextArea();
        messages.setEditable(false);
        messages.setPrefSize(355,300);
        Button exit = new Button("Exit");
        grid.add(messages,0,0);
        grid.add(exit,0,1);
        primaryStage.setScene(new Scene(grid, 400, 375));
        primaryStage.show();

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                Platform.runLater(() -> messages.appendText("--- Server started --- Listening to port: " + SERVER_PORT + " ---\n"));
                threads = new MessageThread[MAX_CLIENTS];
                while (true) {
                    clientSocket = serverSocket.accept();
                    threads[numClients] = new MessageThread(clientSocket, this);
                    threads[numClients].start();
                    numClients++;
                    if(numClients == MAX_CLIENTS) {
                        numClients = 0;
                    }
                }
            } catch (IOException e) {
                messages.appendText(e.toString() + "\n");
            }
        }).start();

        exit.setOnAction(actionEvent -> {
            try {
                serverSocket.close();
                primaryStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
