package Controllers.Admin;

import Models.Student;
import javafx.collections.FXCollections;
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
import org.omg.CORBA.INITIALIZE;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class showAddStudentExcelFileController implements Initializable {
    @FXML
    TableView<Student> tbvStudentSuccess;

    @FXML
    TableColumn<Student,String> colSuccessID;

    @FXML
    TableColumn <Student,String> colSuccessName;

    @FXML
    TableColumn <Student, Date> colSuccessDate;

    @FXML
    TableColumn <Student,String> colSuccessAddress;

    @FXML
    TableColumn <Student,String> colSuccessClassCode;

    @FXML
    TableView<Student> tbvStudentExist;

    @FXML
    TableColumn<Student,String> colExistID;

    @FXML
    TableColumn <Student,String> colExistName;

    @FXML
    TableColumn <Student, Date> colExistDate;

    @FXML
    TableColumn <Student,String> colExistAddress;

    @FXML
    TableColumn <Student,String> colExistClassCode;

    @FXML
    TableView<StudentError> tbvStudentError;
    @FXML
    TableColumn <StudentError,String> colErrorID;
    @FXML
    TableColumn <StudentError,String> colErrorName;

    @FXML
    Button btnBack;

    @FXML
    ImageView imgBack;

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

    public class StudentError{
        String studentCode;
        String Error;

        public StudentError(String studentCode, String error) {
            this.studentCode = studentCode;
            Error = error;
        }

        public String getStudentCode() {
            return studentCode;
        }

        public void setStudentCode(String studentCode) {
            this.studentCode = studentCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }

    public void getStudentList(ObservableList<Student> ob,ObservableList<Student> exist,ObservableList<StudentError> error){
        System.out.println("Danh sách success"+ob);
        tbvStudentSuccess.setItems(ob);
//        tbvStudentSuccess.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colSuccessID.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colSuccessName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colSuccessDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colSuccessAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSuccessClassCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));

        System.out.println("Danh sách exits"+exist);
        tbvStudentExist.setItems(exist);
//        tbvStudentExist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colExistID.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colExistName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colExistDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colExistAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colExistClassCode.setCellValueFactory(new PropertyValueFactory<>("classCode"));

        System.out.println("Danh sách error"+error);
        tbvStudentError.setItems(error);
//        tbvStudentError.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colErrorID.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colErrorName.setCellValueFactory(new PropertyValueFactory<>("Error"));
    }
}
