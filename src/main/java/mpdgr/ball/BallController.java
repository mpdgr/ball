package mpdgr.ball;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class BallController extends Application {
    @Override
    public void start(Stage stage) {
        TilePane topPane = new TilePane(10, 10);
        topPane.setAlignment(Pos.CENTER);

        Button b1 = new Button("1");
        Button b2 = new Button("2");
        Button b3 = new Button("3");
        Button b4 = new Button("4");
        Button b5 = new Button("5");

        topPane.getChildren().add(b1);
        topPane.getChildren().add(b2);
        topPane.getChildren().add(b3);
        topPane.getChildren().add(b4);
        topPane.getChildren().add(b5);

        Canvas space = new Canvas(800, 400);
        space.getGraphicsContext2D().setFill(Color.BLACK);
        space.getGraphicsContext2D().fillRect(0, 0, space.getWidth(), space.getHeight());

        Canvas ground = new Canvas(800, 50);
        ground.getGraphicsContext2D().setFill(Color.CYAN);
        ground.getGraphicsContext2D().fillRect(0, 0, ground.getWidth(), ground.getHeight());

        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(space);
        root.setBottom(ground);

        BallView ballView = new BallView();
        double ballDiameter = 20;
        double startPositionX = ballDiameter * 2;
        double startPositionY = (int) space.getHeight() - ballDiameter / 2;
        Position startPosition = new Position(startPositionX, startPositionY);
        ballView.drawBall(space.getGraphicsContext2D(), startPosition);

        b1.setOnAction(event -> startBall(space, startPosition));

        Scene scene = new Scene(root);
        stage.setTitle("Ball");
        stage.setScene(scene);
        stage.show();
    }

    void startBall(Canvas space, Position startPosition){
        long delay = 0;
        long sTime = System.currentTimeMillis();
        BallModel model = new BallModel(startPosition);


        Timer timer = new Timer();
        TimerTask punchBall = new TimerTask() {
            @Override
            public void run() {
                redrawSpace(space, model.computePosition());
                if (model.isTerminated()) {
                    cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(punchBall, delay, 5);
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