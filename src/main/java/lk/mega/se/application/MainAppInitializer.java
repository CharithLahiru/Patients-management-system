package lk.mega.se.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene( new Scene(FXMLLoader.load(getClass().getResource("/view/AddPatient.fxml"))));
        primaryStage.setTitle("Patients Details");
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.setMaximized(true);
    }
}
