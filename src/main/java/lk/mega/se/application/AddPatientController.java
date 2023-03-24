package lk.mega.se.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import lk.mega.se.data.DataSaveRetrieve;
import lk.mega.se.data.DataSaveRetrieveImpl;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddPatientController {

    public TextField txtIDNumber;
    public TextField txtPassportNumber;
    public Button btnNew;
    public Button btnAdd;
    public Button btnDelete;
    public ListView lstContactNumbers;
    public RadioButton tglMale;
    public ToggleGroup gender;
    public ImageView imgPatient;
    public TableView<Patient> tblPatients;
    public Button btnBrowse;
    public Button btnImgRemove;
    public RadioButton tglFemale;
    @FXML
    private Button btnSave;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnRemove;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtAge;

    @FXML
    private DatePicker txtBirthday;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtHeight;

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtNote;

    @FXML
    private TextField txtPatientNumber;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtWeight;

    private DataSaveRetrieve dataSaveRetrieve = new DataSaveRetrieveImpl();
    private boolean isValidate;
    private TextField[] textFields;

    public void initialize() {
        testingData();

        textFields = new TextField[]{txtPatientNumber, txtName, txtIDNumber, txtPassportNumber, txtAge, txtWeight, txtHeight, txtPhoneNumber, txtEmail};

        txtPhoneNumber.textProperty().addListener((observableValue, previous, current) -> {
            if (validateContactNumber(txtPhoneNumber.getText())) btnAdd.setDisable(false);
        });

        lstContactNumbers.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            if (!lstContactNumbers.getSelectionModel().getSelectedItems().isEmpty()) btnRemove.setDisable(true);
            else btnRemove.setDisable(false);
        });

        tblPatients.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("imageView"));
        tblPatients.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
        tblPatients.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblPatients.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        tblPatients.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("passportNumber"));
        tblPatients.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("gender"));
        tblPatients.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        tblPatients.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("email"));

        tblPatients.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            Patient patient;
            if ((patient = tblPatients.getSelectionModel().getSelectedItem()) != null) {
                patientGetData(patient);
                btnDelete.setDisable(false);
                btnHistory.setDisable(false);
            } else {
                btnDelete.setDisable(true);
                btnHistory.setDisable(true);
            }
        });
    }

    private void testingData() {
        txtPatientNumber.setText("P0001");
        txtName.setText("alice");
        txtIDNumber.setText("568954795V");
        txtPassportNumber.setText("G-45152");
        gender.selectToggle(tglMale);
        txtWeight.setText("65");
        txtHeight.setText("160");
        txtAddress.setText("rawathawaththa road, moratuwa.");
        lstContactNumbers.getItems().add("071-4596854");
        lstContactNumbers.getItems().add("077-2536348");
        txtEmail.setText("vishaka@gmail.com");
        txtNote.setText("This data for testing purposes");
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        for (TextField textField : textFields) {
            textField.clear();
        }
        txtPatientNumber.setText(generatePatientNumber());
        txtBirthday.setValue(null);
        gender.getSelectedToggle().setSelected(false);
        txtAddress.clear();
        lstContactNumbers.getItems().clear();
        txtNote.clear();

        txtName.requestFocus();

        btnHistory.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    void btnHistoryOnAction(ActionEvent event) {

    }

    public void btnBrowseOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Patient photo");
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());
        if (file == null) return;
        try {
            imgPatient.setImage(new Image(file.toURI().toString(), 150, 150, false, false));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Chosen file can not lode").showAndWait();
            e.printStackTrace();
            return;
        }
        btnImgRemove.setDisable(false);
    }

    public void btnImgRemoveOnAction(ActionEvent actionEvent) {
        imgPatient.setImage(new Image("/images/user.png"));
        btnImgRemove.setDisable(true);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        lstContactNumbers.getSelectionModel().clearSelection();
        for (Object number : lstContactNumbers.getItems()) {
            if (number.equals(txtPhoneNumber.getText())) {
                new Alert(Alert.AlertType.ERROR, "Number already exist").showAndWait();
                return;
            }
        }
        lstContactNumbers.getItems().add(txtPhoneNumber.getText());
        txtPhoneNumber.clear();
    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        int selectedIndex = lstContactNumbers.getSelectionModel().getSelectedIndex();
        lstContactNumbers.getItems().remove(selectedIndex);
        lstContactNumbers.getSelectionModel().clearSelection();
    }

    @FXML
    void btnSavaOnAction(ActionEvent event) {
        isValidate = false;
        validateData();
        if (!isValidate) return;

        ArrayList<String> numberList = new ArrayList<>();
        for (Object num : lstContactNumbers.getItems()) {
            numberList.add(num.toString());
        }
        dataSaveRetrieve.updatePatientDatabase(
                txtPatientNumber.getText(),
                txtName.getText(),
                txtIDNumber.getText(),
                txtPassportNumber.getText(),
                txtBirthday.getValue(),
                (gender.getSelectedToggle() == tglMale) ? "Male" : "Female",
                Double.parseDouble(txtWeight.getText()),
                Double.parseDouble(txtHeight.getText()),
                txtAddress.getText(),
                numberList,
                txtEmail.getText(),
                txtNote.getText()
        );

        Patient patient = patientSetData();
        tblPatients.getItems().add(patient);
        btnNew.fire();
    }

    private void dataRetrieve() {

    }

    private boolean validateContactNumber(String number) {
        if (txtPhoneNumber.getText().trim().length() < 4) return false;
        return true;
    }

    private Patient patientSetData() {
        Patient patient = new Patient();

        Image image = new Image(imgPatient.getImage().getUrl(), 50, 50, true, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        patient.setImageView(imageView);

        patient.setPatientNumber(txtPatientNumber.getText());
        patient.setName(txtName.getText());
        patient.setIdNumber(txtIDNumber.getText());
        patient.setPassportNumber(txtPassportNumber.getText());
        patient.setBirthday(txtBirthday.getValue());
        patient.setWeight(Integer.valueOf(txtWeight.getText().trim()));
        patient.setHeight(Integer.valueOf(txtHeight.getText().trim()));
        patient.setAddress(txtAddress.getText());
        patient.setContactNumber(txtPhoneNumber.getText());
        patient.setEmail(txtEmail.getText());
        patient.setNote(txtNote.getText());
        return patient;
    }

    private void validateData() {
        txtPatientNumber.getStyleClass().remove("invalid");
        txtName.getStyleClass().remove("invalid");
        txtIDNumber.getStyleClass().remove("invalid");
        txtPassportNumber.getStyleClass().remove("invalid");
        txtBirthday.getStyleClass().remove("invalid");
        txtWeight.getStyleClass().remove("invalid");
        txtHeight.getStyleClass().remove("invalid");
        txtAddress.getStyleClass().remove("invalid");
        txtPhoneNumber.getStyleClass().remove("invalid");
        lstContactNumbers.getStyleClass().remove("invalid");
        txtEmail.getStyleClass().remove("invalid");

        isValidate = true;

        try {
            LocalDate value = txtBirthday.getValue();
        } catch (Exception e) {
            isValidate = false;
            txtBirthday.getStyleClass().add("invalid");
        }

        if (txtPassportNumber.getText().isEmpty()) {
            isValidate = false;
            txtPassportNumber.getStyleClass().add("invalid");
        }

        if (txtIDNumber.getText().isEmpty()) {
            isValidate = false;
            txtIDNumber.getStyleClass().add("invalid");
        }

        if (txtName.getText().isEmpty()) {
            isValidate = false;
            txtName.getStyleClass().add("invalid");
        }

        if (txtHeight.getText().isEmpty()) {
        } else {
            try {
                Double.parseDouble(txtHeight.getText());
            } catch (Exception e) {
                isValidate = false;
                txtHeight.getStyleClass().add("invalid");
            }
        }

        if (txtWeight.getText().isEmpty()) {
        } else {
            try {
                Double.parseDouble(txtWeight.getText());
            } catch (Exception e) {
                isValidate = false;
                txtWeight.getStyleClass().add("invalid");
            }
        }

        if (lstContactNumbers.getItems().size() == 0) {
            isValidate = false;
            lstContactNumbers.getStyleClass().add("invalid");
            txtPhoneNumber.requestFocus();
        }
    }

    private void patientGetData(Patient patient) {
        imgPatient.setImage(patient.getImageView().getImage());
        txtPatientNumber.setText(patient.getPatientNumber());
        txtName.setText(patient.getName());
        txtIDNumber.setText(patient.getIdNumber());
        txtPassportNumber.setText(patient.getPassportNumber());
        txtBirthday.setValue(patient.getBirthday());
        gender.selectToggle((patient.getGender() == Gender.MALE) ? tglMale : tglFemale);
        txtAge.setText(patient.getAge() + "");
        txtWeight.setText(patient.getWeight() + "");
        txtHeight.setText(patient.getHeight() + "");
        txtAddress.setText(patient.getAddress());
        txtPhoneNumber.setText(patient.getContactNumber());
        txtEmail.setText(patient.getEmail());
        txtNote.setText(patient.getNote());
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        tblPatients.getItems().remove(tblPatients.getSelectionModel().getSelectedIndex());
        tblPatients.getSelectionModel().clearSelection();
        btnNew.fire();
    }

    private String generatePatientNumber() {
        return "P0001";
    }

    @FXML
    void tXtPhoneNumberOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtAgeOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtHeightOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtNameOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtPatientNumberOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtWeightOnKeyReleased(KeyEvent event) {

    }
}
