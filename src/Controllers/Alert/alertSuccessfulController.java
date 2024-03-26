package Controllers.Alert;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class alertSuccessfulController implements Initializable {
    @FXML
    Button btnOK;
    @FXML
    Label lblTittle;
    @FXML
    ImageView imgSuccessful;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
    }
    public void setImage(){
        File success = new File("resources/images/successful.png");
        Image Success = new Image(success.toURI().toString());
        imgSuccessful.setImage(Success);
    }
    public void OK(){
        Stage stage = (Stage)btnOK.getScene().getWindow();
        stage.close();
    }
    public void getNameSuccess(String error){
        lblTittle.setText(error);
    }
}
