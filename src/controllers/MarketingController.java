package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import data_access.*;
import helpers.dialog_messages.DialogMessages;
import helpers.sms.TwilioMain;
import helpers.sms.TwillioHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.BulkSMS;
import models.Customer;
import models.SingleSMS;
import models.SMSTemplate;
import validation.SMSTemplateFormValidations;


public class MarketingController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitialLoad();

    }

    public void InitialLoad() {
        try {

            ShowCustomers();
            ShowSentSMS();
            LoadSMSComboBoxData();

            ShowSMSTemplates();

            //Load two dashboards


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //////////////////////////////////////////////
    // SMS Template //
    //////////////////////////////////////////////

    @FXML
    private StackPane stackpane;

    @FXML
    private JFXTextField tfSmsTemplateId;

    @FXML
    private JFXTextField tfSmsTemplateName;

    @FXML
    private JFXTextArea taSmsTemplateMessage;

    @FXML
    private JFXButton btnCreateTemplate;

    @FXML
    private JFXButton btnUpdateTemplate;

    @FXML
    private JFXTextField tfSmsTemplateSearchTerm;

    @FXML
    private TableView<SMSTemplate> tvSMSTemplate;

    @FXML
    private TableColumn<SMSTemplate, Integer> colSmsTemplateId;

    @FXML
    private TableColumn<SMSTemplate, String> colSmsTemplateName;

    @FXML
    private TableColumn<SMSTemplate, String> colSmsTemplateMsg;

    private ObservableList<SMSTemplate> smsTemplateList;

    private SMSTemplate stmodel = null;
    private DataSource db = null;
    private Connection conn = null;
    private SMSTemplateDAO smsTemplateDAO = null;

    private void ShowSMSTemplates() throws SQLException {
        try {
            smsTemplateList = LoadSMSTemplatesFromDB();
            colSmsTemplateId.setCellValueFactory(new PropertyValueFactory<SMSTemplate, Integer>("id"));
            colSmsTemplateName.setCellValueFactory(new PropertyValueFactory<SMSTemplate, String>("name"));
            colSmsTemplateMsg.setCellValueFactory(new PropertyValueFactory<SMSTemplate, String>("message"));
            tvSMSTemplate.setItems(smsTemplateList);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private ObservableList<SMSTemplate> LoadSMSTemplatesFromDB() throws SQLException {
        ObservableList<SMSTemplate> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);
            list = smsTemplateDAO.getAllSMSTemplates();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closing connection resources
            if (smsTemplateDAO != null)
                smsTemplateDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        return list;

    }

    public void tvSmsTemplateMouseClicked(MouseEvent event) {
        SMSTemplate smsTemplate = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            smsTemplate = tvSMSTemplate.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (smsTemplate == null) {
            return;
        } else {
            tfSmsTemplateId.setText(String.valueOf(smsTemplate.getId()));
            tfSmsTemplateName.setText(smsTemplate.getName());
            taSmsTemplateMessage.setText(smsTemplate.getMessage());
            btnCreateTemplate.setDisable(true);
            btnUpdateTemplate.setDisable(false);
        }
    }

    //DataTable Search Function SMSTemplate TAB
    public void SearchFunctionSMSTemplate() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<SMSTemplate> filteredData = new FilteredList<>(smsTemplateList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSmsTemplateSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(SMSTemplate -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (SMSTemplate.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (String.valueOf(SMSTemplate.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<SMSTemplate> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvSMSTemplate.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvSMSTemplate.setItems(sortedData);

    }

    //SMSTemplate Tab
    public void clearSMSTemplateTextFields() {
        tfSmsTemplateId.clear();
        tfSmsTemplateName.clear();
        taSmsTemplateMessage.clear();
    }

    public void btnSMSTemplateKeyClicked(KeyEvent event) {

        if (event.getCode() == KeyCode.ESCAPE) {
            clearSMSTemplateTextFields();
            tvSMSTemplate.getSelectionModel().clearSelection();
            btnCreateTemplate.setDisable(false);
            btnUpdateTemplate.setDisable(true);
        }

    }

    //Todo :SMS Template Dashboard
    public void LoadSMSTemplateDashboardData() {

    }

    public void btnSMSTemplateUpdateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try {
            stmodel = new SMSTemplate();
            stmodel.setId(Integer.parseInt(tfSmsTemplateId.getText()));
            stmodel.setName(tfSmsTemplateName.getText());
            stmodel.setMessage(taSmsTemplateMessage.getText());


            db = new DataSource();
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);

            boolean valid = SMSTemplateFormValidations.validate(stmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = smsTemplateDAO.updateSMSTemplate(stmodel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

            clearSMSTemplateTextFields();
            InitialLoad();
            btnUpdateTemplate.setDisable(true);
            btnCreateTemplate.setDisable(false);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (smsTemplateDAO != null)
                    smsTemplateDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void btnSMSTemplateCreateClicked(MouseEvent mouseEvent) {
        DialogMessages dm = new DialogMessages(stackpane);

        try {
            stmodel = new SMSTemplate();
            stmodel.setName(tfSmsTemplateName.getText());
            stmodel.setMessage(taSmsTemplateMessage.getText());

            db = new DataSource();
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);

            boolean valid = SMSTemplateFormValidations.validate(stmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = smsTemplateDAO.CreateNewSMSTemplate(stmodel);
            if (result == true)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();


            clearSMSTemplateTextFields();
            InitialLoad();
            btnCreateTemplate.setDisable(false);
            btnUpdateTemplate.setDisable(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (smsTemplateDAO != null)
                    smsTemplateDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    public void SMSTemplateCharacterLimitChecker(KeyEvent keyEvent) {
        int maxCharacters = 150;
        if (taSmsTemplateMessage.getText().length() >= maxCharacters) {
            keyEvent.consume();
        }
    }


    //////////////////////////////////////////////
    // SMS  //
    //////////////////////////////////////////////


    @FXML
    private JFXTextField tfSmsCustSearchTerm;

    @FXML
    private TableView<Customer> tvSMSCustomer;

    @FXML
    private TableColumn<Customer, Integer> colCustId;

    @FXML
    private TableColumn<Customer, String> colCustName;

    @FXML
    private TableColumn<Customer, String> colCustContactNo;

    @FXML
    private JFXComboBox<String> cbSMSMode;


    @FXML
    private JFXTextField tfSMSCustId;

    @FXML
    private JFXTextField tfSMSCustName;

    @FXML
    private JFXComboBox<String> cbSMSTemplate;

    @FXML
    private JFXTextArea taSMSMessage;

    @FXML
    private JFXButton btnSMSSend;

    @FXML
    private JFXTextField tfSmsSearchTerm;

    @FXML
    private TableView<SingleSMS> tvSMSSent;

    @FXML
    private TableColumn<SingleSMS, Integer> colSMSId;

    @FXML
    private TableColumn<SingleSMS, String> colSMSTemplateName;

    @FXML
    private TableColumn<SingleSMS, String> colSMSCustName;

    @FXML
    private TableColumn<SingleSMS, Date> colSMSDate;


    private ObservableList<Customer> customerslist;
    private ObservableList<SingleSMS> singleSmsSentlist;
    private SMSDAO smsDAO = null;
    private SMSModeDAO smsModeDAO = null;


    //Todo : SMS Dashboard
    public void LoadSMSDashboard() {

    }

    private void ShowCustomers() throws SQLException {
        try {
            customerslist = LoadCustomersFromDB();
            colCustId.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));

            colCustName.setCellValueFactory(cellData -> Bindings.createStringBinding(
                    () -> cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName()
            ));

            colCustContactNo.setCellValueFactory(new PropertyValueFactory<Customer, String>("contactNo"));
            tvSMSCustomer.setItems(customerslist);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private ObservableList<Customer> LoadCustomersFromDB() throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            smsDAO = new SMSDAO(conn);
            list = smsDAO.getCustomerData();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closing connection resources
            if (smsTemplateDAO != null)
                smsTemplateDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        return list;

    }


    private void ShowSentSMS() throws SQLException {
        try {
            singleSmsSentlist = LoadSentSmsFromDB();
            colSMSId.setCellValueFactory(new PropertyValueFactory<SingleSMS, Integer>("id"));
            colSMSDate.setCellValueFactory(new PropertyValueFactory<SingleSMS, Date>("SentDate"));
            colSMSCustName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getFirstName()));
            colSMSTemplateName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSmsTemplate().getName()));
            tvSMSSent.setItems(singleSmsSentlist);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private ObservableList<SingleSMS> LoadSentSmsFromDB() throws SQLException {
        ObservableList<SingleSMS> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            smsDAO = new SMSDAO(conn);
            list = smsDAO.getAllSentSMS();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closing connection resources
            if (smsDAO != null)
                smsDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        return list;

    }


    public void LoadSMSComboBoxData() throws SQLException {
        cbSMSMode.setItems(FXCollections.observableArrayList(LoadSMSMode()));
        cbSMSTemplate.setItems(FXCollections.observableArrayList(LoadSMSTemplates()));
    }

    private List<String> LoadSMSTemplates() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);
            list = smsTemplateDAO.getAllSMSTemplateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Close SQL Resources
        if (smsTemplateDAO != null)
            smsTemplateDAO.close();
        ConnectionResources.close(conn);
        return list;
    }

    private List<String> LoadSMSMode() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            smsModeDAO = new SMSModeDAO(conn);
            list = smsModeDAO.getAllSMSModeNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Close SQL Resources
        if (smsModeDAO != null)
            smsModeDAO.close();
        ConnectionResources.close(conn);
        return list;
    }

    public void tvSMSCustomerClicked(MouseEvent event) {
        String fName = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 1) {
            fName = tvSMSCustomer.getSelectionModel().getSelectedItem().getFirstName();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (fName == null) {
            return;
        } else {
            tfSMSCustId.setText(String.valueOf(tvSMSCustomer.getSelectionModel().getSelectedItem().getId()));
            String fname = tvSMSCustomer.getSelectionModel().getSelectedItem().getFirstName();
            String lname = tvSMSCustomer.getSelectionModel().getSelectedItem().getLastName();
            tfSMSCustName.setText(fName + lname);
        }
    }

    public void smsModeSelectionChanged(ActionEvent actionEvent) {
        if (cbSMSMode.getSelectionModel().getSelectedIndex() == 0) {
            tfSmsCustSearchTerm.setDisable(false);
            tvSMSCustomer.setDisable(false);
            taSMSMessage.clear();
            taSMSMessage.setDisable(true);
            cbSMSTemplate.setDisable(false);

        } else {
            tfSmsCustSearchTerm.setDisable(true);
            tvSMSCustomer.setDisable(true);
            tfSmsCustSearchTerm.clear();
            tfSMSCustId.clear();
            tfSMSCustName.clear();
            taSMSMessage.clear();
            taSMSMessage.setDisable(true);
            cbSMSTemplate.setDisable(false);
        }

    }

    public void smsTemplateSelectionChanged(ActionEvent actionEvent) throws SQLException {
        String selection = cbSMSTemplate.getValue();
        try {

            db = new DataSource();
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);
            String message = smsTemplateDAO.getSMSTemplateMessagebyName(selection);
            taSMSMessage.setText(message);
            taSMSMessage.setDisable(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Close SQL Resources
        if (smsTemplateDAO != null)
            smsTemplateDAO.close();
        ConnectionResources.close(conn);
    }

    public void btnSMSEscKeyClicked(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ESCAPE) {
            clearSMSTextFieldsAndComboBoxes();
            DisableSMSTextFieldsAndComboBoxes();
            LoadSMSComboBoxData();
        }
    }

    public void clearSMSTextFieldsAndComboBoxes() {
        tfSmsSearchTerm.clear();
        tfSmsCustSearchTerm.clear();
        taSMSMessage.clear();
        tfSMSCustId.clear();
        tfSMSCustName.clear();
    }

    public void DisableSMSTextFieldsAndComboBoxes() {
        tfSmsCustSearchTerm.setDisable(true);
        tvSMSCustomer.setDisable(true);
        taSMSMessage.setDisable(true);

        cbSMSTemplate.setItems(null);
        cbSMSMode.setItems(null);
        cbSMSTemplate.setDisable(true);
        tfSMSCustId.setDisable(true);
        tfSMSCustName.setDisable(true);
    }

    public void CustomerSearch() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Customer> filteredData = new FilteredList<>(customerslist, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSmsCustSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (Customer.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (String.valueOf(Customer.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Customer> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvSMSCustomer.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvSMSCustomer.setItems(sortedData);
    }

    public void SMSSentSearch() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<SingleSMS> filteredData = new FilteredList<>(singleSmsSentlist, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSmsSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(singleSMS -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (singleSMS.getCustomer().getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (String.valueOf(singleSMS.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<SingleSMS> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvSMSSent.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvSMSSent.setItems(sortedData);
    }

    public void SendSMSBtnClicked(ActionEvent actionEvent) throws SQLException, InterruptedException {
        SingleSMS singleSMS = null;
        BulkSMS bulkSMS=null;
        DialogMessages dm = new DialogMessages(stackpane);
        if (cbSMSMode.getValue() == null) {
            dm.NoMessageModeSelected();
            return;
        }
        if (cbSMSMode.getSelectionModel().getSelectedIndex() == 0) {
            Boolean isemptyDate = ValidateEmptySingleSMSData();
            if (isemptyDate == true)
                return;
            singleSMS = CollectRequiredSingleSMSData();

            Boolean isSingleSMSSent = SendSingleSMS(singleSMS);
            if (isSingleSMSSent == true) {
                dm.MessageDeliveredSuccessfully();
                DataSource db = new DataSource();
                conn = db.getConnection();
                smsDAO = new SMSDAO(conn);
                smsDAO.SaveSingleSMSSentData(singleSMS);
                clearSMSTemplateTextFields();
                clearSMSTextFieldsAndComboBoxes();
                InitialLoad();
                return;
            }

        }

        if (cbSMSMode.getSelectionModel().getSelectedIndex() == 1) {
            Boolean isemptyDate = ValidateEmptyBulkSMSData();
            if (isemptyDate == true)
                return;
            bulkSMS=CollectRequiredBulkSMSData();

            Boolean isBulkSMSSent = SendBulkSMS(bulkSMS);
            if(isBulkSMSSent==true){
                dm.MessageDeliveredSuccessfully();
                clearSMSTemplateTextFields();
                clearSMSTextFieldsAndComboBoxes();
                InitialLoad();
                return;
            }
        }
    }

    private BulkSMS CollectRequiredBulkSMSData() throws SQLException {
        BulkSMS bulkSMS=new BulkSMS();
        bulkSMS.setCustomerlist(customerslist);

        try {
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);
            String templateText = cbSMSTemplate.getValue();
            int templateId = smsTemplateDAO.getSMSTemplateIdByName(templateText);
            SMSTemplate smsTemplate = new SMSTemplate();
            smsTemplate.setId(templateId);
            smsTemplate.setMessage(taSMSMessage.getText());
            bulkSMS.setSmsTemplate(smsTemplate);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smsTemplateDAO != null)
                smsTemplateDAO.close();
        }


        //Get mode Id
        try {
            conn = db.getConnection();
            smsModeDAO = new SMSModeDAO(conn);
            String modeText = cbSMSMode.getValue();
            int mode = smsModeDAO.getSMSModeIdByName(modeText);
            bulkSMS.setModeId(mode);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smsDAO != null)
                smsDAO.close();
        }


        return bulkSMS;
    }

    private Boolean ValidateEmptySingleSMSData() {
        DialogMessages dm = new DialogMessages(stackpane);
        if (cbSMSTemplate.getValue() == null || taSMSMessage.getText().isEmpty()) {
            dm.NoMessageData();
            return true;
        }
        if (tfSMSCustId.getText().isEmpty() || tfSMSCustName.getText().isEmpty()) {
            dm.NoReceiverCustomerSelected();
            return true;
        }
        return false;
    }

    private SingleSMS CollectRequiredSingleSMSData() throws SQLException {
        SingleSMS singleSMS = new SingleSMS();
        //get customer data for model
        singleSMS.setCustomer(tvSMSCustomer.getSelectionModel().getSelectedItem());

        //Getting Template Id
        try {
            conn = db.getConnection();
            smsTemplateDAO = new SMSTemplateDAO(conn);
            String templateText = cbSMSTemplate.getValue();
            int templateId = smsTemplateDAO.getSMSTemplateIdByName(templateText);
            SMSTemplate smsTemplate = new SMSTemplate();
            smsTemplate.setId(templateId);
            smsTemplate.setMessage(taSMSMessage.getText());
            singleSMS.setSmsTemplate(smsTemplate);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smsTemplateDAO != null)
                smsTemplateDAO.close();
        }


        //Get mode Id
        try {
            conn = db.getConnection();
            smsModeDAO = new SMSModeDAO(conn);
            String modeText = cbSMSMode.getValue();
            int mode = smsModeDAO.getSMSModeIdByName(modeText);
            singleSMS.setModeId(mode);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (smsDAO != null)
                smsDAO.close();
        }


        return singleSMS;
    }

    private boolean SendSingleSMS(SingleSMS smsdata)  {
        Boolean isSent= false;
        String nonformattedContactNo = smsdata.getCustomer().getContactNo();
        String formattedContactNo = TwillioHelper.FormatSenderNumber(nonformattedContactNo);
        String message = smsdata.getSmsTemplate().getMessage();

        TwilioMain twilioMain=new TwilioMain(formattedContactNo,message);
        twilioMain.SendMessage();
        isSent = twilioMain.isSuccess();
        return isSent;
    }


    private Boolean SendBulkSMS(BulkSMS bulkSMS) {
        Boolean isSent=false;
        for(Customer cust:bulkSMS.getCustomerlist()){
            String nonformattedContactNo = cust.getContactNo();
            String formattedContactNo = TwillioHelper.FormatSenderNumber(nonformattedContactNo);
            String message=bulkSMS.getSmsTemplate().getMessage()
            TwilioMain twilioMain=new TwilioMain(formattedContactNo,message);
            twilioMain.SendMessage();
            isSent = twilioMain.isSuccess();
        }
        return isSent;
    }

    private Boolean ValidateEmptyBulkSMSData(){
        DialogMessages dm = new DialogMessages(stackpane);
        if (customerslist == null) {
            dm.NoCustomerDataAvailable();
            return true;
        }
        if (cbSMSTemplate.getValue() == null || taSMSMessage.getText().isEmpty()) {
            dm.NoMessageData();
            return true;
        }
        return false;
    }
}
