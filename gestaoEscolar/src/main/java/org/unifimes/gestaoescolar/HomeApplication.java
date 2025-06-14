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
    private static Scene SceneAlunos;
    private static Scene SceneFaltas;
    private static Scene SceneNotas;
    private static Scene SceneTurmas;


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

        Parent fxmlAlunos = FXMLLoader.load(getClass().getResource("alunos-view.fxml"));
        SceneAlunos = new Scene(fxmlAlunos);

        Parent fxmlFaltas = FXMLLoader.load(getClass().getResource("faltas-view.fxml"));
        SceneFaltas = new Scene(fxmlFaltas);

        Parent fxmlnotas = FXMLLoader.load(getClass().getResource("notas-view.fxml"));
        SceneNotas = new Scene(fxmlnotas);

        Parent fxmlturmas = FXMLLoader.load(getClass().getResource("turmas-view.fxml"));
        SceneTurmas = new Scene(fxmlturmas);

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
            case "alunos":
                stage.setScene(SceneAlunos);
                break;
            case "faltas":
                stage.setScene(SceneFaltas);
                break;
            case "notas":
                stage.setScene(SceneNotas);
                break;
            case "turmas":
                stage.setScene(SceneTurmas);
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