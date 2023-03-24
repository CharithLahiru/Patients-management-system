package lk.mega.se.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientDetailImpl implements PatientDetails {

    public PatientDetailImpl() {
        AddPatientController addPatientController = new AddPatientController();
    }

    @Override
    public Patient createPatient() {

        return null;
    }

    @Override
    public Patient updatePatient() {
        return null;
    }

    @Override
    public void getPatientUI() {
        Stage stage = new Stage();
        stage.setTitle("Add Patient");
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AddPatient.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.centerOnScreen();
        stage.show();

    }
}
