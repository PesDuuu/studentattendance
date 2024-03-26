package Controllers.User;

import Controllers.Alert.AlertCustom;
import Models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addStudentToAttendanceController implements Initializable {



    @FXML
    Button btnAdd, btnCancel;
    @FXML
    TextField txtStudentCode;
    @FXML
    Label lblClassroomId, lblNumberSession;


    ScheduleDAO scheduleDAO = new ScheduleDAO();
    AttendanceSessionDAO attendanceSessionDAO = new AttendanceSessionDAO();
    AttendanceSessionDetailsDAO attendanceSessionDetailsDAO = new AttendanceSessionDetailsDAO();
    AlertCustom alertCustom = new AlertCustom();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addStudentToAttendance() throws Exception {
        int attendanceSessionId = attendanceSessionDAO.getIdByNumberSessionAndClassroomId(lblNumberSession.getText(), lblClassroomId.getText());
        if(txtStudentCode.getText().isEmpty() || txtStudentCode.getText().length() != 10){
            alertCustom.alertError("Mã sinh viên không hợp lệ");
            return;
        }

        if(!scheduleDAO.checkClassroom_StudentExist(lblClassroomId.getText(), txtStudentCode.getText())){
            alertCustom.alertError("Mã sinh viên không tồn tại");

            return;
        }

//        int attendanceSessionId = attendanceSessionDAO.getIdByNumberSessionAndClassroomId(lblNumberSession.getText(), lblClassroomId.getText());

        if(attendanceSessionDetailsDAO.checkExistStudentInAttendanceSessionDetails(attendanceSessionId, txtStudentCode.getText())){
            alertCustom.alertError("Sinh viên đã được điểm danh");
            return;
        }

        if(attendanceSessionDetailsDAO.insertStudent(attendanceSessionId, txtStudentCode.getText())){
            alertCustom.alertSuccess("Thêm thành công!");
            CloseForm();
            return;
        }

        System.out.println("classroom: " + lblClassroomId.getText());
        System.out.println("session: " + lblNumberSession.getText());
        System.out.println("id: " + attendanceSessionId);
    }

    public void setLbl(String classroomId, String numberOfSession){
        lblClassroomId.setText(classroomId);
        lblNumberSession.setText(numberOfSession);
    }

    @FXML
    public void CloseForm() throws IOException {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
