module org.unifimes.gestaoescolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires jdk.jconsole;
    requires org.postgresql.jdbc;
    requires org.apache.pdfbox;


    opens org.unifimes.gestaoescolar to javafx.fxml;
    exports org.unifimes.gestaoescolar;
}