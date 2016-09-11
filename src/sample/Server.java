package sample;

import javafx.scene.canvas.GraphicsContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Yehia830 on 9/6/16.
 */
public class Server implements Runnable {
    private GraphicsContext graphicsContextServer;

    public GraphicsContext getGraphicsContextServer() {
        return graphicsContextServer;
    }

    public void setGraphicsContextServer(GraphicsContext graphicsContextServer) {
        this.graphicsContextServer = graphicsContextServer;
    }

    public Server(GraphicsContext graphicsContextServer) {
        this.graphicsContextServer = graphicsContextServer;
    }
    public Server(){

    }

    @Override
    public void run() {

        try {
            serverRun(graphicsContextServer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void serverRun(GraphicsContext graphicsContextServer) throws IOException{
        Main main = new Main();

        ServerSocket listener = new ServerSocket(8005);

        Socket clientSocket = listener.accept();

        System.out.println("New Connection from " + clientSocket.getInetAddress().getHostAddress());

        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);

        String clientString;

        while((clientString = inputFromClient.readLine()) != null){
            System.out.println("Printing out the client's stroke....:" + clientString);
            Stroke deserializedStroke = main.jsonDeserializeStroke(clientString);
            graphicsContextServer.strokeOval(deserializedStroke.getxCord(), deserializedStroke.getyCord(), deserializedStroke.getStrokeSize(), deserializedStroke.getStrokeSize());

            outputToClient.println("These are your strokes!");
        }




    }
}
