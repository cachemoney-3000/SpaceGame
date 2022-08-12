module com.game.firstgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.game.firstgame to javafx.fxml;
    exports com.game.firstgame;
}