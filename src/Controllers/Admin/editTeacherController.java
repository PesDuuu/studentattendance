package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Teacher;
import Models.TeacherDAO;
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

public class editTeacherController implements Initializable {
    TeacherDAO teacherDAO = new TeacherDAO();
    AlertCustom alertCustom = new AlertCustom();

    @FXML
    TextField txtTeacherID, txtTeacherName, txtTeacherEmail, txtTeacherPhoneNumber;
    @FXML
    Button btnClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void getTeacher(Teacher teacher) {
        txtTeacherID.setText(teacher.getTeacherCode());
        txtTeacherName.setText(teacher.getFullName());
        txtTeacherEmail.setText(teacher.getEmail());
        txtTeacherPhoneNumber.setText(teacher.getPhoneNumber());
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    public void Save(ActionEvent ac) throws IOException {
        String ID = txtTeacherID.getText();
        String name = txtTeacherName.getText();
        String email = txtTeacherEmail.getText();
        String phoneNumber = txtTeacherPhoneNumber.getText();
        Teacher teacher = new Teacher(ID, name, email, phoneNumber);
        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()){
            alertCustom.alertError("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        if (txtTeacherPhoneNumber.getText().length() !=10 || isNumeric(txtTeacherPhoneNumber.getText())==false){
            alertCustom.alertError("Số điện thoại không hợp lệ!");
            return;
        }
        if (teacherDAO.update(teacher)) {
            alertCustom.alertSuccess("Cập nhật giảng viên thành công!");
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
