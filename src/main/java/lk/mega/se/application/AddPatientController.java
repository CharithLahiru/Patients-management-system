package lk.mega.se.application;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lk.mega.se.data.BillDB;
import lk.mega.se.data.BillDBImpl;
import lk.mega.se.data.DataSaveRetrieve;
import lk.mega.se.data.DataSaveRetrieveImpl;
import lk.mega.se.report.PrintReport;
import lk.mega.se.report.PrintReportImplement;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class AddPatientController {

    public Button btnNew;
    public Button btnAdd;
    public Button btnDelete;
    public ListView<String> lstContactNumbers;
    public RadioButton tglMale;
    public ToggleGroup gender;
    public RadioButton tglFemale;
    public ComboBox<String> cmbName;
    public ComboBox<String> cmbIdNumber;
    public ComboBox<String> cmbPassportNumber;
    public ComboBox<String> cmbService;
    public TableView<Service> tblService;
    public Button btnPrint;
    public Button btnCancel;
    public RadioButton rdoCash;
    public ToggleGroup paymentMethod;
    public RadioButton rdoCard;
    public Label lblServiceTotal;
    public Label lblDiscount;
    public Label lblTax;
    public Label lblFinalTotal;
    public Button btnServiceRemove;
    public DatePicker dpcServiceDate;
    public ComboBox<String> cmbServiceTime;
    public TextField txtDiscount;
    public TextField txtTax;
    public TextField txtQty;
    public Button btnServiceAdd;
    public AnchorPane anchorPaneBill;
    public AnchorPane anchorPanePatient;
    public TextField txtSchInvoice;
    public Label lblInvalidInvoice;
    public Button btnViewHistory;
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
    private TextArea txtNote;

    @FXML
    private ComboBox<String> txtPatientNumber;

    @FXML
    private TextField txtPhoneNumber;

    private DataSaveRetrieve dataSaveRetrieve = new DataSaveRetrieveImpl();
    private PrintReport printReport = new PrintReportImplement();
    private BillDB billData = new BillDBImpl();
    private boolean isValidate;
    private TextField[] textFields;
    private ArrayList<Patient> patients = new ArrayList<>();
    private int index = 0;

    private List<String> timeList = List.of("2.00 PM","2.15 PM","2.30 PM","2.45 PM","3.00 PM","3.15 PM","3.30 PM","3.45 PM","4.00 PM","4.15 PM","4.30 PM","4.45 PM");
    public void initialize() throws SQLException {
//        testingData();

        textFields = new TextField[]{txtPhoneNumber, txtEmail};

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

        //  bill side initialisation bellow

        for (Object service : billData.getServices()) {
            cmbService.getItems().add(service.toString());
        }

        for (String time : timeList) {
            cmbServiceTime.getItems().add(time);
        }

        searchService();
        searchTime();

        tblService.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        tblService.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("service"));
        tblService.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblService.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
        tblService.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("serviceTime"));
        tblService.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("cost"));

        setDatePickerRange();
        tblService.getSelectionModel().selectedItemProperty().addListener((observableValue, previous, current) -> {
            btnServiceRemove.setDisable(current==null);
        });

        tblService.getItems().addListener((ListChangeListener<? super Service>) observable -> {
            calculateTotalCost();
        });
    }

    private void calculateTotalCost() {
        Double svrCost = 0.0;
        for (Service svr : tblService.getItems()) {
            svrCost+= svr.getCost();
        }
        lblServiceTotal.setText(String.format("$ %.2f",svrCost));

        Double tax = svrCost*Double.parseDouble(txtTax.getText())/100;
        lblTax.setText(String.format("$ %.2f",tax));

        double discount = (svrCost+tax)*Double.parseDouble(txtDiscount.getText())/100;
        lblDiscount.setText(String.format("$ %.2f",discount));

        Double finalCost = svrCost+tax-discount;
        lblFinalTotal.setText(String.format("$ %.2f",finalCost));
    }

    private void setDatePickerRange() {
        dpcServiceDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                // create a new DateCell instance
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // disable days before today
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }

                        // disable days after 30 days from today
                        if (item.isAfter(LocalDate.now().plusDays(30))) {
                            setDisable(true);
                        }
                    }
                };
            }
        });

    }

    private void searchTime() {
        cmbServiceTime.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            if (cmbServiceTime.getSelectionModel().isEmpty()) cmbServiceTime.getItems().clear();
            if (cmbServiceTime.getEditor().getText().isBlank()) return;
            ArrayList<String> newTimeList = new ArrayList<>();
            for (String time : timeList) {
                char[] chars = cmbServiceTime.getEditor().getText().toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] != time.charAt(i)){
                        break;
                    }
                    if (i == chars.length-1){
                        newTimeList.add(time);
                    }
                }
            }
            for (String time : newTimeList) {
                cmbServiceTime.getItems().add(time);
            }
            cmbServiceTime.show();

        });
    }

    private void searchService() {
        cmbService.getEditor().textProperty().addListener((observableValue, previous, current) -> {
            if (cmbService.getSelectionModel().isEmpty()){
                ArrayList<String> srvList = new ArrayList<>();
                for (String srv : billData.searchService(current)) {
                    srvList.add(srv);
                }
                cmbService.getItems().clear();
                for (String s : srvList) {
                    cmbService.getItems().add(s);
                }
                cmbService.show();
            }
            else if (current.isBlank()){
                cmbService.getSelectionModel().clearSelection();
            }
        });
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
            if (txtPatientNumber.getEditor().getText().isBlank()) return;
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
        txtAddress.setText("rawathawaththa road, moratuwa.");
        lstContactNumbers.getItems().add("071-4596854");
        lstContactNumbers.getItems().add("077-2536348");
        txtEmail.setText("vishaka@gmail.com");

        txtNote.setText("This data for testing purposes");
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        editablePatientEdit(true);
        for (TextField textField : textFields) {
            textField.clear();
        }
        txtPatientNumber.getEditor().clear();
        cmbName.getEditor().clear();
        cmbIdNumber.getEditor().clear();
        cmbPassportNumber.getEditor().clear();

        txtPatientNumber.getEditor().setEditable(true);
        txtBirthday.setValue(null);
        if (gender.getSelectedToggle() != null) gender.getSelectedToggle().setSelected(false);
        txtAddress.clear();
        lstContactNumbers.getItems().clear();
        txtNote.clear();

        btnAddBill.setDisable(true);
        btnViewHistory.setDisable(true);
        btnDelete.setDisable(true);
        cmbName.requestFocus();
    }

    @FXML
    void btnAddBillOnAction(ActionEvent event) {
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        anchorPaneBill.setDisable(false);
        btnAddBill.setDisable(true);
        btnViewHistory.setDisable(true);
        btnViewHistory.setDisable(true);
    }

    public void btnViewHistoryOnAction(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
//        Stage primaryStage = (Stage) btnViewHistory.getScene().getWindow();
        try {
            PatientHistoryController patientHistoryController = new PatientHistoryController();
            patientHistoryController.patientId=getPatientId();
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/PatientHistory.fxml"))));
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPatientId(){
        return Integer.parseInt(txtPatientNumber.getEditor().getText().substring(1));
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
        if (!txtPatientNumber.getEditor().isEditable()){
            editablePatientEdit(true);
            txtPatientNumber.setEditable(false);
            return;
        }

        isValidate = false;
        validateData();
        if (!isValidate) return;

        ArrayList<String> numberList = new ArrayList<>();
        for (Object num : lstContactNumbers.getItems()) {
            numberList.add(num.toString());
        }

        dataSaveRetrieve.updatePatientDatabase(
                cmbName.getEditor().getText(),
                cmbIdNumber.getEditor().getText(),
                cmbPassportNumber.getEditor().getText(),
                txtBirthday.getValue(),
                (gender.getSelectedToggle() == tglMale) ? "Male" : "Female",
                txtAddress.getText(),
                numberList,
                txtEmail.getText(),
                txtNote.getText()
        );

        btnNew.fire();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        dataSaveRetrieve.deletePatient(Integer.parseInt(txtPatientNumber.getEditor().getText().substring(1, 4)));
        editablePatientEdit(true);
        txtPatientNumber.getEditor().clear();
        btnNew.fire();
    }

    private boolean validateContactNumber(String number) {
        if (txtPhoneNumber.getText().trim().length() < 4) return false;
        return true;
    }

    private Patient patientSetDataFromDB(ResultSet resultSet) {
        Patient patient = new Patient();


        try {
            patient.setPatientNumber(String.format("P%03d", resultSet.getInt("patient_number")));
            patient.setName(resultSet.getString("name"));
            patient.setIdNumber(resultSet.getString("id_number"));
            patient.setPassportNumber(resultSet.getString("passport_number"));
            LocalDate localDate = resultSet.getDate("birthday").toLocalDate();
            patient.setBirthday(localDate);
            Period period = Period.between(localDate, LocalDate.now());
            patient.setAge(period.getYears());
            patient.setAddress(resultSet.getString("address"));
            lstContactNumbers.getItems().clear();
            for (String patientNumber : dataSaveRetrieve.getContactList(resultSet.getInt("patient_number"))) {
                lstContactNumbers.getItems().add(patientNumber);
            }
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

        if (lstContactNumbers.getItems().size() == 0) {
            isValidate = false;
            lstContactNumbers.getStyleClass().add("invalid");
            txtPhoneNumber.requestFocus();
        }
    }

    private void patientGetData(Patient patient) {
        txtPatientNumber.setValue(patient.getPatientNumber());
        cmbName.getEditor().setText(patient.getName());
        cmbName.getEditor().setText(patient.getName());
        cmbIdNumber.getEditor().setText(patient.getIdNumber());
        cmbPassportNumber.getEditor().setText(patient.getPassportNumber());
        txtBirthday.setValue(patient.getBirthday());
        gender.selectToggle((patient.getGender() == Gender.MALE) ? tglMale : tglFemale);
        txtAge.setText(patient.getAge() + "");
        txtAddress.setText(patient.getAddress());
        txtEmail.setText(patient.getEmail());
        txtNote.setText(patient.getNote());
        //contact data set in setdata
    }
    public void txtSchInvoiceOnKeyReleased(KeyEvent event){

    }

    @FXML
    void tXtPhoneNumberOnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnAdd.fire();
        }
    }

    public void lstContactNumbersOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE){
            if (lstContactNumbers.getItems()==null){
                return;
            }
            btnDelete.fire();
        }
    }

    @FXML
    void txtAgeOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            txtNote.requestFocus();
        }
    }

    public void cmbNameOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (patients.isEmpty()){
                cmbIdNumber.requestFocus();
                txtPatientNumber.getEditor().clear();
                return;
            }
            cmbName.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbName.hide();
            txtPatientNumber.hide();
            editablePatientEdit(false);
        } else {
            cmbName.show();
        }
    }

    public void cmbIdNumberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (patients.isEmpty()){
                cmbPassportNumber.requestFocus();
                txtPatientNumber.getEditor().clear();
                return;
            }
            System.out.println(patients.get(index));
            cmbIdNumber.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbIdNumber.hide();
            txtPatientNumber.hide();

            btnSave.setText("Edit");
            editablePatientEdit(false);
        } else {
            cmbIdNumber.show();
        }

    }

    private void editablePatientEdit(boolean tru) {
        txtPatientNumber.getEditor().setEditable(tru);
        cmbName.getEditor().setEditable(tru);
        cmbIdNumber.getEditor().setEditable(tru);
        cmbPassportNumber.getEditor().setEditable(tru);
        txtBirthday.setEditable(tru);
        txtBirthday.setEditable(tru);
        tglFemale.setDisable(!tru);
        tglMale.setDisable(!tru);
        txtAddress.setEditable(tru);
        txtPhoneNumber.setEditable(tru);
        txtEmail.setEditable(tru);
        txtNote.setEditable(tru);

        btnAdd.setDisable(!tru);
        btnDelete.setDisable(tru);
        btnSave.setText(tru==false? "Edit":"Save");
        btnAddBill.setDisable(tru);
        btnViewHistory.setDisable(tru);
    }
    public void cmbPassportNumberOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (patients.isEmpty()){
                txtBirthday.requestFocus();
                txtPatientNumber.getEditor().clear();
                return;
            }

            cmbPassportNumber.getSelectionModel().select(null);
            patientGetData(patients.get(index));
            cmbPassportNumber.hide();
            txtPatientNumber.hide();
            editablePatientEdit(false);
        } else {
            cmbPassportNumber.show();
        }

    }

    // focused on bill detail bellow
    public void btnServiceAddOnAction(ActionEvent actionEvent) {
        if (!isValidServiceData()) return;
        Service service = new Service();
        service.setService(cmbService.getEditor().getText().trim());
        service.setQty(Integer.parseInt(txtQty.getText()));
        service.setServiceDate(dpcServiceDate.getValue());
        service.setServiceTime(getLocalTimeFromCmb());
        Double serviceCost = billData.getServiceCost(cmbService.getEditor().getText().trim());
        service.setCost(serviceCost*Integer.parseInt(txtQty.getText()));
        tblService.getItems().add(service);

        cmbService.getEditor().clear();
        txtQty.clear();
        dpcServiceDate.setValue(null);
        cmbServiceTime.getEditor().clear();

    }

    private LocalTime getLocalTimeFromCmb(){
        LocalTime localTime = null;
        try {
            String text = cmbServiceTime.getEditor().getText().trim();
            String[] split = text.split("\\.| ");
            if (split[2].strip().equals("AM") ||split[2].strip().equals("am")) {
                localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }else if (split[2].strip().equals("PM") ||split[2].strip().equals("pm")) {
                localTime = LocalTime.of(Integer.parseInt(split[0])+12, Integer.parseInt(split[1]));
            }
        } catch (Exception e){
            return null;
        }
        return localTime;
    }

    private boolean isValidServiceData() {
        boolean isValid = true;
        dpcServiceDate.getStyleClass().remove("invalid");
        txtQty.getStyleClass().remove("invalid");
        cmbService.getStyleClass().remove("invalid");
        cmbServiceTime.getStyleClass().remove("invalid");

        if (getLocalTimeFromCmb()==null){
            cmbServiceTime.getStyleClass().add("invalid");
            isValid=false;
            cmbServiceTime.requestFocus();
        }
        if (dpcServiceDate.getValue()==null || dpcServiceDate.getValue().isBefore(LocalDate.now())){
            dpcServiceDate.getStyleClass().add("invalid");
            isValid=false;
            dpcServiceDate.requestFocus();
        }
        try {
            Integer text = Integer.valueOf(txtQty.getText());

        }catch (NumberFormatException e){
            txtQty.getStyleClass().add("invalid");
            txtQty.requestFocus();
            isValid=false;
        }
        cmbService.getItems().contains(cmbService.getEditor().getText());
        if (!cmbService.getItems().contains(cmbService.getEditor().getText()) ){
            cmbService.getStyleClass().add("invalid");
            cmbService.requestFocus();
            cmbService.getEditor().selectAll();
            isValid = false;
        }
        return isValid;
    }

    public void btnServiceRemoveOnAction(ActionEvent actionEvent) {
        tblService.getItems().remove(tblService.getSelectionModel().getSelectedIndex());
        tblService.getSelectionModel().clearSelection();

    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblService.getItems().clear();
        btnNew.setDisable(false);
        btnAddBill.setDisable(false);
        btnViewHistory.setDisable(false);
        btnSave.setDisable(false);
        btnDelete.setDisable(false);
        anchorPaneBill.setDisable(true);
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
        int count = 0;
        for (Service service : tblService.getItems()) {
            if (service.getInvoiceNo() != "") continue;
            updateServiceDatabase(service);

            printReport.printJasperReport(LocalDate.now().toString(),
                    "456456985",
                    cmbName.getEditor().getText(),
                    lstContactNumbers.getItems().get(0),
                    txtAddress.getText(),
                    (paymentMethod.getSelectedToggle() == rdoCard) ? "Cars" : "Cash",
                    txtDiscount.getText(),
                    txtTax.getText(),
                    lblFinalTotal.getText(),
                    service.getService(),
                    String.valueOf(service.getQty()),
                    service.getServiceDate().toString(),
                    service.getServiceTime().toString(),
                    service.getCost().toString()
                    );
        }
    }

    private void updateServiceDatabase(Service service){
        String paymentMethodSelection = (paymentMethod.getSelectedToggle() == rdoCard)? "Card":"Cash";
        billData.updateBillDB(Integer.parseInt(txtPatientNumber.getEditor().getText().substring(1)),
                LocalDate.now(),
                service.getService(),
                service.getQty(),
                paymentMethodSelection,
                Double.parseDouble(txtDiscount.getText()),
                Double.parseDouble(txtTax.getText()),
                service.getServiceDate(),
                service.getServiceTime(),
                Double.parseDouble(lblServiceTotal.getText().substring(2)),
                Double.parseDouble(lblFinalTotal.getText().substring(2)));
    }

    public void rdoPaymentMethodOnAction(ActionEvent actionEvent) {
        if (rdoCash == paymentMethod.getSelectedToggle()){
            txtDiscount.setText("5");
        } else if(rdoCard == paymentMethod.getSelectedToggle()){
            txtDiscount.setText("0");
        }
        calculateTotalCost();
    }

}
