package Controllers;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HomeController {


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
}
