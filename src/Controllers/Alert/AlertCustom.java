package Controllers.Alert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertCustom {
    public AlertCustom() {
    }

    public void alertSuccess(String successName) throws IOException {
        Stage successForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Alert/alertSuccessfulForm.fxml"));
        Parent root = loader.load();
        successForm.setScene(new Scene(root));
        alertSuccessfulController alertSuccessfulController = loader.getController();
        alertSuccessfulController.getNameSuccess(successName);
        successForm.initStyle(StageStyle.UNDECORATED);
        successForm.initModality(Modality.APPLICATION_MODAL);
        successForm.showAndWait();
    }
    public void alertError(String errorName) throws IOException{
        Stage errorForm = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Alert/alertErrorForm.fxml"));
        Parent root = loader.load();
        errorForm.setScene(new Scene(root));
        alertErrorController alertErrorController = loader.getController();
        alertErrorController.getNameError(errorName);
        errorForm.initStyle(StageStyle.UNDECORATED);
        errorForm.initModality(Modality.APPLICATION_MODAL);
        errorForm.showAndWait();
    }
}
