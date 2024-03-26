package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Schedule;
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

public class editScheduleController implements Initializable {
    @FXML
    Button btnClose;

    @FXML
    TextField txtClassroomID,txtStudentCode;

    String classroomId_old, studentCode_old;
    AlertCustom alertCustom = new AlertCustom();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    ScheduleDAO scheduleDAO = new ScheduleDAO();
    public void getSchedule(Schedule schedule){
        txtClassroomID.setText(String.valueOf(schedule.getClassroomId()));
        txtStudentCode.setText(schedule.getStudentCode());
        classroomId_old = txtClassroomID.getText();
        studentCode_old = txtStudentCode.getText();
    }

    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    public void Save() throws IOException {
        String ID = txtClassroomID.getText();
        String studentCode =txtStudentCode.getText();

        if (ID.isEmpty() || studentCode.isEmpty()){
            alertCustom.alertError("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        if (isNumeric(ID) == false){
            alertCustom.alertError("Mã lớp không hợp lệ!");
            return;
        }
        if(scheduleDAO.checkClassroomIdExist(txtClassroomID.getText())== false){
            alertCustom.alertError("Mã lớp học không tồn tại!");
            return;
        }

        if(scheduleDAO.checkStudentCodeExist(txtStudentCode.getText())== false){
            alertCustom.alertError("Mã sinh viên không tồn tại!");
            return;
        }

        Classroom_Student cs = new Classroom_Student(Integer.valueOf(ID),studentCode);
        if (scheduleDAO.update(cs,classroomId_old, studentCode_old)) {
            alertCustom.alertSuccess("Cập nhật lịch học thành công!");
        } else {
           alertCustom.alertError("Đã xảy ra lỗi!");
        }
        Close();
    }
}