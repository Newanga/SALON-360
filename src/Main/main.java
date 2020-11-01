package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/CommonTemplates/TabView.fxml"));
        primaryStage.setTitle("Home");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Main/app.png"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();


    }


    public static void main(String[] args){
        launch(args);
    }


}
