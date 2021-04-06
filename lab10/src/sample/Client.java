package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Application {
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    public static String SERVER_ADDRESS = "localhost";
    public static int SERVER_PORT = 6868;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SimpleBBS Client v1.0");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(10);
        Label nameLb = new Label("Username:");
        Label messageLb = new Label("Message:");
        TextField nameTx = new TextField();
        TextField messageTx = new TextField();
        Button send = new Button("Send");
        Button exit = new Button("Exit");
        Label statusLb = new Label();

        grid.add(nameLb,0,0);
        grid.add(nameTx,1,0,2,1);
        grid.add(messageLb,0,1);
        grid.add(messageTx,1,1,2,1);
        grid.add(send,0,2);
        grid.add(exit,0,3);
        grid.add(statusLb,1,2);

        primaryStage.setScene(new Scene(grid, 300, 275));
        primaryStage.show();

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            statusLb.setTextFill(Color.GREEN);
        } catch (UnknownHostException e) {
            statusLb.setText("Unknown host: " + SERVER_ADDRESS);
            statusLb.setTextFill(Color.RED);
        } catch (IOException e) {
            statusLb.setText("IOException while connecting to server: " + SERVER_ADDRESS);
            statusLb.setTextFill(Color.RED);
        }
        if (socket == null) {
            statusLb.setText("Socket is null");
            statusLb.setTextFill(Color.RED);
        }
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            statusLb.setText("IOException while opening a read/write connection");
            statusLb.setTextFill(Color.RED);
        }

        send.setOnAction(actionEvent -> {
            try {
                dataOutputStream.writeUTF("SEND");
                dataOutputStream.flush();
                String message;
                if (nameTx.getText().isBlank()) {
                    message = "Unknown: " + messageTx.getText();
                } else {
                    message = nameTx.getText() + ": " + messageTx.getText();
                }
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                statusLb.setText("Message sent");
                statusLb.setTextFill(Color.GREEN);
                messageTx.clear();
            } catch (IOException e) {
                statusLb.setText("Error while sending message");
                statusLb.setTextFill(Color.RED);
            }
        });

        exit.setOnAction(actionEvent -> {
            try {
                socket.close();
                primaryStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) { launch(args); }
}
