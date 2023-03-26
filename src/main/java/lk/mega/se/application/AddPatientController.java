package lk.mega.se.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
    public VBox vBoxStudentsList;
    public Pane paneHistory;
    public Button btnBack;
    public AnchorPane paneHome;
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

    public void initialize() throws SQLException {
        paneHome.getStyleClass().add("pane-background");
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
        tblPatients.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));

        tblPatients.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            Patient patient;
            if ((patient = tblPatients.getSelectionModel().getSelectedItem()) != null) {
                patientGetData(patient);
                btnDelete.setDisable(false);
                btnHistory.setDisable(false);
                btnAdd.setDisable(false);
            } else {
                btnDelete.setDisable(true);
                btnHistory.setDisable(true);
                btnAdd.setDisable(true);
            }
        });

        for (int i = 0; i < 6; i++) {
            tblPatients.getColumns().get(i).getStyleClass().add("center");
        }
        searchName();
        searchIdNumber();
        searchPassportNumber();
//        btnNew.fire();
    }

    private void searchPassportNumber() {
        txtPassportNumber.textProperty().addListener((observableValue, previous, current) -> {
            System.out.println("hsdgjsd");
            if(tblPatients.getSelectionModel().isEmpty()){
                ResultSet resultSet = dataSaveRetrieve.searchPatientsPassportNumber(txtPassportNumber.getText());
                tblPatients.getItems().clear();
                try {
                    while (resultSet.next()){
                        Patient patient = patientSetDataFromDB(resultSet);
                        tblPatients.getItems().add(patient);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchIdNumber() {
        txtIDNumber.textProperty().addListener((observableValue, previous, current) -> {
            if(tblPatients.getSelectionModel().isEmpty()){
                ResultSet resultSet = dataSaveRetrieve.searchPatientsIdNumber(txtIDNumber.getText());
                tblPatients.getItems().clear();
                try {
                    while (resultSet.next()){
                        Patient patient = patientSetDataFromDB(resultSet);
                        tblPatients.getItems().add(patient);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchName(){
        txtName.textProperty().addListener((observableValue, previous, current) -> {
            if(tblPatients.getSelectionModel().isEmpty()){
                ResultSet resultSet = dataSaveRetrieve.searchPatientsName(txtName.getText());
                tblPatients.getItems().clear();
                try {
                    while (resultSet.next()){
                        Patient patient = patientSetDataFromDB(resultSet);
                        tblPatients.getItems().add(patient);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void testingData() {
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
        if (gender.getSelectedToggle()!=null)gender.getSelectedToggle().setSelected(false);
        txtAddress.clear();
        lstContactNumbers.getItems().clear();
        txtNote.clear();

        txtName.requestFocus();

        btnHistory.setDisable(true);
        btnDelete.setDisable(true);
        tblPatients.getSelectionModel().clearSelection();
    }

    @FXML
    void btnHistoryOnAction(ActionEvent event) {
        vBoxStudentsList.setVisible(false);
        paneHistory.setVisible(true);

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
        int selectedIndex = tblPatients.getSelectionModel().getSelectedIndex();

        dataSaveRetrieve.updatePatientDatabase(
                imgPatient.getImage(),
                txtName.getText(),
                txtIDNumber.getText(),
                txtPassportNumber.getText(),
                txtBirthday.getValue(),
                (gender.getSelectedToggle() == tglMale) ? "Male" : "Female",
                txtWeight.getText().trim().isBlank()? null:Double.parseDouble(txtWeight.getText()),
                txtHeight.getText().trim().isBlank()? null:Double.parseDouble(txtHeight.getText()),
                txtAddress.getText(),
                numberList,
                txtEmail.getText(),
                txtNote.getText()
        );

        if (tblPatients.getSelectionModel().getSelectedItems().isEmpty()){
            Patient patient = patientSetDataFromField();
            tblPatients.getItems().add(patient);
        }else {
            Patient patient = patientSetDataFromField();
            tblPatients.getItems().remove(tblPatients.getSelectionModel().getSelectedIndex());
            tblPatients.getItems().add(patient);
        }

        btnNew.fire();
    }

    private void dataRetrieve() {

    }

    private boolean validateContactNumber(String number) {
        if (txtPhoneNumber.getText().trim().length() < 4) return false;
        return true;
    }

    private Patient patientSetDataFromField() {
        Patient patient = new Patient();

//        try {
//            Blob blob = resultSet.getBlob("");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        Image image = new Image(imgPatient.getImage().getUrl(), 50, 50, true, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        patient.setImageView(imageView);

        patient.setPatientNumber(txtPatientNumber.getText());
        patient.setName(txtName.getText());
        patient.setIdNumber(txtIDNumber.getText());
        patient.setPassportNumber(txtPassportNumber.getText());
        patient.setBirthday(txtBirthday.getValue());
        try {
            patient.setWeight(Integer.valueOf(txtWeight.getText().trim()));
        }catch (NumberFormatException e){
            txtWeight.setText("");
        }
        try {
            patient.setHeight(Integer.valueOf(txtHeight.getText().trim()));
        }catch (NumberFormatException e){
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
            if (value==null) throw new Exception();
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
        Image image = dataSaveRetrieve.retrievePatientImage(Integer.parseInt((patient.getPatientNumber().substring(1, 4))));
        imgPatient.setImage(image);
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

    public void btnBackOnAction(ActionEvent actionEvent) {
        vBoxStudentsList.setVisible(true);
        paneHistory.setVisible(false);
        btnNew.fire();
    }

    private String generatePatientNumber() {
        int i = dataSaveRetrieve.lastPatientID();
        return String.format("P%03d",i+1);
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
        if(event.getCode()== KeyCode.ENTER){
            char[] chars = txtPatientNumber.getText().toCharArray();
            if (chars[0]!='P') return;
            if (chars.length!=4) return;
            for (int i = 1; i < 4; i++) {
                if (!Character.isDigit(chars[i])) return;
            }
            int number =Integer.parseInt(txtPatientNumber.getText().substring(1, 4)) ;
            ResultSet resultSet = dataSaveRetrieve.searchPatientsIdNumber(number);
            try {
                tblPatients.getItems().clear();
                while (resultSet.next()){
                    Patient patient = patientSetDataFromDB(resultSet);
                    tblPatients.getItems().add(patient);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void txtWeightOnKeyReleased(KeyEvent event) {

    }
}
