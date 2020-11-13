package controllers;


import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
    private BorderPane borderpane;

    @FXML
    private JFXSpinner spinner;


    private Parent LoadUI(String UI) {

        Parent root = null;
        try{
            root= FXMLLoader.load(getClass().getResource("/views/" + UI + ".fxml"));
        }catch (IOException ex){

        }
        return root;
    }

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnServicesClicked(ActionEvent actionEvent) throws Exception {

        VBox hb=new VBox();
        JFXSpinner spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);

        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "Services" +".fxml"));
                Parent root = loader.load();
                spinner.setVisible(false);
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            borderpane.getChildren().remove(borderpane.getCenter());
            borderpane.setCenter(root);
        });

        Thread thread = new Thread(loadUI);
        thread.start();

    }


    public void btnCustomersClicked(ActionEvent actionEvent) {
        VBox hb=new VBox();
        JFXSpinner spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);

        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "Customers" +".fxml"));
                Parent root = loader.load();
                spinner.setVisible(false);
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            borderpane.getChildren().remove(borderpane.getCenter());
            borderpane.setCenter(root);
        });

        Thread thread = new Thread(loadUI);
        thread.start();
    }

    public void btnAccountsClicked(ActionEvent actionEvent) {
        VBox hb=new VBox();
        JFXSpinner spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);

        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "Accounts" +".fxml"));
                Parent root = loader.load();
                spinner.setVisible(false);
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            borderpane.getChildren().remove(borderpane.getCenter());
            borderpane.setCenter(root);
        });

        Thread thread = new Thread(loadUI);
        thread.start();
    }

    public void btnEmployeesClicked(ActionEvent actionEvent) {
        VBox hb=new VBox();
        JFXSpinner spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);

        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "Employees" +".fxml"));
                Parent root = loader.load();
                spinner.setVisible(false);
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            borderpane.getChildren().remove(borderpane.getCenter());
            borderpane.setCenter(root);
        });

        Thread thread = new Thread(loadUI);
        thread.start();
    }

    public void btnInventoryClicked(ActionEvent actionEvent) {
        VBox hb=new VBox();
        JFXSpinner spinner=new JFXSpinner();
        hb.getChildren().add(spinner);
        hb.setAlignment(Pos.CENTER);
        borderpane.setCenter(hb);

        Task<Parent> loadUI = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "Inventory" +".fxml"));
                Parent root = loader.load();
                spinner.setVisible(false);
                return root ;
            }
        };

        loadUI.setOnSucceeded(e -> {
            Parent root = loadUI.getValue();
            borderpane.getChildren().remove(borderpane.getCenter());
            borderpane.setCenter(root);
        });

        Thread thread = new Thread(loadUI);
        thread.start();
    }
}
