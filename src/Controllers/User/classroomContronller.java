package Controllers.User;

import Models.Classroom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class classroomContronller implements Initializable {
    @FXML
    Label lblID,lblName,lblDate,lblCode;

    public void setClassroom(Classroom classroom){
        lblID.setText("ID:" + classroom.getClassroomId());
        lblName.setText("Name" + classroom.getClassroomName());
        lblDate.setText("Date: " + classroom.getCreateDate());
        lblCode.setText("Code: " + classroom.getTeacherCode());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
