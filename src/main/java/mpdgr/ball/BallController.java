package mpdgr.ball;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class BallController extends Application {
    @Override
    public void start(Stage stage) {

        GridPane topPanel = new GridPane();
        topPanel.setMinSize(400, 100);
        topPanel.setPadding(new Insets(10, 10, 10, 10));
        topPanel.setVgap(5);
        topPanel.setHgap(15);
        topPanel.setStyle("-fx-background-color: #F6F6F6");

        Label xVelocityLabel = new Label("Horizontal start speed:");
        TextField xVelocityField = new TextField ();
        xVelocityField.setMaxWidth(60);

        Label yVelocityLabel = new Label("Vertical start speed:");
        TextField yVelocityField = new TextField ();
        yVelocityField.setMaxWidth(60);

        Label gravityLabel = new Label("Gravity:");
        TextField gravityField = new TextField ();
        gravityField.setMaxWidth(60);

        Label rollingResLabel = new Label("Rolling resistance:");
        TextField rollingResField = new TextField ();
        rollingResField.setMaxWidth(60);

        Label bouncingResLabel = new Label("Bounce energy loss:");
        TextField bouncingResField = new TextField ();
        bouncingResField.setMaxWidth(60);

        Button startButton = new Button("Start!");
        startButton.setMinWidth(100);

        Button stopButton = new Button("Stop");
        stopButton.setMinWidth(100);

        topPanel.add(startButton, 0, 0);
        topPanel.add(stopButton, 0, 1);
        topPanel.add(xVelocityLabel, 1, 0);
        topPanel.add(xVelocityField, 2, 0);
        topPanel.add(yVelocityLabel, 1, 1);
        topPanel.add(yVelocityField, 2, 1);
        topPanel.add(gravityLabel, 1, 2);
        topPanel.add(gravityField, 2, 2);
        topPanel.add(rollingResLabel, 1, 3);
        topPanel.add(rollingResField, 2, 3);
        topPanel.add(bouncingResLabel, 1, 4);
        topPanel.add(bouncingResField, 2, 4);

        Canvas space = new Canvas(800, 400);
        space.getGraphicsContext2D().setFill(Color.BLACK);
        space.getGraphicsContext2D().fillRect(0, 0, space.getWidth(), space.getHeight());

        Canvas ground = new Canvas(800, 50);
        ground.getGraphicsContext2D().setFill(Color.CYAN);
        ground.getGraphicsContext2D().fillRect(0, 0, ground.getWidth(), ground.getHeight());

        BorderPane root = new BorderPane();
        root.setTop(topPanel);
        root.setCenter(space);
        root.setBottom(ground);

        BallView ballView = new BallView();
        double ballDiameter = 20;
        double startPositionX = ballDiameter * 2;
        double startPositionY = (int) space.getHeight() - ballDiameter / 2;
        Position startPosition = new Position(startPositionX, startPositionY);
        ballView.drawBall(space.getGraphicsContext2D(), startPosition);

        Physics physics = new Physics(-1000, 0.1, -0.5);
        AtomicReference<BallModel> model = new AtomicReference<>(new BallModel(startPosition));

        xVelocityField.textProperty().setValue(String.valueOf(model.get().getxVelocity()));
        xVelocityField.textProperty().addListener((o, oldValue, newValue) -> {
            model.get().setxVelocity(Double.parseDouble(newValue));
        });

        yVelocityField.textProperty().setValue(String.valueOf(model.get().getyVelocity()));
        yVelocityField.textProperty().addListener((o, oldValue, newValue) -> {
            model.get().setyVelocity(Double.parseDouble(newValue));
        });

        gravityField.textProperty().setValue(String.valueOf(physics.getGravAcc()));
        gravityField.textProperty().addListener((o, oldValue, newValue) -> {
            physics.setGravAcc(Double.parseDouble(newValue));
        });

        rollingResField.textProperty().setValue(String.valueOf(physics.getRollingResistance()));
        rollingResField.textProperty().addListener((o, oldValue, newValue) -> {
            physics.setRollingResistance(Double.parseDouble(newValue));
        });

        bouncingResField.textProperty().setValue(String.valueOf(physics.getEnergyLoss()));
        bouncingResField.textProperty().addListener((o, oldValue, newValue) -> {
            physics.setEnergyLoss(Double.parseDouble(newValue));
        });

        startButton.setOnAction(event -> startBall(space, model.get()));

        stopButton.setOnAction(event -> {
            model.get().setTerminated(true);
            Physics physicsUpdate = new Physics(Double.parseDouble(gravityField.textProperty().get()),
                    Double.parseDouble(bouncingResField.textProperty().get()),
                    Double.parseDouble(rollingResField.textProperty().get()));
            BallModel modelUpdate = new BallModel(physicsUpdate, startPosition,
                    Double.parseDouble(xVelocityField.textProperty().get()),
                    Double.parseDouble(yVelocityField.textProperty().get()));
            model.set(modelUpdate);
        }
        );

        Scene scene = new Scene(root);
        stage.setTitle("Ball");
        stage.setScene(scene);
        stage.show();
    }

    void startBall(Canvas space, BallModel model){
        long delay = 0;
        model.timeStart();
        Timer timer = new Timer();
        TimerTask startBall = new TimerTask() {
            @Override
            public void run() {
                redrawSpace(space, model.computePosition());
                if (model.isTerminated()) {
                    cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(startBall, delay, 5);
    }

    void redrawSpace(Canvas space, Position ballPosition){
        space.getGraphicsContext2D().setFill(Color.BLACK);
        space.getGraphicsContext2D().fillRect(0, 0, space.getWidth(), space.getHeight());
        BallView ballView = new BallView();
        ballView.drawBall(space.getGraphicsContext2D(), ballPosition);
    }

    public static void main(String[] args) {
        launch();
    }
}




//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                redrawSpace(space, model.computePosition());
//            }
//        };
//        timer.start();