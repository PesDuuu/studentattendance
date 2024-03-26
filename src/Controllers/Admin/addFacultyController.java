package Controllers.Admin;

import Models.Faculty;
import Models.FacultyDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Controllers.Alert.AlertCustom;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addFacultyController implements Initializable {
    FacultyDAO facultyDAO = new FacultyDAO();
    @FXML
    TextField txtFacultyID,txtFacultyName;

    @FXML
    Button btnClose,btnSave;

    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoUpperCase();
    }
    private void autoUpperCase(){
        txtFacultyID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtFacultyID.setText(txtFacultyID.getText().toUpperCase());
            }
        });
    }

    @FXML
    public void Save(ActionEvent a) throws IOException {
        String facultyCode = txtFacultyID.getText().toUpperCase();
        String facultyName = txtFacultyName.getText();
        Faculty faculty = new Faculty(facultyCode,facultyName);

        if (facultyCode.isEmpty() || facultyName.isEmpty()){
            alertCustom.alertError("Vui lòng điền đầy đủ thông tin");
            return;
        }
        if (facultyCode.length() !=2){
            alertCustom.alertError("Mã khoa không hợp lệ!");
            return;
        }
        if (facultyDAO.checkFacultyIDExit(facultyCode)) {
            alertCustom.alertError("Mã khoa đã tồn tại!");
            return;
        }
        if (facultyDAO.insert(faculty)){
            alertCustom.alertSuccess("Thêm khoa thành công!");
            Close();
        }
    }

    @FXML
    private void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
}
