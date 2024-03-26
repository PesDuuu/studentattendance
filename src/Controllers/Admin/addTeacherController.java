package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Controllers.Alert.alertErrorController;
import Models.Teacher;
import Models.TeacherDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class addTeacherController implements Initializable {

    @FXML
    Button btnSave,btnClose;

    @FXML
    TextField txtTeacherID,txtTeacherName,txtEmail,txtPhoneNumber;
    TeacherDAO teacherDAO = new TeacherDAO();
    AlertCustom alertCustom = new AlertCustom();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoUpperCase();
    }
    private void autoUpperCase(){
        txtTeacherID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtTeacherID.setText(txtTeacherID.getText().toUpperCase());
            }
        });
    }
    @FXML
    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    @FXML
    private void addTeacher() throws IOException {
        Teacher teacher = new Teacher(txtTeacherID.getText(),txtTeacherName.getText(), txtEmail.getText(),txtPhoneNumber.getText());
        if(txtTeacherID.getText().isEmpty() || txtTeacherName.getText().isEmpty() || txtEmail.getText().isEmpty()  || txtPhoneNumber.getText().isEmpty()) {
            alertCustom.alertError("Vui lòng đầy đủ thông tin!");
            return;
        }
        if (txtPhoneNumber.getText().length() !=10 || isNumeric(txtPhoneNumber.getText())==false){
            alertCustom.alertError("Số điện thoại không hợp lệ!");
            return;
        }
        if (teacherDAO.insertTeacher(teacher)){
            alertCustom.alertSuccess("Thêm giảng viên thành công!");
            Close();
        } else {
            alertCustom.alertError("Đã xảy ra lỗi!");
            return;
        }
    }
}
