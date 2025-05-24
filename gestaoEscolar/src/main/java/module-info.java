module org.unifimes.gestaoescolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;


    opens org.unifimes.gestaoescolar to javafx.fxml;
    exports org.unifimes.gestaoescolar;
}