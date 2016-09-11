package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Main extends Application {
    final double DEFAULT_SCENE_WIDTH = 800;
    final double DEFAULT_SCENE_HEIGHT = 600;
    private boolean keepDrawing = true;
    private boolean isClientRunning = false;
    int strokeSize = 8;

    GraphicsContext secondGraphicsContext;

    long drawDelay = 0;
    long delayIncrements = 20;

    PrintWriter out;
    BufferedReader in;

    String jsonString = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);


        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Open second stage");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 0, 1);


        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
                startSecondStage();
            }
        });

        Button connectButton = new Button("Start Client");
        hbButton.getChildren().add(connectButton);

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startClient();

            }
        });

        Button serverButton = new Button("Start Server");
        hbButton.getChildren().add(serverButton);

        serverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Server myServer = new Server();
                    myServer.run();

                } catch (Exception ex) {

                }
            }
        });


        //addCanvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.setLineWidth(5);


        Server myServer = new Server(gc);
        Thread serverThread = new Thread(myServer);
        serverThread.start();

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
//                System.out.println("x: " + e.getX() + ", y: " + e.getY());
                if (keepDrawing) {
                    gc.strokeOval(e.getX(), e.getY(), strokeSize, strokeSize);
                    // save stroke to client's arrayList for replay button (NOT DONE YET)
//                    Stroke saveStroke = new Stroke(e.getX(), e.getY(), strokeSize);
//                    myClient.addStrokeToArrayList(saveStroke);

                    // To avoid error messages before second screen is open
                    if (secondGraphicsContext != null) {
                        // draw on second screen
                        secondGraphicsContext.strokeOval(e.getX(), e.getY(), strokeSize, strokeSize);
                    }

                
                }
            }
        });






        grid.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {
                System.out.println(e.getCode());
                System.out.println(e.getText());

                if (e.getText().equalsIgnoreCase("D")) {
                    System.out.println("Toggle drawing....");
                    keepDrawing = !keepDrawing;
                }

                if (e.getCode() == KeyCode.UP) {
                    strokeSize++;
                    int maxStrokeSize = 60;
                    if (strokeSize > maxStrokeSize) {
                        System.out.println("sample.Stroke size can't increase past " + maxStrokeSize + "!");
                        strokeSize = maxStrokeSize;
                    }
                }

                if (e.getCode() == KeyCode.DOWN) {
                    strokeSize--;
                    if (strokeSize < 1) {
                        System.out.println("sample.Stroke size must be at least 1!");
                        strokeSize = 1;
                    }
                }
            }
        });


        grid.add(canvas, 0, 2);

        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        primaryStage.setScene(defaultScene);
        primaryStage.show();
        
        
        
    }
    //sending stroke to server
    
    public void sendStroke(Stroke stroke){
        try{
            String serizalizedStroke = jsonSerialize(stroke);
           
            out.println(serizalizedStroke);

            System.out.println("Server says.... " + in.readLine());
            
            
            
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void startSecondStage() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Second Stage");

        // we're using a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);
//        grid.setPrefSize(primaryStage.getMaxWidth(), primaryStage.getMaxHeight());

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Sample paint button");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
            }
        });



        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);


        secondGraphicsContext = canvas.getGraphicsContext2D();
        secondGraphicsContext.setFill(Color.GREEN);
        secondGraphicsContext.setStroke(Color.BLUE);
        secondGraphicsContext.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        secondGraphicsContext.setLineWidth(5);



        grid.add(canvas, 0 ,2);

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);


        secondaryStage.setScene(defaultScene);
        System.out.println("About to show the second stage");

        secondaryStage.show();
    }









    public void startClient() {
        try {
            Socket clientSocket = new Socket("localhost", 8005);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            out.println(jsonString);
            String serverResponse = in.readLine();
            System.out.println(serverResponse);


//			startSecondStage();

            clientSocket.close();
        } catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }





    //JsonMethods

    public String jsonSerialize(Stroke myStroke) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(myStroke);
        return jsonString;
    }

    public Stroke jsonDeserializeStroke(String jsonString) {
        JsonParser myParser = new JsonParser();
        Stroke myStrokeObject = myParser.parse(jsonString, Stroke.class);
        return myStrokeObject;


    }


    public static void main(String[] args) {
        launch(args);
    }
}
