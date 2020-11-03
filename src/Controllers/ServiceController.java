package Controllers;

import DataAccess.*;
import Helpers.DialogMessages;
import Helpers.Export;
import Models.Service;
import Models.ServiceCategory;
import Validation.ServiceCategoryFormValidation;
import Validation.ServiceFormValidation;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {

    @FXML
    private JFXTextField tfSSearchTableData;
    @FXML
    private TableView<Service> tvServices;
    @FXML
    private TableColumn<Service, Integer> colSId;
    @FXML
    private TableColumn<Service, String> colSName;
    @FXML
    private TableColumn<Service, Double> colSPrice;
    @FXML
    private TableColumn<Service, String> colSCategory;
    @FXML
    private TableColumn<Service, String> colSState;
    @FXML
    private TableColumn<Service, String> colSDescription;


    @FXML
    private JFXTextField tfSId;
    @FXML
    private JFXTextField tfSName;
    @FXML
    private JFXTextField tfSPrice;
    @FXML
    private JFXTextArea taSDescription;
    @FXML
    private JFXComboBox<String> cbSServiceCategory;
    @FXML
    private JFXComboBox<String> cbSServiceState;
    @FXML
    private JFXButton btnSCreate;
    @FXML
    private JFXButton btnSUpdate;
    @FXML
    private Label lblSTotalServices;
    @FXML
    private Label lblSAvailableServices;
    @FXML
    private Label lblSNonAvailableServices;
    @FXML
    private Label lblSWithdrawnServices;


    //Dashboard Service category TAB
    public void LoadSDashboardData() {
        //Todo: stream and map data in the list
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitialLoad();
    }

    public void InitialLoad() {
        try {
            showServiceCategories();

            //Load Service Dashboard
            LoadSCDashboardData();


            //Show Services
            ShowServices();
            //LoadComboBoxData
            LoadServiceComboBoxData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private ServiceStateDAO serviceStatedao;
    private ServiceDAO servicedao;
    private ObservableList<Service> servicesList;
    private Service smodel = null;

    public void LoadServiceComboBoxData() throws SQLException {
        cbSServiceCategory.setItems(FXCollections.observableArrayList(loadServiceCategory()));
        cbSServiceState.setItems(FXCollections.observableArrayList(loadServiceState()));
    }

    private List<String> loadServiceState() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            serviceStatedao = new ServiceStateDAO(conn);
            list = serviceStatedao.getAllServiceStateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Close SQL Resources
        if (servicedao != null)
            serviceStatedao.close();
        ConnectionResources.close(conn);
        return list;
    }

    public List<String> loadServiceCategory() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            ServiceCategorydao = new ServiceCategoryDAO(conn);
            list = ServiceCategorydao.getAllServiceCategoryNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (serviceStatedao != null)
            ServiceCategorydao.close();
        ConnectionResources.close(conn);

        return list;
    }

    private void ShowServices() throws SQLException {
        servicesList = LoadServicesFromDB();
        colSId.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
        colSName.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

        colSDescription.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));

        colSState.setCellValueFactory(new PropertyValueFactory<Service, String>("state"));
        colSCategory.setCellValueFactory(new PropertyValueFactory<Service, String>("category"));
        colSPrice.setCellValueFactory(new PropertyValueFactory<Service, Double>("price"));
        tvServices.setItems(servicesList);
    }

    public ObservableList<Service> LoadServicesFromDB() throws SQLException {
        ObservableList<Service> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            servicedao = new ServiceDAO(conn);
            list = servicedao.getAllServices();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{
                //closing connection resources
                if (servicedao != null)
                    servicedao.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;

    }

    //DataTable Search Function SC TAB
    public void SearchFunctionS() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Service> filteredData = new FilteredList<>(servicesList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSSearchTableData.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(service -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (service.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (String.valueOf(service.getPrice()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches price.
                } else if (String.valueOf(service.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Service> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvServices.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvServices.setItems(sortedData);

    }

    public void clearSTextFieldsAndComboBoxes() {
        tfSId.clear();
        tfSName.clear();
        tfSPrice.clear();
        tfSSearchTableData.clear();
        cbSServiceState.setItems(null);
        cbSServiceCategory.setItems(null);
    }

    public void tvSMouseClicked(MouseEvent event) {
        Service model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvServices.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            tfSId.setText(String.valueOf(model.getId()));
            tfSName.setText(model.getName());
            tfSPrice.setText(String.valueOf(model.getPrice()));
            taSDescription.setText(model.getDescription());
            cbSServiceState.setValue(model.getState());
            cbSServiceCategory.setValue(model.getCategory());
            btnSCreate.setDisable(true);
            btnSUpdate.setDisable(false);
        }
    }

    public void btnSKeyClicked(KeyEvent event) throws SQLException {

        if (event.getCode() == KeyCode.ESCAPE) {
            tvServices.getSelectionModel().clearSelection();
            clearSTextFieldsAndComboBoxes();
            btnSCreate.setDisable(false);
            btnSUpdate.setDisable(true);
            LoadServiceComboBoxData();

        }

    }

    public void btnSExcelExportClicked(MouseEvent mouseEvent) {
        Export ex = new Export(tvServices, stackpane);
        ex.run();

    }

    public void btnSCreateClicked() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        DataSource db = new DataSource();
        conn = db.getConnection();
        servicedao = new ServiceDAO(conn);

        try {
            smodel = new Service();


            //Double validation in parsing and prevent crash
            String price = tfSPrice.getText();
            Boolean validPrice = ServiceFormValidation.ValidatePrice(price);
            if (!validPrice) {
                dm.InvalidPrice();
                return;
            } else {
                smodel.setPrice(Double.parseDouble(price));
            }

            smodel.setName(tfSName.getText());
            smodel.setDescription(taSDescription.getText());
            smodel.setCategory(cbSServiceCategory.getValue());
            smodel.setState(cbSServiceState.getValue());

            //Check for empty data
            boolean emptyData = ServiceFormValidation.validateEmptyData(smodel);
            if (emptyData) {
                dm.EmptyDataInForm();
                return;
            }

            boolean success = servicedao.CreateNewService(smodel);
            if (success)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();
        } catch (SQLException ex) {

        }

       try{
           //closing Connection resources
           if (servicedao != null)
               servicedao.close();
           if (conn != null)
               ConnectionResources.close(conn);
       }
       catch (Exception ex){
           ex.printStackTrace();
       }


        //clear from data
        clearSTextFieldsAndComboBoxes();

        btnSCreate.setDisable(false);
        btnSUpdate.setDisable(true);

        //Refresh Datatable for applied updates
        InitialLoad();
    }

    public void btnSUpdateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);

        try {
            db = new DataSource();
            conn = db.getConnection();
            ServiceCategorydao = new ServiceCategoryDAO(conn);

            smodel = new Service();

            //Double validation in parsing and prevent crash
            String price = tfSPrice.getText();
            Boolean validPrice = ServiceFormValidation.ValidatePrice(price);
            if (!validPrice) {
                dm.InvalidPrice();
                return;
            } else {
                smodel.setPrice(Double.parseDouble(price));
            }

            smodel.setId(Integer.parseInt(tfSId.getText()));
            smodel.setName(tfSName.getText());
            smodel.setDescription(taSDescription.getText());
            smodel.setCategory(cbSServiceCategory.getValue());
            smodel.setState(cbSServiceState.getValue());

            //Check for empty data
            boolean emptyData = ServiceFormValidation.validateEmptyData(smodel);
            if (emptyData) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = servicedao.UpdateService(smodel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        clearSTextFieldsAndComboBoxes();
        InitialLoad();
        btnSUpdate.setDisable(true);
        btnSCreate.setDisable(false);

        try{
            //closing connection resources
            if (servicedao != null)
                servicedao.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }




























































































































    @FXML
    private Label lblTotalServiceCategories;

    //Form
    @FXML
    private StackPane stackpane;

    @FXML
    private JFXTextField tfSCId;

    @FXML
    private JFXTextField tfSCName;

    @FXML
    private JFXTextArea taSCDescription;

    @FXML
    private JFXButton btnSCCreate;

    @FXML
    private JFXButton btnSCUpdate;

    //Service category Table
    @FXML
    private JFXTextField tfSCSearchTableData;

    @FXML
    private TableView<ServiceCategory> tvServiceCategory;

    @FXML
    private TableColumn<ServiceCategory, Integer> colSCId;

    @FXML
    private TableColumn<ServiceCategory, String> colSCName;
    @FXML
    private TableColumn<ServiceCategory, String> colSCDescription;

    private ObservableList<ServiceCategory> serviceCategoryList;


    private ServiceCategory scmodel = null;
    private DataSource db = null;
    private Connection conn = null;
    private ServiceCategoryDAO ServiceCategorydao = null;


    //SC TAB
    private void showServiceCategories() throws SQLException {
        try {
            serviceCategoryList = LoadServiceCategoriesFromDB();
            colSCId.setCellValueFactory(new PropertyValueFactory<ServiceCategory, Integer>("id"));
            colSCName.setCellValueFactory(new PropertyValueFactory<ServiceCategory, String>("name"));
            colSCDescription.setCellValueFactory(new PropertyValueFactory<ServiceCategory, String>("description"));
            tvServiceCategory.setItems(serviceCategoryList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    //Loading Data from DB  //SC TAB
    private ObservableList<ServiceCategory> LoadServiceCategoriesFromDB() throws SQLException {
        ObservableList<ServiceCategory> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            ServiceCategorydao = new ServiceCategoryDAO(conn);
            list = ServiceCategorydao.getAllServiceCategories();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closing connection resources
            if (serviceStatedao != null)
                ServiceCategorydao.close();
            if(conn!=null)
                ConnectionResources.close(conn);
        }
        return list;

    }

    //Load Data from single table row to form to view or update  //SC TAB
    public void tvSCMouseClicked(MouseEvent event) {
        ServiceCategory serviceCategory = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            serviceCategory = tvServiceCategory.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (serviceCategory == null) {
            return;
        } else {
            tfSCId.setText(String.valueOf(serviceCategory.getId()));
            tfSCName.setText(serviceCategory.getName());
            taSCDescription.setText(serviceCategory.getDescription());
            btnSCCreate.setDisable(true);
            btnSCUpdate.setDisable(false);
        }
    }


    //DataTable Search Function SC TAB
    public void SearchFunctionSC() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ServiceCategory> filteredData = new FilteredList<>(serviceCategoryList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSCSearchTableData.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(serviceCategory -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (serviceCategory.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (serviceCategory.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches description name.
                } else if (String.valueOf(serviceCategory.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<ServiceCategory> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvServiceCategory.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvServiceCategory.setItems(sortedData);

    }

    //SC TAB
    public void btnSCUpdateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try {
            scmodel = new ServiceCategory();
            scmodel.setId(Integer.parseInt(tfSCId.getText()));
            scmodel.setName(tfSCName.getText());
            scmodel.setDescription(taSCDescription.getText());


            db = new DataSource();
            conn = db.getConnection();
            ServiceCategorydao = new ServiceCategoryDAO(conn);

            boolean valid = ServiceCategoryFormValidation.validate(scmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = ServiceCategorydao.updateServiceCategory(scmodel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

            clearSCTextFields();
            InitialLoad();
            btnSCUpdate.setDisable(true);
            btnSCCreate.setDisable(false);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{
                //closing connection resources
                if (serviceStatedao != null)
                    ServiceCategorydao.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }

    }

    //SC TAB
    public void btnSCCreateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);

        try {
            scmodel = new ServiceCategory();
            scmodel.setName(tfSCName.getText());
            scmodel.setDescription(taSCDescription.getText());

            db = new DataSource();
            conn = db.getConnection();
            ServiceCategorydao = new ServiceCategoryDAO(conn);

            boolean valid = ServiceCategoryFormValidation.validate(scmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = ServiceCategorydao.createNewServiceCategory(scmodel);
            if (result == true)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();


            clearSCTextFields();
            InitialLoad();
            btnSCCreate.setDisable(false);
            btnSCUpdate.setDisable(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try{
                if (serviceStatedao != null)
                    serviceStatedao.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    //SC Tab
    public void clearSCTextFields() {
        tfSCId.clear();
        tfSCName.clear();
        taSCDescription.clear();
        tfSCSearchTableData.clear();
    }

    //SC Tab
    public void btnSCExcelExportClicked(MouseEvent mouseEvent) {
        Export ex = new Export(tvServiceCategory, stackpane);
        ex.run();

    }

    //SC TAB
    public void btnSCKeyClicked(KeyEvent event) {

        if (event.getCode() == KeyCode.ESCAPE) {
            clearSCTextFields();
            tvServiceCategory.getSelectionModel().clearSelection();
            btnSCCreate.setDisable(false);
            btnSCUpdate.setDisable(true);
        }

    }

    //Dashboard Service category TAB
    public void LoadSCDashboardData() {
        Long scListCount = serviceCategoryList.stream().count();
        String count = Long.toString(scListCount);
        lblTotalServiceCategories.setText(count);
    }


}
