package controllers;


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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    public void minimize(MouseEvent mouseEvent) {
        Stage s=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    public void close(MouseEvent mouseEvent) {
        Stage s=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.close();
    }
    @FXML
    private BorderPane borderpane;

    private JFXSpinner spinner;
    private boolean isLoaderActive=false;
    private String currentWindow;
    private Parent LoadUI(String view) {

        Parent root = null;
        try{
            root= FXMLLoader.load(getClass().getResource("/views/" + view + ".fxml"));
        }catch (IOException ex){

        }
        return root;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private boolean CurrentViewState(String view) {
        if(isLoaderActive ==true ||currentWindow==view)
            return false;
        else {
            borderpane.getChildren().remove(borderpane.getCenter());
            return true;
        }

    }

    private void StartLoader(){
        isLoaderActive =true;
        VBox hb=new VBox();
        spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);
    }

    private void LoadViewInBackgroundThread(String view) {
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

        Thread thread = new Thread(loadUI);
        thread.start();
    }

    public void btnServicesClicked() {
        final String view="Services";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnCustomersClicked() {
        final String view="Customers";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnAccountsClicked() {
        final String view="Accounts";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnEmployeesClicked() {
        final String view="Employees";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnInventoryClicked() {
        final String view="Inventory";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnVouchersClicked() {
        final String view="Vouchers";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnAppointmentsClicked() {
        final String view="Appointments";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnMarketingClicked() {
        final String view="Marketing";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnPOSClicked() {
        final String view="POS";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }

    public void btnTransactionClicked() {
        final String view="Transactions";
        if(CurrentViewState(view)==false)
            return;
        StartLoader();
        LoadViewInBackgroundThread(view);
    }






}
