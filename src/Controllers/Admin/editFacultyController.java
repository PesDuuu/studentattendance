package Controllers.Admin;
import Controllers.Alert.AlertCustom;
import Models.Faculty;
import Models.FacultyDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class editFacultyController implements Initializable {
    @FXML
    Button btnClose, btnSave;

    @FXML
    TextField txtFacultyID, txtFacultyName;
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) { }
    FacultyDAO facultyDAO = new FacultyDAO();
    public void getFaculty(Faculty faculty){
        txtFacultyID.setText(faculty.getFacultyCode());
        txtFacultyName.setText(faculty.getFacultyName());
    }
    public void Save(ActionEvent ac) throws IOException {
        String ID = txtFacultyID.getText();
        String name = txtFacultyName.getText();
        Faculty faculty = new Faculty(ID,name);
        if (name.isEmpty()){
            alertCustom.alertError("Vui lòng điền thông tin!");
            return;
        }
        if (facultyDAO.update(faculty)) {
            alertCustom.alertSuccess("Cập nhật khoa thành công!");
            Close();
        } else {
            alertCustom.alertError("Đã xảy ra lỗi!");
            return;
        }
    }
    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
}
