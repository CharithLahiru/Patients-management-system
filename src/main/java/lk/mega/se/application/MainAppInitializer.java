package lk.mega.se.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.mega.se.data.DBConnection;
import lk.mega.se.data.DataSaveRetrieve;
import lk.mega.se.data.DataSaveRetrieveImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class MainAppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                if (DBConnection.getInstance().getConnection()!=null && !DBConnection.getInstance().getConnection().isClosed()){
                    DBConnection.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        DataSaveRetrieve dataSaveRetrieve = new DataSaveRetrieveImpl();
        dataSaveRetrieve.generateTables();
        primaryStage.setScene( new Scene(FXMLLoader.load(getClass().getResource("/view/AddPatient.fxml"))));
        primaryStage.setTitle("Patients Details");
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

}
