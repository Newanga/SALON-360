package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import data_access.*;
import helpers.dialog_messages.DialogMessages;
import helpers.report_generation.ExportToExcel;
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
import models.Inventory;
import models.InventoryCategory;
import validation.InventoryCategoryFormValidation;
import validation.InventoryFormValidation;
import view_models.InventoryVM;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {


    @FXML
    private StackPane stackpane;

    @FXML
    private Label lblTotalNoOfItems;

    @FXML
    private Label lblTotalCategory;

    @FXML
    private JFXTextField tfICSearchTerm;

    @FXML
    private TableView<InventoryCategory> tvInventoryCategory;

    @FXML
    private TableColumn<InventoryCategory, Integer> coICId;

    @FXML
    private TableColumn<InventoryCategory, String> colICName;

    @FXML
    private TableColumn<InventoryCategory, String> colICDescription;

    @FXML
    private JFXTextField tfICid;

    @FXML
    private JFXTextField tfICName;

    @FXML
    private JFXTextArea taICDescription;

    @FXML
    private JFXButton btnICCreate;

    @FXML
    private JFXButton btnICUpdate;


    private ObservableList<InventoryCategory> inventoryCategoryList;

    private InventoryCategory icmodel = null;
    private DataSource db = null;
    private Connection conn = null;
    private InventoryCategoryDAO inventoryCategorydao = null;


    private void ShowInventoryCategories() throws SQLException {
        try {
            inventoryCategoryList = LoadInventoryCategoriesFromDB();
            coICId.setCellValueFactory(new PropertyValueFactory<InventoryCategory, Integer>("id"));
            colICName.setCellValueFactory(new PropertyValueFactory<InventoryCategory, String>("name"));
            colICDescription.setCellValueFactory(new PropertyValueFactory<InventoryCategory, String>("description"));
            tvInventoryCategory.setItems(inventoryCategoryList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private ObservableList<InventoryCategory> LoadInventoryCategoriesFromDB() throws SQLException {
        ObservableList<InventoryCategory> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            inventoryCategorydao = new InventoryCategoryDAO(conn);
            list = inventoryCategorydao.getAllInventoryCategories();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closing connection resources
            if (inventoryCategorydao != null)
                inventoryCategorydao.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        return list;

    }

    //Load Data from single table row to form to view or update  //IC TAB
    public void tvICMouseClicked(MouseEvent event) {
        InventoryCategory inventoryCategory = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            inventoryCategory = tvInventoryCategory.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (inventoryCategory == null) {
            return;
        } else {
            tfICid.setText(String.valueOf(inventoryCategory.getId()));
            tfICName.setText(inventoryCategory.getName());
            taICDescription.setText(inventoryCategory.getDescription());
            btnICCreate.setDisable(true);
            btnICUpdate.setDisable(false);
        }
    }

    public void SearchFunctionIC() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<InventoryCategory> filteredData = new FilteredList<>(inventoryCategoryList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfICSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(InventoryCategory -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (InventoryCategory.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (InventoryCategory.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches description name.
                } else if (String.valueOf(InventoryCategory.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<InventoryCategory> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvInventoryCategory.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvInventoryCategory.setItems(sortedData);

    }

    public void btnICUpdateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try {
            icmodel = new InventoryCategory();
            icmodel.setId(Integer.parseInt(tfICid.getText()));
            icmodel.setName(tfICName.getText());
            icmodel.setDescription(taICDescription.getText());


            db = new DataSource();
            conn = db.getConnection();
            inventoryCategorydao = new InventoryCategoryDAO(conn);

            boolean valid = InventoryCategoryFormValidation.validate(icmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = inventoryCategorydao.updateInventoryCategory(icmodel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

            clearICTextFields();
            InitialLoad();
            btnICUpdate.setDisable(true);
            btnICCreate.setDisable(false);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (inventoryCategorydao != null)
                    inventoryCategorydao.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void btnICCreateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);

        try {
            icmodel = new InventoryCategory();
            icmodel.setName(tfICName.getText());
            icmodel.setDescription(taICDescription.getText());

            db = new DataSource();
            conn = db.getConnection();
            inventoryCategorydao = new InventoryCategoryDAO(conn);

            boolean valid = InventoryCategoryFormValidation.validate(icmodel);
            if (!valid) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = inventoryCategorydao.createNewInventoryCategory(icmodel);
            if (result == true)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();


            clearICTextFields();
            InitialLoad();
            btnICCreate.setDisable(false);
            btnICUpdate.setDisable(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (inventoryCategorydao != null)
                    inventoryCategorydao.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public void btnICExcelExportClicked(MouseEvent mouseEvent) {
        ExportToExcel ex = new ExportToExcel(tvInventoryCategory, stackpane);
        ex.run();

    }

    public void btnICKeyClicked(KeyEvent event) {

        if (event.getCode() == KeyCode.ESCAPE) {
            clearICTextFields();
            tvInventoryCategory.getSelectionModel().clearSelection();
            btnICCreate.setDisable(false);
            btnICUpdate.setDisable(true);
        }

    }

    public void clearICTextFields() {
        tfICid.clear();
        tfICName.clear();
        taICDescription.clear();
        tfICSearchTerm.clear();
    }


    public void InitialLoad() {
        try {
            ShowInventoryCategories();

            //Show Inventory
            ShowInventory();

            //LoadComboBoxData
            LoadInventoryComboBoxData();


            LoadDashboardData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void LoadDashboardData() {
        InventoryVM inventoryVM;
        try {
            db = new DataSource();
            conn = db.getConnection();
            inventoryDAO = new InventoryDAO(conn);
            inventoryVM=inventoryDAO.getDashBoardData();
            lblTotalCategory.setText(String.valueOf(inventoryVM.getTotalInventoryCategory()));
            lblTotalNoOfItems.setText(String.valueOf(inventoryVM.getTotalNoOfItems()));
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

    }


    @FXML
    private JFXTextField tfISearchTerm;

    @FXML
    private TableView<Inventory> tvInventory;

    @FXML
    private TableColumn<Inventory, Integer> colIId;

    @FXML
    private TableColumn<Inventory, String> colIName;

    @FXML
    private TableColumn<Inventory, Double> colIPrice;

    @FXML
    private TableColumn<Inventory, Integer> colIQuantity;

    @FXML
    private TableColumn<Inventory, String> colICategory;

    @FXML
    private TableColumn<Inventory, String> colIDescription;

    @FXML
    private TableColumn<Inventory, String> colISpecialNotes;

    @FXML
    private JFXTextField tfIId;

    @FXML
    private JFXTextField tfIName;

    @FXML
    private JFXTextField tfIPrice;

    @FXML
    private JFXTextField tfIQuantity;

    @FXML
    private JFXComboBox<String> cbICategory;

    @FXML
    private JFXTextArea taIDescription;

    @FXML
    private JFXTextArea taISpecialNote;

    @FXML
    private JFXButton btnICreate;

    @FXML
    private JFXButton btnIUpdate;


    private InventoryCategoryDAO inventoryCategoryDAO;
    private InventoryDAO inventoryDAO;
    private ObservableList<Inventory> inventoryList;
    private Inventory iModel = null;

    public void LoadInventoryComboBoxData() throws SQLException {
        cbICategory.setItems(FXCollections.observableArrayList(loadInventoryCategory()));
    }

    private List<String> loadInventoryCategory() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {
            db = new DataSource();
            conn = db.getConnection();
            inventoryCategoryDAO = new InventoryCategoryDAO(conn);
            list = inventoryCategoryDAO.getAllInventoryCategoryNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (inventoryCategoryDAO != null)
            inventoryCategoryDAO.close();
        ConnectionResources.close(conn);

        return list;
    }

    private void ShowInventory() throws SQLException {
        try{
            inventoryList = LoadInventoryFromDB();
            colIId.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("id"));
            colIName.setCellValueFactory(new PropertyValueFactory<Inventory, String>("name"));
            colIDescription.setCellValueFactory(new PropertyValueFactory<Inventory, String>("description"));
            colISpecialNotes.setCellValueFactory(new PropertyValueFactory<Inventory, String>("specialNote"));
            colICategory.setCellValueFactory(new PropertyValueFactory<Inventory, String>("category"));
            colIPrice.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("price"));
            colIQuantity.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("quantity"));
            tvInventory.setItems(inventoryList);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public ObservableList<Inventory> LoadInventoryFromDB() throws SQLException {
        ObservableList<Inventory> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            inventoryDAO = new InventoryDAO(conn);
            list = inventoryDAO.getAllInventory();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (inventoryDAO != null)
                    inventoryDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;

    }

    public void clearITextFieldsAndComboBoxes() {
        tfIId.clear();
        tfIName.clear();
        tfIPrice.clear();
        tfIQuantity.clear();
        tfISearchTerm.clear();
        taIDescription.clear();
        taISpecialNote.clear();
        cbICategory.setItems(null);
    }

    public void tvIMouseClicked(MouseEvent event) {
        Inventory model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvInventory.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            tfIId.setText(String.valueOf(model.getId()));
            tfIName.setText(model.getName());
            tfIPrice.setText(String.valueOf(model.getPrice()));
            tfIQuantity.setText(String.valueOf(model.getQuantity()));
            taIDescription.setText(model.getDescription());
            taISpecialNote.setText(model.getSpecialNote());
            cbICategory.setValue(model.getCategory());
            btnICreate.setDisable(true);
            btnIUpdate.setDisable(false);
        }
    }

    public void btnIKeyClicked(KeyEvent event) throws SQLException {

        if (event.getCode() == KeyCode.ESCAPE) {
            tvInventory.getSelectionModel().clearSelection();
            clearITextFieldsAndComboBoxes();
            btnICreate.setDisable(false);
            btnIUpdate.setDisable(true);
            LoadInventoryComboBoxData();

        }

    }

    public void btnIExcelExportClicked(MouseEvent mouseEvent) {
        ExportToExcel ex = new ExportToExcel(tvInventory, stackpane);
        ex.run();

    }

    public void btnICreateClicked() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        DataSource db = new DataSource();
        conn = db.getConnection();
        inventoryDAO = new InventoryDAO(conn);

        try {
            iModel = new Inventory();


            //Double validation in parsing and prevent crash
            String price = tfIPrice.getText();
            Boolean isPriceValid = InventoryFormValidation.ValidatePrice(price);
            if (isPriceValid==false) {
                dm.InvalidPrice();
                return;
            } else {
                iModel.setPrice(Double.parseDouble(price));
            }

            String quantity = tfIQuantity.getText();
            Boolean isQuantityValid = InventoryFormValidation.ValidateQuantity(quantity);
            if (isQuantityValid==false) {
                dm.InvalidQuantity();
                return;
            } else {
                iModel.setQuantity(Integer.parseInt(quantity));
            }

            iModel.setName(tfIName.getText());
            iModel.setDescription(taIDescription.getText());
            iModel.setSpecialNote(taISpecialNote.getText());
            iModel.setCategory(cbICategory.getValue());


            //Check for empty data
            boolean idDataEmpty = InventoryFormValidation.validateEmptyData(iModel);
            if (idDataEmpty==true) {
                dm.EmptyDataInForm();
                return;
            }

            boolean success = inventoryDAO.CreateNewInventory(iModel);
            if (success)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            //closing Connection resources
            if (inventoryDAO != null)
                inventoryDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //clear from data
        clearITextFieldsAndComboBoxes();

        btnICreate.setDisable(false);
        btnIUpdate.setDisable(true);

        //Refresh Datatable for applied updates
        InitialLoad();
    }

    public void btnIUpdateClicked(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);

        try {
            db = new DataSource();
            conn = db.getConnection();
            inventoryDAO = new InventoryDAO(conn);

            iModel = new Inventory();

            //Double validation in parsing and prevent crash
            String price = tfIPrice.getText();
            Boolean isPriceValid = InventoryFormValidation.ValidatePrice(price);
            if (isPriceValid==false) {
                dm.InvalidPrice();
                return;
            } else {
                iModel.setPrice(Double.parseDouble(price));
            }

            String quantity = tfIQuantity.getText();
            Boolean isQuantityValid = InventoryFormValidation.ValidateQuantity(quantity);
            if (isQuantityValid==false) {
                dm.InvalidQuantity();
                return;
            } else {
                iModel.setQuantity(Integer.parseInt(quantity));
            }

            iModel.setId(Integer.parseInt(tfIId.getText()));
            iModel.setName(tfIName.getText());
            iModel.setDescription(taIDescription.getText());
            iModel.setSpecialNote(taISpecialNote.getText());
            iModel.setCategory(cbICategory.getValue());

            //Check for empty data
            boolean idDataEmpty = InventoryFormValidation.validateEmptyData(iModel);
            if (idDataEmpty==true) {
                dm.EmptyDataInForm();
                return;
            }

            Boolean result = inventoryDAO.UpdateInventory(iModel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            //closing Connection resources
            if (inventoryDAO != null)
                inventoryDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //clear from data
        clearITextFieldsAndComboBoxes();

        btnICreate.setDisable(false);
        btnIUpdate.setDisable(true);

        //Refresh Datatable for applied updates
        InitialLoad();
    }

    public void SearchFunctionI() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Inventory> filteredData = new FilteredList<>(inventoryList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfISearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Inventory -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every inventory with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Inventory.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else if (String.valueOf(Inventory.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Inventory> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvInventory.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvInventory.setItems(sortedData);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitialLoad();
    }
}
