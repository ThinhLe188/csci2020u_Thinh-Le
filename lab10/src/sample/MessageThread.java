package sample;

import javafx.application.Platform;

import java.io.*;
import java.net.*;

public class MessageThread extends Thread{
    Socket socket;
    Server server;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    public MessageThread(Socket socket, Server server) {
        super();
        this.socket = socket;
        this.server = server;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Platform.runLater(() -> server.messages.appendText("--- Client connected ---\n"));
        boolean endOfSession = false;
        while (!endOfSession) {
            endOfSession = processCommand();
        }
        try {
            Platform.runLater(() -> server.messages.appendText("--- Client disconnected ---\n"));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processCommand() {
        String command;
        try {
            command = dataInputStream.readUTF();
            System.out.println(command);
        } catch (IOException e) {
            return true;
        }

        if (command.equalsIgnoreCase("SEND")) {
            try {
                synchronized (this) {
                    String message = dataInputStream.readUTF();
                    System.out.println(message);
                    Platform.runLater(() -> server.messages.appendText(message + "\n"));
                    return false;
                }
            } catch (IOException e) {
                return true;
            }
        }
        return true;
    }
}
