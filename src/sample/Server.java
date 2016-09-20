package sample;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import jodd.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
    private GraphicsContext serverGC;
    private boolean myTurn = true;

    public Server(GraphicsContext gc) {
        this.serverGC = gc;
    }

    public void run() {
        try {
            startServer(serverGC);
        } catch (IOException exception) {
            System.out.println("Caught exception creating server socket or accepting client socket...");
            exception.printStackTrace();
        }
    }

    public void startServer(GraphicsContext serverGC) throws IOException {
        ServerSocket serverListener = new ServerSocket(8005);
        System.out.println("Listener ready to accept connections");

        Socket clientSocket = serverListener.accept();

        myTurn = false;

        System.out.println("myMain myTurn should be false: " + myTurn);

        System.out.println("Incoming connection from " + clientSocket.getInetAddress().getHostAddress());

        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);


        String clientInput;
        while ((clientInput = inputFromClient.readLine()) != null) {
            System.out.println("From ben: " + clientInput);
            if (clientInput.equals("switch")) {
                myTurn = !myTurn;
            }
            if (clientInput.substring(0,2).equals("0x")) {

                Paint myColor = Color.valueOf(clientInput);

                serverGC.setStroke(myColor);
            }
            if (!clientInput.equals("switch") && !clientInput.substring(0,2).equals("0x")) {
                Stroke deserializedStroke = jsonDeserializeStroke(clientInput);

                serverGC.strokeOval(deserializedStroke.getxCoordinate(), deserializedStroke.getyCoordinate(), deserializedStroke.getStrokeSize(), deserializedStroke.getStrokeSize());


            }
        }

    }
    public Stroke jsonDeserializeStroke (String jsonString) {
        JsonParser myParser = new JsonParser();
        Stroke myStrokeObject = myParser.parse(jsonString, Stroke.class);
        return myStrokeObject;
    }





    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }
}
