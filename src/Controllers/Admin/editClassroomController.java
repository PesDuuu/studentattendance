package Controllers.Admin;

import Controllers.Alert.AlertCustom;
import Models.Classroom;
import Models.ClassroomDAO;
import Models.Teacher;
import Models.TeacherDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class editClassroomController implements Initializable{
    @FXML
    Button btnClose,btnSave;
    @FXML
    TextField txtClassroomID,txtClassroomName;
    @FXML
    DatePicker dpCreate;
    @FXML
    ComboBox<String> cbbTeacherCode;
    TeacherDAO teacherDAO = new TeacherDAO();
    ClassroomDAO classroomDAO = new ClassroomDAO();
    AlertCustom alertCustom = new AlertCustom();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCBB();
    }

    public void loadCBB(){
        ArrayList<Teacher> teachers = teacherDAO.getTeacherList();
        for (Teacher teacher : teachers){
            cbbTeacherCode.getItems().addAll(teacher.getTeacherCode());
        }
    }
    public void getClassroomID(Classroom classroom){
        txtClassroomID.setText(String.valueOf(classroom.getClassroomId()));
        txtClassroomName.setText(classroom.getClassroomName());
        dpCreate.valueProperty().setValue(LocalDate.parse(String.valueOf(classroom.getCreateDate())));
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        dpCreate.setConverter(converter);
        dpCreate.setPromptText("dd/MM/yyyy");
        cbbTeacherCode.setPromptText(classroom.getTeacherCode());
    }

    public void Save() throws IOException {
        Integer ID = Integer.valueOf(txtClassroomID.getText());
        String classroomName = txtClassroomName.getText();
        Date createDate = Date.valueOf(dpCreate.valueProperty().getValue());
        String teacherCode = cbbTeacherCode.getSelectionModel().getSelectedItem();
        if (teacherCode==null){
            teacherCode=cbbTeacherCode.getPromptText();
        }
        Classroom classroom = new Classroom(ID, classroomName, createDate, teacherCode);

        if (classroomName.isEmpty() || createDate.toString().isEmpty() || teacherCode.isEmpty()) {
            alertCustom.alertError("Vui lòng điển đầy đủ thông tin");
            return;
        }
        if (classroomDAO.update(classroom)) {
            alertCustom.alertSuccess("Cập nhật phòng học thành công!");
        } else {
            alertCustom.alertError("Đã xảy ra lỗi!");
        }
        Close();
    }
    public void Close(){
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
}
