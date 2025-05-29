package org.unifimes.gestaoescolar;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import org.unifimes.gestaoescolar.dao.Session;

import java.io.IOException;

public class HomeApplication extends Application {
    private static Stage stage;
    private static Scene SceneLogin;
    private static Scene SceneHome;
    private static Scene SceneDashboard;
    private static Scene SceneUsuarios;


    @Override
    public void start(Stage stage) throws IOException {

        String startview = "login-view.fxml";
        if(Session.isLoggedUser()){
            startview = "home-view.fxml";
        }

        HomeApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(startview));

        Parent fxmlHome = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        SceneHome = new Scene(fxmlHome);

        Parent fxmlDashboard = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
        SceneDashboard = new Scene(fxmlDashboard);

        Parent fxmlUsuarios = FXMLLoader.load(getClass().getResource("usuarios-view.fxml"));
        SceneUsuarios = new Scene(fxmlUsuarios);

        SceneLogin = new Scene(fxmlLoader.load(),800,500);
        SceneLogin.getStylesheets().add(getClass().getResource("styles/styles.css").toExternalForm());
        stage.setTitle("SIGE: Sistema de Gestão Escolar");
        stage.setScene(SceneLogin);

        if(Session.isLoggedUser()) {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void abrirScene(String scene) {
        switch (scene) {
            case "home":
                stage.setScene(SceneHome);
                break;
            case "dashboard":
                stage.setScene(SceneDashboard);
                break;
            case "login":
                stage.setScene(SceneLogin);
                break;
            case "usuarios":
                stage.setScene(SceneUsuarios);
                break;
        }

        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
        });
    }

}