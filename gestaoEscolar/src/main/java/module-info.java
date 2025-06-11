module org.unifimes.gestaoescolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires jdk.jconsole;
    requires org.postgresql.jdbc;


    opens org.unifimes.gestaoescolar to javafx.fxml;
    exports org.unifimes.gestaoescolar;
}