package Controllers.Admin;

import Models.Classroom;
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
import java.util.ResourceBundle;

public class showAddClassroomExcelFileController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
    }
    @FXML
    ImageView imgBack;

    @FXML
    Button btnBack;

    @FXML
    TableView<Classroom> tbvClassroomSuccess;

    @FXML
    TableColumn<Classroom,String> colSuccessName;

    @FXML
    TableColumn <Classroom,String> colSuccessCreateDate;

    @FXML
    TableColumn <Classroom,String> colSuccessTeacherCode;

    @FXML
    TableColumn <Classroom,String> colSuccessID;

    @FXML
    TableView<Classroom> tbvClassroomExist;
    @FXML
    TableColumn<Classroom,String> colExistName;

    @FXML
    TableColumn<Classroom,String> colExistID;

    @FXML
    TableColumn <Classroom,String> colExistCreateDate;

    @FXML
    TableColumn <Classroom,String> colExistTeacherCode;

    @FXML
    TableView<ClassroomError> tbvClassroomError;
    @FXML
    TableColumn <ClassroomError,String> colErrorID;
    @FXML
    TableColumn <ClassroomError,String> colErrorName;

    @FXML
    private void Back(){
        Stage stage =(Stage)btnBack.getScene().getWindow();
        stage.close();
    }

    public void setImg(){
        File reload = new File("resources/images/back-adminForm.png");
        Image reLoad= new Image(reload.toURI().toString());
        imgBack.setImage(reLoad);
    }

    public class ClassroomError {
        String classroomCode;
        String Error;


        public ClassroomError(String classroomCode, String error) {
            this.classroomCode = classroomCode;
            Error = error;
        }

        public String getClassroomCode() {
            return classroomCode;
        }

        public void setClassroomCode(String classroomCode) {
            this.classroomCode = classroomCode;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }
    }

    public void getClassroomList(ObservableList<Classroom> ob, ObservableList<Classroom> exist, ObservableList<ClassroomError> error){
        System.out.println("Danh sách success"+ob);
        tbvClassroomSuccess.setItems(ob);
//        tbvClassroomSuccess.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colSuccessID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colSuccessName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colSuccessCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colSuccessTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));

        System.out.println("Danh sách exits"+exist);
        tbvClassroomExist.setItems(exist);
//        tbvClassroomExist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colExistID.setCellValueFactory(new PropertyValueFactory<>("classroomId"));
        colExistName.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        colExistCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colExistTeacherCode.setCellValueFactory(new PropertyValueFactory<>("teacherCode"));

        System.out.println("Danh sách error"+error);
        tbvClassroomError.setItems(error);
//        tbvClassroomError.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colErrorID.setCellValueFactory(new PropertyValueFactory<>("classroomCode"));
        colErrorName.setCellValueFactory(new PropertyValueFactory<>("Error"));
    }
}
