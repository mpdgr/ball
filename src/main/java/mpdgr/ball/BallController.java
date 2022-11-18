package mpdgr.ball;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class BallController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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
        space.getGraphicsContext2D().fill();

        Canvas ground = new Canvas(800, 100);
        ground.getGraphicsContext2D().setFill(Color.CYAN);
        ground.getGraphicsContext2D().fill();

        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setCenter(space);
        root.setBottom(ground);

        Scene scene = new Scene(root);
        stage.setTitle("Ball");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}