package controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.CurrentUserData;

import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label lblFullName;

    @FXML
    private Label  lblEmail;

    @FXML
    private Label lblRole;

    @FXML
    private JFXButton btnPOS;

    @FXML
    private JFXButton btnAppointments;

    @FXML
    private JFXButton btnTransactions;

    @FXML
    private JFXButton btnCustomers;

    @FXML
    private JFXButton btnEmployees;

    @FXML
    private JFXButton btnAccounts;

    @FXML
    private JFXButton btnServices;

    @FXML
    private JFXButton btnVouchers;

    @FXML
    private JFXButton btnMarketing;

    @FXML
    private JFXButton btnInventory;

    @FXML
    private BorderPane borderpane;



    private JFXSpinner spinner;
    private boolean isLoaderActive=false;
    private String currentWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCurrentUserData();
        AuthorizeUser();
    }

    private void AuthorizeUser() {
        String currentRole=CurrentUserData.getRole();
        if(currentRole.equals("Owner")){
            AuthorizeAdmin();
        }
        if(currentRole.equals("Manager")){
            AuthorizeReceptionist();
        }
        if(currentRole.equals("Receptionist")){
            AuthorizeManager();
        }
    }

    private void AuthorizeAdmin() {
        btnAccounts.setDisable(false);
        btnAppointments.setDisable(false);
        btnCustomers.setDisable(false);
        btnEmployees.setDisable(false);
        btnInventory.setDisable(false);
        btnMarketing.setDisable(false);
        btnPOS.setDisable(false);
        btnServices.setDisable(false);
        btnTransactions.setDisable(false);
        btnVouchers.setDisable(false);
    }

    private void AuthorizeManager() {
        btnPOS.setDisable(false);
        btnAppointments.setDisable(false);
        btnCustomers.setDisable(false);
        btnMarketing.setDisable(false);
        btnInventory.setDisable(false);
    }

    private void AuthorizeReceptionist() {
        btnPOS.setDisable(false);
        btnAppointments.setDisable(false);
        btnCustomers.setDisable(false);
    }



    //Setting logged user details based on logged user
    private void setCurrentUserData(){
        lblFullName.setText(CurrentUserData.getFullName());
        lblEmail.setText(CurrentUserData.getEmail());
        lblRole.setText(CurrentUserData.getRole());
    }

    public void minimizeWindow(MouseEvent mouseEvent) {
        Stage s=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public void closeWindow(MouseEvent mouseEvent) {
        Stage s=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.close();
    }

    //Checking whether loader or another view is present
    private boolean currentViewState(String view) {
        if(isLoaderActive ==true ||currentWindow == view)
            return false;
        else {
            //Remove the current active view
            borderpane.getChildren().remove(borderpane.getCenter());
            return true;
        }

    }

    private void startLoader(){
        //Set loader state to true
        isLoaderActive =true;
        VBox hb=new VBox();
        spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);
    }

    private void loadViewInBackgroundThread(String view) {
        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                //Creating new view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + view +".fxml"));
                Parent root = loader.load();
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            //Remove loader in the current view
            borderpane.getChildren().remove(borderpane.getCenter());
            //Change State of Loader
            isLoaderActive =false;
            //Load New view
            borderpane.setCenter(root);
            //Set the name of the current loaded view
            currentWindow=view;
        });

        //Start the process in background thread.
        Thread thread = new Thread(loadUI);
        thread.start();
    }

    public void btnServicesClicked() {
        final String view="Services";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnCustomersClicked() {
        final String view="Customers";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnAccountsClicked() {
        final String view="Accounts";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnEmployeesClicked() {
        final String view="Employees";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnInventoryClicked() {
        final String view="Inventory";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnVouchersClicked() {
        final String view="Vouchers";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnAppointmentsClicked() {
        final String view="Appointments";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnMarketingClicked() {
        final String view="Marketing";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnPOSClicked() {
        final String view="POS";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }

    public void btnTransactionClicked() {
        final String view="Transactions";
        if(currentViewState(view)==false)
            return;
        startLoader();
        loadViewInBackgroundThread(view);
    }



}
