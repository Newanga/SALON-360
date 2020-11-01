package Controllers;

import DataAccess.ConnectionResources;
import DataAccess.DataSource;
import DataAccess.ServiceCategoryDAO;
import Models.ServiceCategory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ServiceCategoryController implements Initializable {


    //Form Fields
    @FXML
    private JFXTextField tfId;

    @FXML
    private JFXTextField tfName;

    @FXML
    private JFXTextField tfDescription;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnUpdate;


    //Service category Table Fields

    @FXML
    private JFXTextField tfSearchTableData;

    @FXML
    private TableView<ServiceCategory> tvServiceCategory;

    @FXML
    private TableColumn<ServiceCategory,Integer> colId;

    @FXML
    private TableColumn<ServiceCategory,String> colName;
    @FXML
    private TableColumn<ServiceCategory,String> colDescription;

    private ObservableList<ServiceCategory> serviceCategoryList;


    private ServiceCategory model=null;
    private DataSource db=null;
    private Connection conn=null;
    private ServiceCategoryDAO dao=null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showServiceCategories();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void showServiceCategories() throws SQLException {
        try{
            serviceCategoryList=LoadServiceCategoriesFromDB();
            colId.setCellValueFactory(new PropertyValueFactory<ServiceCategory,Integer>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<ServiceCategory,String>("name"));
            colDescription.setCellValueFactory(new PropertyValueFactory<ServiceCategory,String>("description"));
            tvServiceCategory.setItems(serviceCategoryList);
        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    //Loading Data from DB
    private ObservableList<ServiceCategory> LoadServiceCategoriesFromDB() throws SQLException {
        ObservableList<ServiceCategory> list = FXCollections.observableArrayList();

        DataSource db=new DataSource();
        Connection conn =db.getConnection();
        ServiceCategoryDAO dao=new ServiceCategoryDAO(conn);
        list= dao.getAllServiceCategories();
        //closing connection resources
        dao.close();
        ConnectionResources.close(conn);

        return list;

    }

    //Load Data from single table row to form to view or update
    public void tvMouseClicked(MouseEvent event) {
        ServiceCategory serviceCategory=null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            serviceCategory = tvServiceCategory.getSelectionModel().getSelectedItem();
        }
        else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if(serviceCategory ==null) {
            return;
        }
        else {
            tfId.setText(String.valueOf(serviceCategory.getId()));
            tfName.setText(String.valueOf(serviceCategory.getName()));
            tfDescription.setText(String.valueOf(serviceCategory.getDescription()));
            btnCreate.setDisable(true);
            btnUpdate.setDisable(false);
        }
    }


    public void SearchFunction(){
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ServiceCategory> filteredData = new FilteredList<>(serviceCategoryList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearchTableData.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(serviceCategory -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (serviceCategory.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches name.
                } else if (serviceCategory.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches description name.
                }
                else if (String.valueOf(serviceCategory.getId()).indexOf(lowerCaseFilter)!=-1)
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


}
