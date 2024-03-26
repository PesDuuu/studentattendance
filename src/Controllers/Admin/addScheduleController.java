package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Controllers.Alert.alertErrorController;
import Models.ScheduleDAO;
import Models.Classroom_Student;
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

public class addScheduleController implements Initializable {
    @FXML
    Button btnSave,btnClose;

    @FXML
    TextField txtClassroomID,txtStudentCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    ScheduleDAO scheduleDAO = new ScheduleDAO();
    AlertCustom alertCustom = new AlertCustom();

    @FXML
    private void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    public void Save() throws IOException {
        String ID = txtClassroomID.getText();
        String studentCode = txtStudentCode.getText();
        if (ID.isEmpty() || studentCode.isEmpty()) {
            alertCustom.alertError("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        if (isNumeric(ID)==false){
            alertCustom.alertError("Mã phòng học không hợp lệ!");
            return;
        }
        if(scheduleDAO.checkClassroomIdExist(txtClassroomID.getText())== false) {
            alertCustom.alertError("Mã lớp không tồn tại!");
            return;
        }

        if(scheduleDAO.checkStudentCodeExist(txtStudentCode.getText())== false) {
            alertCustom.alertError("Mã sinh viên không tồn tại");
            return;
        }
        if(scheduleDAO.checkClassroom_StudentExist(txtClassroomID.getText(),txtStudentCode.getText()) == true) {
            alertCustom.alertError("Sinh viên đã tồn tại trong lớp học này!");
            return;
        }
        if (Integer.valueOf(ID)==null){
            alertCustom.alertError("Mã phòng học không hợp lệ");
            return;
        }
        Classroom_Student cs = new Classroom_Student(Integer.valueOf(ID), studentCode);
        if (scheduleDAO.insert(cs)) {
            alertCustom.alertSuccess("Thêm lịch học thành công!");
            Close();
        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }


}
