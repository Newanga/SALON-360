package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import data_access.DataSource;
import data_access.LogInDAO;
import helpers.dialog_messages.DialogMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LogInController {


    @FXML
    private StackPane rootPane;

    @FXML
    private JFXTextField tfUsername;

    @FXML
    private JFXPasswordField tfPassword;

    private DataSource db;
    private Connection conn;
    private LogInDAO logInDAO;


    public void loginUser() throws IOException {
        //checkForValidData
        DialogMessages dm = new DialogMessages(rootPane);
        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            dm.RecheckUserNameAndPassword();
            return;
        }

        Boolean isLogInSuccess = false;
        //Check For valid Login Details
        try {
            db = new DataSource();
            conn = db.getConnection();
            logInDAO = new LogInDAO(conn);
            isLogInSuccess = logInDAO.ValidateUser(tfUsername.getText(), tfPassword.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        } finally {
            try {
                if (logInDAO != null) {
                    logInDAO.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (isLogInSuccess == true) {
            Parent root = FXMLLoader.load(getClass().getResource("../views/Home.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("main/app.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            //Hide LoginWindow
            rootPane.getScene().getWindow().hide();
        }
        else {
            dm.RecheckUserNameAndPassword();
        }


    }

    public void closeWindow(MouseEvent mouseEvent) {
        Stage s=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        s.close();
    }
}
