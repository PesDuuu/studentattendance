package Controllers.Admin;

import Models.Student;
import Models.Teacher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class showAddTeacherExcelFileController implements Initializable {
    @FXML
    Button btnBack;

    @FXML
    ImageView imgBack;

    @FXML
    TableView<Teacher> tbvTeacherSuccess;

    @FXML
    TableColumn<Teacher,String> colSuccessID;

    @FXML
    TableColumn <Teacher,String> colSuccessName;

    @FXML
    TableColumn <Teacher, Date> colSuccessEmail;

    @FXML
    TableColumn <Teacher,String> colSuccessPhone;

    @FXML
    TableView<Teacher> tbvTeacherExist;

    @FXML
    TableColumn<Teacher,String> colExistID;

    @FXML
    TableColumn <Teacher,String> colExistName;

    @FXML
    TableColumn <Teacher, Date> colExistEmail;

    @FXML
    TableColumn <Teacher,String> colExistPhone;

    @FXML
    TableView<teacherError> tbvTeacherError;
    @FXML
    TableColumn <teacherError,String> colErrorID;
    @FXML
    TableColumn <teacherError,String> colErrorName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
    }

    @FXML
    private void Back(){
        Stage stage =(Stage)btnBack.getScene().getWindow();
        stage.close();
    }

    public void setImg(){
        File reload = new File("resources/images/back-adminForm.png");
        Image reloadclass = new Image(reload.toURI().toString());
        imgBack.setImage(reloadclass);
    }

    public class teacherError {
        String teacherCode;
        String Error;

        public teacherError() {
        }

        public String getTeacherCode() {
            return teacherCode;
        }

        public void setTeacherCode(String teacherCode) {
            this.teacherCode = teacherCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }

        public teacherError(String teacherCode, String error) {
            this.teacherCode = teacherCode;
            Error = error;
        }
    }

    public void getTeacherList(ObservableList<Teacher> ob, ObservableList<Teacher> exist, ObservableList<teacherError> error){
        System.out.println("Danh sách success"+ob);
        tbvTeacherSuccess.setItems(ob);
//        tbvTeacherSuccess.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colSuccessID.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colSuccessName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colSuccessEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSuccessPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        System.out.println("Danh sách exits"+exist);
        tbvTeacherExist.setItems(exist);
//        tbvTeacherExist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colExistID.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colExistName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colExistEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colExistPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        System.out.println("Danh sách error"+error);
        tbvTeacherError.setItems(error);
//        tbvTeacherError.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colErrorID.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));
        colErrorName.setCellValueFactory(new PropertyValueFactory<>("Error"));
    }
}
