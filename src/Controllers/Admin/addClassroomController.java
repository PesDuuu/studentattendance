package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Controllers.Alert.alertErrorController;
import Models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class addClassroomController implements Initializable{

    @FXML
    Button btnClose,btnSave;
    @FXML
    TextField txtClassroomID,txtClassroomName;
    ClassroomDAO classroomDAO = new ClassroomDAO();
    @FXML
    DatePicker dpCreate;
    @FXML
    ComboBox<String> cbbTeacherCode;
    TeacherDAO teacherDAO = new TeacherDAO();
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
    }
    @FXML
    private void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
    private void loadCBB() {
        ArrayList<Teacher> teachers = teacherDAO.getTeacherList();
        for( Teacher teacher : teachers){
            cbbTeacherCode.getItems().addAll(teacher.getTeacherCode());
        }
    }

    public void Save() throws IOException {
        String classroomName = txtClassroomName.getText();
        String teacherCode = cbbTeacherCode.getSelectionModel().getSelectedItem();
        if (classroomName.isEmpty() || dpCreate.getValue() == null || cbbTeacherCode.getSelectionModel().getSelectedItem()==null) {
            alertCustom.alertError("Vui lòng đầy đủ thông tin!");
            return;
        }
        Classroom classroom = new Classroom(classroomName, Date.valueOf(dpCreate.getValue()), teacherCode);
        if (classroomDAO.insertClassroom(classroom)) {
            alertCustom.alertSuccess("Thêm phòng học thành công!");
            Close();
        }
    }
}
