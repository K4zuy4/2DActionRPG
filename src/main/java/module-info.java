module org.projectgame.project2dgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires annotations;

    opens org.projectgame.project2dgame to javafx.fxml;
    exports org.projectgame.project2dgame;

    opens org.projectgame.project2dgame.Controller to javafx.fxml;
    exports org.projectgame.project2dgame.Controller;
    exports org.projectgame.project2dgame.GameField;
    opens org.projectgame.project2dgame.GameField to javafx.fxml;
    exports org.projectgame.project2dgame.GameField.TileManagement;
    opens org.projectgame.project2dgame.GameField.TileManagement to javafx.fxml;
    exports org.projectgame.project2dgame.Entities;
    opens org.projectgame.project2dgame.Entities to javafx.fxml;
}