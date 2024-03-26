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

public class alertErrorController implements Initializable {
    @FXML
    ImageView imgError;
    @FXML
    Button btnOK;
    @FXML
    Label lblTittle;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImage();
    }
    public void setImage(){
        File add = new File("resources/images/alert-error.png");
        Image addclass = new Image(add.toURI().toString());
        imgError.setImage(addclass);
    }
    public void OK(){
        Stage stage =(Stage)btnOK.getScene().getWindow();
        stage.close();
    }
    public void getNameError(String error){
        lblTittle.setText(error);
    }
}
