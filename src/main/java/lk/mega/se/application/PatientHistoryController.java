package lk.mega.se.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.mega.se.data.BillDB;
import lk.mega.se.data.BillDBImpl;
import lk.mega.se.data.DataSaveRetrieve;
import lk.mega.se.data.DataSaveRetrieveImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class PatientHistoryController {

    @FXML
    private AnchorPane anchorPaneBill;

    @FXML
    private AnchorPane anchorPanePatient;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnAddBill;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnViewHistory;

    @FXML
    private ComboBox<String> cmbIdNumber;

    @FXML
    private ComboBox<String> cmbName;

    @FXML
    private ComboBox<String> cmbPassportNumber;

    @FXML
    private ToggleGroup gender;

    @FXML
    private Label lblInvalidInvoice;

    @FXML
    private ListView<String> lstContactNumbers;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<PatientHistory> tblService;

    @FXML
    private RadioButton tglFemale;

    @FXML
    private RadioButton tglMale;

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

    @FXML
    private TextField txtSchInvoice;
    private DataSaveRetrieve dataSaveRetrieve = new DataSaveRetrieveImpl();
    private BillDB billData = new BillDBImpl();

    public static int patientId ;
    public void initialize(){
        ResultSet resultSet = dataSaveRetrieve.searchPatientsIdNumber(patientId);
        try {
            resultSet.next();
            Patient patient = patientSetDataFromDB(resultSet);
            patientGetData(patient);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setHistoryData();

        tblService.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        tblService.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        tblService.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("service"));
        tblService.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblService.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
        tblService.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("serviceTime"));
        tblService.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("cost"));
        tblService.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        tblService.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblService.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("tax"));
        tblService.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }
    @FXML
    void btnAddBillOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {

    }

    @FXML
    void btnSavaOnAction(ActionEvent event) {

    }

    @FXML
    void btnViewHistoryOnAction(ActionEvent event) {

    }

    @FXML
    void cmbIdNumberOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void cmbNameOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void cmbPassportNumberOnKeyReleased(KeyEvent event) {

    }

    @FXML
    void lstContactNumbersOnKeyReleased(KeyEvent event) {

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
    void txtSchInvoiceOnKeyReleased(KeyEvent event) {

    }

    private void setHistoryData(){
        try {
            tblService.getItems().clear();
            ResultSet rst = billData.getPatientHistory(patientId);
            while (rst.next()) {
                PatientHistory patientHistory = new PatientHistory();
                patientHistory.setInvoiceNo(rst.getString("invoice_number"));
                patientHistory.setInvoiceDate(rst.getString("invoice_date"));
                patientHistory.setService(rst.getString("service"));
                patientHistory.setQty(rst.getString("qty"));
                patientHistory.setServiceDate(rst.getString("service_date"));
                patientHistory.setServiceTime(rst.getString("service_time"));
                patientHistory.setCost(rst.getString("cost"));
                patientHistory.setPaymentMethod(rst.getString("payment_method"));
                patientHistory.setDiscount(rst.getString("discount"));
                patientHistory.setTax(rst.getString("tax"));
                patientHistory.setTotalCost(rst.getString("final_cost"));
                tblService.getItems().add(patientHistory);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
    }


}
