package lk.mega.se.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import lk.mega.se.data.DataSaveRetrieve;
import lk.mega.se.data.DataSaveRetrieveImpl;

import java.io.File;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddPatientController {

    public Button btnNew;
    public Button btnAdd;
    public Button btnDelete;
    public ListView<String> lstContactNumbers;
    public RadioButton tglMale;
    public ToggleGroup gender;
    public ImageView imgPatient;
    public Button btnBrowse;
    public Button btnImgRemove;
    public RadioButton tglFemale;
    public ComboBox<String> cmbName;
    public ComboBox<String> cmbIdNumber;
    public ComboBox<String> cmbPassportNumber;
    @FXML
    private Button btnSave;

    @FXML
    private Button btnAddBill;

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
    private TextArea txtNote;

    @FXML
    private ComboBox<String> txtPatientNumber;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtWeight;

    private DataSaveRetrieve dataSaveRetrieve = new DataSaveRetrieveImpl();
    private boolean isValidate;
    private TextField[] textFields;
    private ArrayList<Patient> patients = new ArrayList<>();
    private int index = 0;

    public void initialize() throws SQLException {
        testingData();

        textFields = new TextField[]{txtAge, txtWeight, txtHeight, txtPhoneNumber, txtEmail}; // effect here should be recover

        txtPhoneNumber.textProperty().addListener((observableValue, previous, current) -> {
            if (validateContactNumber(txtPhoneNumber.getText())) btnAdd.setDisable(false);
        });

        lstContactNumbers.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            if (!lstContactNumbers.getSelectionModel().getSelectedItems().isEmpty()) btnRemove.setDisable(true);
            else btnRemove.setDisable(false);
        });

        searchPatientNumber();
        searchName();
        searchIdNumber();
        searchPassportNumber();
//        btnNew.fire();
    }

    private void searchPassportNumber() {
        cmbPassportNumber.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            if (cmbPassportNumber.getEditor().getText().isBlank()) {
                cmbPassportNumber.hide();
                return;
            }
            if (!cmbPassportNumber.getSelectionModel().isEmpty()) {
                index = cmbPassportNumber.getSelectionModel().getSelectedIndex();
                return;
            }
            patients.clear();
            cmbPassportNumber.getItems().clear();
            ResultSet resultSet = dataSaveRetrieve.searchPatientsPassportNumber(cmbPassportNumber.getEditor().getText());
            try {
                while (resultSet.next()) {
                    patients.add(patientSetDataFromDB(resultSet));
                    cmbPassportNumber.getItems().add(resultSet.getString("passport_number"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void searchIdNumber() {
        cmbIdNumber.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            if (cmbIdNumber.getEditor().getText().isBlank()) {
                cmbIdNumber.hide();
                return;
            }
            if (!cmbIdNumber.getSelectionModel().isEmpty()) {
                index = cmbIdNumber.getSelectionModel().getSelectedIndex();
                return;
            }
            patients.clear();
            cmbIdNumber.getItems().clear();
            ResultSet resultSet = dataSaveRetrieve.searchPatientsIdNumber(cmbIdNumber.getEditor().getText());
            try {
                while (resultSet.next()) {
                    patients.add(patientSetDataFromDB(resultSet));
                    cmbIdNumber.getItems().add(resultSet.getString("id_number"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void cmbNameOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            System.out.println(index);
            cmbName.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbName.hide();
            txtPatientNumber.hide();

        } else {
            cmbName.show();
        }
    }

    private void searchName() {

        cmbName.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            if (cmbName.getEditor().getText().isBlank()) {
                cmbName.hide();
                return;
            }
            if (!cmbName.getSelectionModel().isEmpty()) {
                index = cmbName.getSelectionModel().getSelectedIndex();
                return;
            }
            patients.clear();
            cmbName.getItems().clear();
            ResultSet resultSet = dataSaveRetrieve.searchPatientsName(cmbName.getEditor().getText());
            try {
                while (resultSet.next()) {
                    patients.add(patientSetDataFromDB(resultSet));
                    cmbName.getItems().add(resultSet.getString("name"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void searchPatientNumber() {
        txtPatientNumber.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            btnNew.fire();
            btnSave.setDisable(false);
            char[] chars = txtPatientNumber.getEditor().getText().toCharArray();
            if (chars.length < 1) return;
            if (chars[0] != 'P') return;
            if (chars.length != 4) return;
            for (int i = 1; i < 4; i++) {
                if (!Character.isDigit(chars[i])) return;
            }
            int number = Integer.parseInt(txtPatientNumber.getEditor().getText().substring(1, 4));
            ResultSet resultSet = dataSaveRetrieve.searchPatientsIdNumber(number);
            try {
                if (!resultSet.next()) return;
                txtPatientNumber.getItems().add(String.format("P%03d", (Integer.parseInt(resultSet.getString("patient_number")))));
                Patient patient = patientSetDataFromDB(resultSet);
                patientGetData(patient);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            txtPatientNumber.show();
            txtPatientNumber.getEditor().setEditable(false);
        });
    }

    private void testingData() {
        cmbName.getEditor().setText("alice");
        cmbIdNumber.getEditor().setText("568954795V");
        cmbPassportNumber.getEditor().setText("G-45152");
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
        for (TextField textField : textFields) {                // text feild used
            textField.clear();
        }
        imgPatient.setImage(new Image("/images/user.png"));
        txtPatientNumber.getEditor().setEditable(true);
        txtBirthday.setValue(null);
        if (gender.getSelectedToggle() != null) gender.getSelectedToggle().setSelected(false);
        txtAddress.clear();
        lstContactNumbers.getItems().clear();
        txtNote.clear();

        btnAddBill.setDisable(true);
        btnDelete.setDisable(true);
        txtPatientNumber.getItems().clear();
    }

    @FXML
    void btnAddBillOnAction(ActionEvent event) {
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
        btnAdd.setDisable(true);
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
                imgPatient.getImage(),
                cmbName.getEditor().getText(),
                cmbIdNumber.getEditor().getText(),
                cmbPassportNumber.getEditor().getText(),
                txtBirthday.getValue(),
                (gender.getSelectedToggle() == tglMale) ? "Male" : "Female",
                txtWeight.getText().trim().isBlank() ? null : Double.parseDouble(txtWeight.getText()),
                txtHeight.getText().trim().isBlank() ? null : Double.parseDouble(txtHeight.getText()),
                txtAddress.getText(),
                numberList,
                txtEmail.getText(),
                txtNote.getText()
        );

        btnNew.fire();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        dataSaveRetrieve.deletePatient(Integer.parseInt(txtPatientNumber.getEditor().getText().substring(1, 4)));
        btnNew.fire();
    }

    private boolean validateContactNumber(String number) {
        if (txtPhoneNumber.getText().trim().length() < 4) return false;
        return true;
    }

    private Patient patientSetDataFromField() {
        Patient patient = new Patient();

        Image image = new Image(imgPatient.getImage().getUrl(), 50, 50, true, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        patient.setImageView(imageView);

        patient.setPatientNumber(txtPatientNumber.getValue().toString());
        patient.setName(cmbName.getEditor().getText());
        patient.setIdNumber(cmbIdNumber.getEditor().getText());
        patient.setPassportNumber(cmbPassportNumber.getEditor().getText());
        patient.setBirthday(txtBirthday.getValue());
        try {
            patient.setWeight(Integer.valueOf(txtWeight.getText().trim()));
        } catch (NumberFormatException e) {
            txtWeight.setText("");
        }
        try {
            patient.setHeight(Integer.valueOf(txtHeight.getText().trim()));
        } catch (NumberFormatException e) {
            txtHeight.setText("");
        }
        patient.setAddress(txtAddress.getText());
        patient.setContactNumber(txtPhoneNumber.getText());
        patient.setEmail(txtEmail.getText());
        patient.setNote(txtNote.getText());
        return patient;
    }

    private Patient patientSetDataFromDB(ResultSet resultSet) {
        Patient patient = new Patient();

        try {
            Blob blob = resultSet.getBlob("image");
            Image image = new Image(blob.getBinaryStream(), 50, 50, true, true);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            patient.setImageView(imageView);
            patient.setPatientNumber(String.format("P%03d", resultSet.getInt("patient_number")));
            patient.setName(resultSet.getString("name"));
            patient.setIdNumber(resultSet.getString("id_number"));
            patient.setPassportNumber(resultSet.getString("passport_number"));
            patient.setBirthday(resultSet.getDate("birthday").toLocalDate());
            patient.setWeight((int) resultSet.getDouble("height"));
            patient.setHeight((int) resultSet.getDouble("weight"));
            patient.setAddress(resultSet.getString("address"));
            patient.setContactNumber(txtPhoneNumber.getText());                 // update contact list
            patient.setEmail(resultSet.getString("email"));
            patient.setNote(resultSet.getString("note"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patient;
    }

    private void validateData() {
        txtPatientNumber.getStyleClass().remove("invalid");
        cmbName.getStyleClass().remove("invalid");
        cmbIdNumber.getEditor().getStyleClass().remove("invalid");
        cmbPassportNumber.getEditor().getStyleClass().remove("invalid");
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
            if (value == null) throw new Exception();
        } catch (Exception e) {
            isValidate = false;
            txtBirthday.getStyleClass().add("invalid");
        }

        if (cmbPassportNumber.getEditor().getText().isEmpty()) {
            isValidate = false;
            cmbPassportNumber.getEditor().getStyleClass().add("invalid");
        }

        if (cmbIdNumber.getEditor().getText().isEmpty()) {
            isValidate = false;
            cmbIdNumber.getEditor().getStyleClass().add("invalid");
        }

        if (cmbName.getEditor().getText().isEmpty()) {
            isValidate = false;
            cmbName.getStyleClass().add("invalid");
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
        Image image = dataSaveRetrieve.retrievePatientImage(Integer.parseInt((patient.getPatientNumber().substring(1, 4))));
        imgPatient.setImage(image);
        txtPatientNumber.setValue(patient.getPatientNumber());
        cmbName.getEditor().setText(patient.getName());
        cmbName.getEditor().setText(patient.getName());
        cmbIdNumber.getEditor().setText(patient.getIdNumber());
        cmbPassportNumber.getEditor().setText(patient.getPassportNumber());
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

    private String generatePatientNumber() {
        int i = dataSaveRetrieve.lastPatientID();
        return String.format("P%03d", i + 1);
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
    void txtWeightOnKeyReleased(KeyEvent event) {

    }

    public void cmbIdNumberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            System.out.println(index);
            cmbIdNumber.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbIdNumber.hide();
            txtPatientNumber.hide();

        } else {
            cmbIdNumber.show();
        }

    }

    public void cmbPassportNumberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            System.out.println(index);
            cmbPassportNumber.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbPassportNumber.hide();
            txtPatientNumber.hide();

        } else {
            cmbPassportNumber.show();
        }

    }
}
