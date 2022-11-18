module mpdgr.ball {
    requires javafx.controls;
    requires javafx.fxml;


    opens mpdgr.ball to javafx.fxml;
    exports mpdgr.ball;
}